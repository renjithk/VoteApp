package com.voting.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This class is responsible for providing all utility operations
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
public class Utils {
    /**
     * Converts date string to a date object of type {@link Date}
     * @param dateString valid date string
     * @return formatted date object or null otherwise
     */
    public static Date asDate(String dateString) {
        try {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            return dateFormatter.parse(dateString);
        } catch(Exception e) {
            return null;
        }
    }

    /**
     * Checks whether a list is empty or not
     * @param list list of any type
     * @return true if the list is empty and false otherwise
     */
    public static boolean isEmptyList(List<?> list) {
        return null == list || list.isEmpty();
    }

    /**
     * Checks whether the data is old enough to sync again
     * @param lastUpdated last updated date from the server
     * @return true if the data is outdated and false otherwise
     */
    public static boolean isDataOutdated(Date lastUpdated) {
        if(null == lastUpdated) return true;

        Calendar today = Calendar.getInstance();
        today.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar lastUpdatedCal = Calendar.getInstance();
        lastUpdatedCal.setTimeZone(TimeZone.getTimeZone("UTC"));
        lastUpdatedCal.setTime(lastUpdated);
        lastUpdatedCal.add(Calendar.DATE, 1);

        return lastUpdatedCal.before(today);
    }

    /**
     * Returns current state of network connectivity
     * @param context a valid application context
     * @return true if there is internet connection and false otherwise
     */
    public static boolean isNetworkUp(Context context) {
        final ConnectivityManager connectionHandler =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectionHandler.getActiveNetworkInfo();
        //notify user you are online
        //notify user you are not online
        return networkInfo != null && networkInfo.isConnected();
    }
}
