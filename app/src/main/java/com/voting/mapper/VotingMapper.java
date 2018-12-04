package com.voting.mapper;


import com.voting.VotingApp;
import com.voting.entity.BlacklistAPIEntity;
import com.voting.entity.CandidateAPIEntity;
import com.voting.mapper.api.IVotingAPI;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Provides mapper implementation as defined in {@link IVotingMapper}
 * Created by Renjith Kandanatt on 02/12/2018.
 */
public class VotingMapper implements IVotingMapper {
    private final IVotingAPI votingAPI;

    public VotingMapper(IVotingAPI votingAPI) {
        this.votingAPI = votingAPI;
    }

    @Override
    public CandidateAPIEntity fetchAllCandidates() throws Exception {
        Call<CandidateAPIEntity> call = votingAPI.getAllCandidates();
        Response<CandidateAPIEntity> response =  call.execute();
        return response.body();
    }

    @Override
    public BlacklistAPIEntity fetchAllBlacklisted() throws Exception {
        Call<BlacklistAPIEntity> call = votingAPI.getAllBlacklisted();
        Response<BlacklistAPIEntity> response =  call.execute();
        return response.body();
    }
}
