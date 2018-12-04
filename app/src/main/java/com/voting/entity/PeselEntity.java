package com.voting.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Entity to map individual blacklisted pesel number
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
@Entity(tableName = "tbl_disallowed")
public class PeselEntity {
    @PrimaryKey
    @NonNull @SerializedName("pesel")
    private Long pesel;

    @NonNull
    public Long getPesel() {
        return pesel;
    }

    public void setPesel(@NonNull Long pesel) {
        this.pesel = pesel;
    }
}
