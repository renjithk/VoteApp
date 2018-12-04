package com.voting.di;

import android.content.Context;

import com.google.gson.Gson;
import com.voting.VotingApp;
import com.voting.localstorage.VotingDatabase;
import com.voting.service.IVotingService;
import com.voting.ui.CastVoteActivity;
import com.voting.ui.ElectionTrendActivity;
import com.voting.ui.background.DataFetchService;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Dagger dependency component definitions
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(DataFetchService dataFetchService);
    void inject(CastVoteActivity castVoteActivity);
    void inject(ElectionTrendActivity electionTrendActivity);

    Gson gson();
    OkHttpClient okHttpClient();
    Retrofit retrofit();
    IVotingService service();
    VotingDatabase votingDB();
}
