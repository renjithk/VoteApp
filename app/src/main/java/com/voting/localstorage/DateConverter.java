package com.voting.localstorage;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Type convertor for date objects
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
public class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
