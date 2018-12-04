package com.voting.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.service.voice.VoiceInteractionService;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.voting.R;
import com.voting.VotingApp;
import com.voting.entity.CandidateEntity;
import com.voting.entity.UserEntity;
import com.voting.service.IVotingService;
import com.voting.ui.adapter.CandidateAdapter;
import com.voting.ui.custom.MessageBox;
import com.voting.ui.custom.ProgressLoader;
import com.voting.utils.AppConstants;
import com.voting.utils.StatusOptions;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.Lazy;

/**
 * This class is responsible for showing the list of candidates and allowing user to cast their votes
 *
 * Created by Renjith Kandanatt on 03/12/2018.
 */
public class CastVoteActivity extends BaseActivity {
    @Inject Lazy<IVotingService> votingService;
    @Inject Lazy<CandidateAdapter> adapter;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.toolbar_text) TextView toolbarText;
    @Bind(R.id.list_candidate) RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_cast_vote);
        VotingApp.get(this).getComponent().inject(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        addNavIconListener();
        toolbarText.setText(R.string.cast_vote_title);

        new CandidateLoadTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cast_vote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_vote:
                //check if at least one item is selected
                if(null == adapter.get().chosenCandidate()) {
                    showMessageBox(getString(R.string.warning_no_candidate_chosen_title),
                                    getString(R.string.warning_no_candidate_chosen_message));
                } else new RegisterVoteTask().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class CandidateLoadTask extends AsyncTask<Void, Void, List<CandidateEntity>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //initialise recycler
            recyclerView.setLayoutManager(new LinearLayoutManager(CastVoteActivity.this));
            recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(CastVoteActivity.this,
                                                                            R.anim.layout_drop_down));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        @Override
        protected List<CandidateEntity> doInBackground(Void... voids) {
            return votingService.get().getAllCandidates();
        }

        @Override
        protected void onPostExecute(List<CandidateEntity> candidates) {
            super.onPostExecute(candidates);
            adapter.get().setData(candidates);
            recyclerView.setAdapter(adapter.get());
        }
    }

    private class RegisterVoteTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //display a loading message while background task is running
            showProgressLoader(getString(R.string.loader_register_vote));
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            UserEntity loggedInUser = getIntent().getExtras().getParcelable(AppConstants.KEY_LOGIN_USER);
            CandidateEntity chosenCandidate = adapter.get().chosenCandidate();
            int status = votingService.get().registerVote(loggedInUser, chosenCandidate);

            //keep the dialog for a while to prevent it from flashing
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(Integer status) {
            super.onPostExecute(status);
            setResult(status);
            finish();
        }
    }
}
