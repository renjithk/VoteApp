package com.voting.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Entity to map individual candidate
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
@Entity(tableName = "tbl_candidates")
public class CandidateEntity {
    @NonNull @PrimaryKey(autoGenerate = true)
    private int uniqueId;

    @NonNull @SerializedName("name")
    private String name;

    @NonNull @SerializedName("party")
    private String party;

    @NonNull
    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(@NonNull int uniqueId) {
        this.uniqueId = uniqueId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getParty() {
        return party;
    }

    public void setParty(@NonNull String party) {
        this.party = party;
    }
}
