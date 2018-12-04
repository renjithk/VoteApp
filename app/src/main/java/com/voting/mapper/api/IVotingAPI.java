package com.voting.mapper.api;

import com.voting.entity.BlacklistAPIEntity;
import com.voting.entity.CandidateAPIEntity;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * API interface to be used by Retrofit
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
public interface IVotingAPI {
    @GET("/candidates")
    Call<CandidateAPIEntity> getAllCandidates() throws Exception;

    @GET("/blocked")
    Call<BlacklistAPIEntity> getAllBlacklisted() throws Exception;
}
