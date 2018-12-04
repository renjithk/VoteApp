package com.voting.ui.background;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;

import com.voting.VotingApp;
import com.voting.entity.UserEntity;
import com.voting.service.IVotingService;
import com.voting.utils.AppConstants;
import com.voting.utils.CustomResultReceiver;
import com.voting.utils.StatusOptions;
import com.voting.utils.Utils;

import javax.inject.Inject;

import dagger.Lazy;
import okhttp3.internal.Util;

/**
 * Responsible for all data fecth relation action in the background
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
public class DataFetchService extends JobIntentService {
    @Inject Lazy<IVotingService> votingService;

    private static final int JOB_ID = 1000;
    private static final String KEY_RECEIVER = "key_result_receiver";

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        VotingApp.get(context).getComponent().inject(this);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        ResultReceiver resultReceiver = intent.getParcelableExtra(KEY_RECEIVER);
        UserEntity user = intent.getParcelableExtra(AppConstants.KEY_LOGIN_USER);

        if(null == user) {
            if(votingService.get().isDataEmptyOrOutdated()) {
                //check if there is internet before proceeding
                if(Utils.isNetworkUp(getBaseContext())) votingService.get().syncAllData();
                else onFinished(resultReceiver, StatusOptions.FAILED_NO_INTERNET);
            } else {
                //adding extra wait to prevent frequent flashing of dialog while opening and closing
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            onFinished(resultReceiver, StatusOptions.SYNC_OK);
        } else {
            int statusCode = votingService.get().proceedWithSignIn(user);
            onFinished(resultReceiver, statusCode, user);
        }
    }

    public static void enqueueWork(Context context, CustomResultReceiver resultReceiver) {
        enqueueWork(context, resultReceiver, null);
    }

    public static void enqueueWork(Context context, CustomResultReceiver resultReceiver,
                                   UserEntity user) {
        Intent intent = new Intent(context, DataFetchService.class);
        intent.putExtra(KEY_RECEIVER, resultReceiver);
        intent.putExtra(AppConstants.KEY_LOGIN_USER, user);
        enqueueWork(context, DataFetchService.class, JOB_ID, intent);
    }

    private void onFinished(ResultReceiver resultReceiver, @StatusOptions int resultCode) {
        resultReceiver.send(resultCode, null);
    }

    private void onFinished(ResultReceiver resultReceiver, @StatusOptions int resultCode,
                            UserEntity user) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstants.KEY_LOGIN_USER, user);
        resultReceiver.send(resultCode, bundle);
    }
}
