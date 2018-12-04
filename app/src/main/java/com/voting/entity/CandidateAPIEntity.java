package com.voting.entity;

import com.google.gson.annotations.SerializedName;
import com.voting.utils.Utils;

import java.util.Date;
import java.util.List;

/**
 * API entity to map list candidates response from the server
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
public class CandidateAPIEntity {
    @SerializedName("candidates")
    CandidateResultEntity candidatesResult;

    public List<CandidateEntity> candidates() {
        return candidatesResult.candidates;
    }

    public Date lastUpdated() {
        return candidatesResult.lastUpdated();
    }

    private class CandidateResultEntity extends BaseAPIEntity {
        @SerializedName("candidate")
        private List<CandidateEntity> candidates;
    }
}
