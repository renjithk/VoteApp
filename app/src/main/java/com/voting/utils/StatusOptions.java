package com.voting.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Provides available sync options
 * Created by Renjith Kandanatt on 02/12/2018.
 */
@IntDef({StatusOptions.SYNC_OK, StatusOptions.FAILED_NO_INTERNET, StatusOptions.FAILED_OTHER,
         StatusOptions.LOGIN_OK, StatusOptions.BLACKLISTED, StatusOptions.REGISTER_VOTE_OK,
         StatusOptions.LOGIN_NEW_USER, StatusOptions.LOGIN_FAIL_BLACKLISTED,
         StatusOptions.LOGIN_ALREADY_VOTED})
@Retention(RetentionPolicy.SOURCE)
public @interface StatusOptions {
    int SYNC_OK = 6;
    int FAILED_NO_INTERNET = 4;
    int FAILED_OTHER = 3;
    int LOGIN_OK = 7;
    int BLACKLISTED = 8;
    int REGISTER_VOTE_OK = 9;
    int LOGIN_NEW_USER = 10;
    int LOGIN_FAIL_BLACKLISTED = 11;
    int LOGIN_ALREADY_VOTED = 12;
}
