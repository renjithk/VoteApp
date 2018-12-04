package com.voting.localstorage.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.voting.entity.CandidateEntity;
import com.voting.entity.DataTrackerEntity;
import com.voting.entity.PeselEntity;
import com.voting.entity.UserEntity;
import com.voting.entity.VotingMapEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data access object for the all data manipulations within the app
 * Since there isn't many data manipulations, a single DAO is maintained for the whole app
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
@Dao
public interface VotingDAO {
    /**
     * Adds all candidates to the table, tbl_candidates
     * @param candidates list of candidates to be inserted
     */
    @Insert
    void insertAllCandidates(List<CandidateEntity> candidates);

    /**
     * Adds all blacklisted pesel numbers to the table, tbl_disallowed
     * @param pesels list of blacklisted pesel numbers to be inserted
     */
    @Insert
    void insertAllBlacklisted(List<PeselEntity> pesels);

    /**
     * Adds last updated date to the table, tbl_data_tracker
     * @param dataTracker valid publication date to be inserted, and null otherwise
     */
    @Insert
    void insertLastUpdated(DataTrackerEntity dataTracker);

    /**
     * Deletes all entries of table, tbl_data_tracker
     */
    @Query("DELETE FROM tbl_data_tracker")
    void deleteAllEntries();

    /**
     * Returns the last updated date entry
     * this table always contains one entry
     * @return valid date info about last sync
     */
    @Query("SELECT * FROM tbl_data_tracker LIMIT 1")
    DataTrackerEntity getLastUpdatedDate();

    /**
     * Returns all avaialable candidates
     * @return list of candidates of type {@link CandidateEntity}
     */
    @Query("SELECT * FROM tbl_candidates")
    List<CandidateEntity> fetchAllCandidates();

    /**
     * Finds a user matching the following criteria
     * @param forename valid forename of the user
     * @param surname valid surname of the user
     * @param pesel valid Pesel number of the user
     * @return valid user matching criteria and null otherwise
     */
    @Query("SELECT * FROM  tbl_users WHERE forename = :forename AND surname = :surname AND " +
            "pesel = :pesel")
    UserEntity findUser(String forename, String surname, long pesel);

    /**
     * Inserts a user record to related database table
     * @param user entity to map user information
     */
    @Insert
    void insert(UserEntity user);

    /**
     * Finds a given pesel number is blacklisted or not
     * @param pesel Pesel number to check for blacklisted
     * @return valid instance of type {@link PeselEntity} if disallowed and null otherwise
     */
    @Query("SELECT * FROM tbl_disallowed WHERE pesel = :pesel")
    PeselEntity findIfUserBlacklisted(long pesel);

    /**
     * Inserts all infor related to record a vote registration
     * @param voteMap valid entity to map all details
     */
    @Insert
    void insert(VotingMapEntity voteMap);

    /**
     * Finds whether a user has already voted or not
     * @param pesel Pesel number of the user to be checked
     * @return valid {@link UserEntity} instance if the user has already voted and null otherwise
     */
    @Query("SELECT A.* FROM tbl_users A INNER JOIN tbl_user_votes B ON A.pesel = B.userPesel AND A.pesel = :pesel")
    UserEntity findUserAlreadyVoted(Long pesel);

    /**
     * Finds all available party names
     * @return list of all party names
     */
    @Query("SELECT party FROM tbl_candidates GROUP BY party")
    List<String> findPartyNames();

    /**
     * Finds number of votes earned by each party
     * @param party valid party name
     * @return number of votes they have won
     */
    @Query("SELECT COUNT(*) FROM tbl_user_votes WHERE party = :party")
    Integer findVotesEarnedByParty(String party);

    /**
     * Finds all competing candidates
     * @return list of candidate names
     */
    @Query("SELECT name FROM tbl_candidates GROUP BY name")
    List<String> findCandidateNames();

    /**
     * Finds number of votes earned by each candidate
     * @param candidate valid candidate name
     * @return number of votes won by the give candidate
     */
    @Query("SELECT COUNT(*) FROM tbl_user_votes WHERE candidate = :candidate")
    Integer findVotesEarnedByCandidate(String candidate);
}
