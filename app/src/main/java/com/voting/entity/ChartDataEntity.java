package com.voting.entity;

import com.github.mikephil.charting.data.BarDataSet;

import java.util.List;

/**
 * Entity to include all required data to plot bar graphs
 *
 * Created by Renjith Kandanatt on 03/12/2018.
 */
public class ChartDataEntity {
    private final List<String> labels;
    private final BarDataSet dataSet;

    public ChartDataEntity(List<String> labels, BarDataSet dataSet) {
        this.labels = labels;
        this.dataSet = dataSet;
    }

    public List<String> getLabels() {
        return labels;
    }

    public BarDataSet getDataSet() {
        return dataSet;
    }
}
