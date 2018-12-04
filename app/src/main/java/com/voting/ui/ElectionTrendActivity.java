package com.voting.ui;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.voting.R;
import com.voting.VotingApp;
import com.voting.entity.ChartDataEntity;
import com.voting.service.IVotingService;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.Lazy;

/**
 * This class is responsible for showing different election trends
 *
 * Created by Renjith Kandanatt on 03/12/2018.
 */
public class ElectionTrendActivity extends BaseActivity {
    @Inject Lazy<IVotingService> votingService;

    @Bind(R.id.toolbar_text) TextView toolbarText;
    @Bind(R.id.election_trend_by_party) BarChart trendByParty;
    @Bind(R.id.election_trend_by_candidate) BarChart trendByCandidate;

    private static final int VOTES_PER_PARTY = 1;
    private static final int VOTES_PER_CANDIDATE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_election_trend);
        VotingApp.get(this).getComponent().inject(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        addNavIconListener();
        toolbarText.setText(R.string.election_trend_title);

        new ElectionTrendTask(VOTES_PER_PARTY).execute();
        new ElectionTrendTask(VOTES_PER_CANDIDATE).execute();
    }

    private class ElectionTrendTask extends AsyncTask<Void, Void, ChartDataEntity> {
        private final int type;

        ElectionTrendTask(int type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            BarChart chart = VOTES_PER_PARTY == type ? trendByParty : trendByCandidate;
            chart.getDescription().setText("");
            chart.setDrawMarkers(false);
            chart.getAxisLeft().setAxisMinimum(0);
            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            chart.animateY(1000);
            chart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            chart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);

            chart.getXAxis().setGranularityEnabled(true);
            chart.getXAxis().setGranularity(1.0f);

            chart.getXAxis().setXOffset(10f);
            chart.getXAxis().setYOffset(10f);
            chart.getXAxis().setLabelRotationAngle(VOTES_PER_PARTY == type ? 0 : 45f);
            chart.getXAxis().setDrawAxisLine(false);
            chart.setDrawValueAboveBar(false);

            chart.getAxisRight().setDrawAxisLine(true);
            chart.getAxisRight().setDrawGridLines(false);
            chart.getAxisRight().setEnabled(false);
        }

        @Override
        protected ChartDataEntity doInBackground(Void... voids) {
            return VOTES_PER_PARTY == type ? votingService.get().votesPerParty() :
                                             votingService.get().votesPerCandidate();
        }

        @Override
        protected void onPostExecute(ChartDataEntity result) {
            super.onPostExecute(result);
            BarChart chart = VOTES_PER_PARTY == type ? trendByParty : trendByCandidate;
            result.getDataSet().setAxisDependency(YAxis.AxisDependency.LEFT);
            result.getDataSet().setColor(Color.YELLOW);
            result.getDataSet().setHighlightEnabled(true);
            result.getDataSet().setHighLightColor(Color.RED);
            result.getDataSet().setValueTextSize(12);
            result.getDataSet().setValueTextColor(Color.BLUE);

            chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(result.getLabels()));
            chart.getXAxis().setLabelCount(result.getDataSet().getEntryCount());
            chart.setData(new BarData(result.getDataSet()));
        }
    }
}
