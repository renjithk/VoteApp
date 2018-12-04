package com.voting.service;

import android.os.AsyncTask;
import android.util.Log;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.voting.entity.BlacklistAPIEntity;
import com.voting.entity.CandidateAPIEntity;
import com.voting.entity.CandidateEntity;
import com.voting.entity.ChartDataEntity;
import com.voting.entity.DataTrackerEntity;
import com.voting.entity.PeselEntity;
import com.voting.entity.UserEntity;
import com.voting.entity.VotingMapEntity;
import com.voting.localstorage.VotingDatabase;
import com.voting.mapper.IVotingMapper;
import com.voting.utils.StatusOptions;
import com.voting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation as defined in {@link IVotingService}
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
public class VotingService implements IVotingService {
    private final IVotingMapper mapper;
    private final VotingDatabase database;

    public VotingService(IVotingMapper mapper, VotingDatabase database) {
        this.mapper = mapper;
        this.database = database;
    }

    @Override
    public void syncAllData() {
        try {
            //fetch all candidates
            CandidateAPIEntity candidatesData = mapper.fetchAllCandidates();
            //save them to database
            database.votingDAO().insertAllCandidates(candidatesData.candidates());
            //fetch blacklisted pesels
            BlacklistAPIEntity blacklistData = mapper.fetchAllBlacklisted();
            //save them to database
            database.votingDAO().insertAllBlacklisted(blacklistData.blacklistedList());

            //delete all entries & save the last updated date
            database.votingDAO().deleteAllEntries();

            DataTrackerEntity dateTracker = new DataTrackerEntity(candidatesData.lastUpdated());
            database.votingDAO().insertLastUpdated(dateTracker);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isDataEmptyOrOutdated() {
        List<CandidateEntity> candidates = database.votingDAO().fetchAllCandidates();
        if(Utils.isEmptyList(candidates)) return true;

        //check if the data is outdated
        DataTrackerEntity lastUpdated = database.votingDAO().getLastUpdatedDate();
        return Utils.isDataOutdated(lastUpdated.getLastUpdated());
    }

    @Override
    public int proceedWithSignIn(UserEntity user) {
        //check if this is new user
        UserEntity existingUser = database.votingDAO().findUser(user.getForename(), user.getSurname(),
                                                                user.getPesel());
        if(null == existingUser) {
            //new user, add them into users table and proceed as normal
            database.votingDAO().insert(user);
            return StatusOptions.LOGIN_NEW_USER;
        }

        //existing user
        //check if they are blacklisted
        if(null != database.votingDAO().findIfUserBlacklisted(user.getPesel())) {
            //blacklisted, don't proceed
            return StatusOptions.LOGIN_FAIL_BLACKLISTED;
        }

        //normal user, check if they have already voted
        if(null == database.votingDAO().findUserAlreadyVoted(user.getPesel())) {
            //not voted
            return StatusOptions.LOGIN_OK;
        }
        return StatusOptions.LOGIN_ALREADY_VOTED;
    }

    @Override
    public List<CandidateEntity> getAllCandidates() {
        return database.votingDAO().fetchAllCandidates();
    }

    @Override
    public int registerVote(UserEntity user, CandidateEntity candidate) {
        //check whether the user is blacklist
        PeselEntity disallowed = database.votingDAO().findIfUserBlacklisted(user.getPesel());
        if(null == disallowed) { //not blacklisted
            //register vote
            VotingMapEntity voteMap = new VotingMapEntity(candidate.getName(),
                                                          candidate.getParty(), user.getPesel());
            database.votingDAO().insert(voteMap);

            return StatusOptions.REGISTER_VOTE_OK;
        } else {
            //add blacklist user entry to register vote on the voting keep the stats
            VotingMapEntity voteMap = new VotingMapEntity(candidate.getName(),
                                                          candidate.getParty(), null);
            database.votingDAO().insert(voteMap);

            return StatusOptions.BLACKLISTED;
        }
    }

    @Override
    public ChartDataEntity votesPerParty() {
        //get all part labels
        List<String> parties = database.votingDAO().findPartyNames();
        //build dataset list
        List<BarEntry> entries = new ArrayList<>(parties.size());

        for(int i = 0; i < parties.size(); i++) {
            int votes = database.votingDAO().findVotesEarnedByParty(parties.get(i));
            entries.add(new BarEntry(i, votes));
        }
        BarDataSet barDataSet = new BarDataSet(entries, "Votes earned by each party");

        return new ChartDataEntity(parties, barDataSet);
    }

    @Override
    public ChartDataEntity votesPerCandidate() {
        //get all part labels
        List<String> candidates = database.votingDAO().findCandidateNames();
        //build dataset list
        List<BarEntry> entries = new ArrayList<>(candidates.size());

        for(int i = 0; i < candidates.size(); i++) {
            int votes = database.votingDAO().findVotesEarnedByCandidate(candidates.get(i));
            entries.add(new BarEntry(i, votes));
        }
        BarDataSet barDataSet = new BarDataSet(entries, "Votes earned by each candidate");

        return new ChartDataEntity(candidates, barDataSet);
    }
}
