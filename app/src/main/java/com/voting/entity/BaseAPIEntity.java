package com.voting.entity;

import com.google.gson.annotations.SerializedName;
import com.voting.utils.Utils;

import java.util.Date;

/**
 * Base entity for both {@link CandidateAPIEntity.CandidateResultEntity} and {@link BlacklistAPIEntity}
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
public class BaseAPIEntity {
    @SerializedName("publicationDate")
    private String publishDate;

    public Date lastUpdated() {
        return Utils.asDate(publishDate);
    }
}
