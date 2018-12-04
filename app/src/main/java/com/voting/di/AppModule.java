package com.voting.di;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.voting.R;
import com.voting.VotingApp;
import com.voting.localstorage.VotingDatabase;
import com.voting.mapper.IVotingMapper;
import com.voting.mapper.VotingMapper;
import com.voting.mapper.api.IVotingAPI;
import com.voting.service.IVotingService;
import com.voting.service.VotingService;

import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Indicates binding for components defined in the DaggerComponent
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
@Module
public class AppModule {
    private final VotingApp appContext;

    public AppModule(VotingApp appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return appContext;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();

        //create gson object from the builder
        return builder.create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient, Context context) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl(context.getString(R.string.api_base_url))
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        //retrofit network logs, it is turned on by default
        //in reality, this can be restricted only for dev builds
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpBuilder.addInterceptor(interceptor);

        //set timeout to 30 seconds
        okHttpBuilder.readTimeout(30, TimeUnit.SECONDS);
        okHttpBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(30, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(30, TimeUnit.SECONDS);
        okHttpBuilder.retryOnConnectionFailure(true);
        return okHttpBuilder.build();
    }

    @Provides
    @Singleton
    IVotingService provideVotingService(IVotingMapper mapper, VotingDatabase database) {
        return new VotingService(mapper, database);
    }

    @Provides
    @Singleton
    IVotingMapper provideVotingMapper(IVotingAPI votingAPI) {
        return new VotingMapper(votingAPI);
    }

    @Provides
    @Singleton
    IVotingAPI provideAPI(Retrofit retrofit) {
        return retrofit.create(IVotingAPI.class);
    }

    @Provides
    @Singleton
    VotingDatabase provideVotingDB() {
        return Room.databaseBuilder(appContext, VotingDatabase.class,
                                    appContext.getString(R.string.db_name)).fallbackToDestructiveMigration().build();
    }
}
