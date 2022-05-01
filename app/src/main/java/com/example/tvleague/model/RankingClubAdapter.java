package com.example.tvleague.model;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvleague.databinding.ItemClubRankingRowBinding;
import com.example.tvleague.view.MainActivity;

import java.util.ArrayList;

public class RankingClubAdapter extends RecyclerView.Adapter<RankingClubAdapter.RankingClubViewHolder> {
    private ArrayList<RankingClub> rankingClubs;

    public RankingClubAdapter(ArrayList<RankingClub> rankingClubs) {
        this.rankingClubs = rankingClubs;
    }

    public void setRankingClubs(ArrayList<RankingClub> rankingClubs) {
        this.rankingClubs = rankingClubs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RankingClubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemClubRankingRowBinding binding = ItemClubRankingRowBinding.inflate(layoutInflater,parent,false);
        return new RankingClubViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingClubViewHolder holder, int position) {
        RankingClub rankingClub = rankingClubs.get(position);
        if(rankingClub == null)
            return;
        ItemClubRankingRowBinding binding = holder.getBinding();
        binding.tvClubRank.setText(rankingClub.getRank() + "");
        binding.tvClubName.setText(rankingClub.getClub().getName());
        binding.tvWinCount.setText(rankingClub.getWinCount() + "");
        binding.tvDrawCount.setText(rankingClub.getDrawCount() + "");
        binding.tvLoseCount.setText(rankingClub.getLoseCount() + "");
        binding.tvGoalDifference.setText(rankingClub.getGoalDifference() + "");
        binding.tvPoints.setText((rankingClub.getWinCount() * MainActivity.regulations.getWIN_POINT()
                + rankingClub.getDrawCount())*MainActivity.regulations.getDRAW_POINT() +
                 rankingClub.getLoseCount()*MainActivity.regulations.getLOSE_POINT() + "");

    }

    @Override
    public int getItemCount() {
        if(rankingClubs != null)
            return rankingClubs.size();
        else
            return 0;
    }

    public class RankingClubViewHolder extends RecyclerView.ViewHolder{
        ItemClubRankingRowBinding binding;
        public RankingClubViewHolder(@NonNull ItemClubRankingRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemClubRankingRowBinding getBinding() {
            return binding;
        }
    }
}
