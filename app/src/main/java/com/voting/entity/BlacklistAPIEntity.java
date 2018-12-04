package com.voting.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Entity to map response for blacklisted voters
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
public class BlacklistAPIEntity {
    @SerializedName("disallowed")
    private DisallowedResultEntity disallowedResult;

    public List<PeselEntity> blacklistedList() {
        return disallowedResult.disallowedPesels;
    }

    private class DisallowedResultEntity extends BaseAPIEntity {
        @SerializedName("person")
        private List<PeselEntity> disallowedPesels;
    }
}
