package com.voting.localstorage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.voting.entity.CandidateEntity;
import com.voting.entity.DataTrackerEntity;
import com.voting.entity.PeselEntity;
import com.voting.entity.UserEntity;
import com.voting.entity.VotingMapEntity;
import com.voting.localstorage.dao.VotingDAO;

/**
 * Abstract definition for the database
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
@Database(entities = {CandidateEntity.class, UserEntity.class, PeselEntity.class,
                      DataTrackerEntity.class, VotingMapEntity.class}, version = 3, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class VotingDatabase extends RoomDatabase {
    public abstract VotingDAO votingDAO();
}
