package com.voting.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Entity to map each vote done by the user
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
@Entity(tableName = "tbl_user_votes",
        foreignKeys = {
            @ForeignKey(entity = UserEntity.class,
                        parentColumns = "pesel",
                        childColumns = "userPesel")
        }, indices = {@Index("userPesel")})
public class VotingMapEntity {
    @NonNull @PrimaryKey(autoGenerate = true)
    private int uniqueId;

    @NonNull
    private String candidate;

    @NonNull
    private String party;

    @Nullable
    private Long userPesel;

    public VotingMapEntity(String candidate, String party, Long userPesel) {
        this.candidate = candidate;
        this.party = party;
        this.userPesel = userPesel;
    }

    @NonNull
    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(@NonNull int uniqueId) {
        this.uniqueId = uniqueId;
    }

    @NonNull
    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(@NonNull String candidate) {
        this.candidate = candidate;
    }

    @NonNull
    public String getParty() {
        return party;
    }

    public void setParty(@NonNull String party) {
        this.party = party;
    }

    @Nullable
    public Long getUserPesel() {
        return userPesel;
    }

    public void setUserPesel(@Nullable Long userPesel) {
        this.userPesel = userPesel;
    }
}
