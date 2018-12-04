package com.voting;

import android.app.Application;
import android.content.Context;

import com.voting.di.AppComponent;
import com.voting.di.AppModule;
import com.voting.di.DaggerAppComponent;

/**
 * Application class for the voting application
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
public class VotingApp extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static VotingApp get(Context context) {
        return (VotingApp) context.getApplicationContext();
    }

    public AppComponent getComponent() {
        if(null == appComponent) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
        return appComponent;
    }
}
