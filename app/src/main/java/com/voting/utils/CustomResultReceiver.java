package com.voting.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import java.lang.ref.WeakReference;

/**
 * Custom result receiver to fed data from background service to UI
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
public class CustomResultReceiver extends ResultReceiver {
    private WeakReference<IReceiverCallback> mReceiver;

    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    public CustomResultReceiver(Handler handler) {
        super(handler);
    }

    public void setCallback(IReceiverCallback receiver) {
        mReceiver = new WeakReference<>(receiver);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.get().onReceiveResult(resultCode, resultData);
        }
    }

    public interface IReceiverCallback {
        void onReceiveResult(int resultCode, Bundle resultData);
    }
}
