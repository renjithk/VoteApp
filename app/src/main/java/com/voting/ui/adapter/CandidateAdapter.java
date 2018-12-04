package com.voting.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.voting.R;
import com.voting.entity.CandidateEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Adapter class responsible for rendering list of candidates
 *
 * Created by Renjith Kandanatt on 03/12/2018.
 */
public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.ViewHolder> {
    private List<CandidateEntity> dataList;
    private int selectedPosition = RecyclerView.NO_POSITION;

    @Inject
    CandidateAdapter() {
        dataList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_candidate_row,
                                                                     parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setData(List<CandidateEntity> candidates) {
        this.dataList.clear();
        if(null != candidates) this.dataList.addAll(candidates);
    }

    public CandidateEntity chosenCandidate() {
        if(RecyclerView.NO_POSITION == selectedPosition) return null;
        try {
            return dataList.get(selectedPosition);
        } catch (IndexOutOfBoundsException iobex) {
            return null;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.candidate_name) TextView candidateName;
        @Bind(R.id.party) TextView party;
        @Bind(R.id.icon_tick) AppCompatImageView tickIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.layout_parent)
        public void onCandidateClicked() {
            notifyItemChanged(selectedPosition);
            selectedPosition = getAdapterPosition();
            notifyItemChanged(selectedPosition);
        }

        public void bind(CandidateEntity entity, int position) {
            candidateName.setText(entity.getName());
            party.setText(entity.getParty());
            tickIcon.setVisibility(position == selectedPosition ? View.VISIBLE : View.GONE);
        }
    }
}
