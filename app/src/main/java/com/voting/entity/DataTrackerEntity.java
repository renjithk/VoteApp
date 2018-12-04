package com.voting.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

/**
 * Entity to track when the data was last updated
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
@Entity(tableName = "tbl_data_tracker")
public class DataTrackerEntity {
    @NonNull @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "unique_id")
    private int uniqueId;

    @Nullable
    @ColumnInfo(name = "last_updated")
    private Date lastUpdated;

    @NonNull
    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(@NonNull int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public DataTrackerEntity(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Nullable
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(@Nullable Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
