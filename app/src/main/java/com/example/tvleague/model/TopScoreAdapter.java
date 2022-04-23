package com.example.tvleague.model;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvleague.databinding.ItemClubRankingRowBinding;
import com.example.tvleague.databinding.ItemTopScoreRankingRowBinding;

import java.util.ArrayList;

public class TopScoreAdapter extends RecyclerView.Adapter<TopScoreAdapter.TopScoreViewHolder> {
    private ArrayList<TopScore> topScores;

    public TopScoreAdapter(ArrayList<TopScore> topScores) {
        this.topScores = topScores;
    }
    public void setTopScore(ArrayList<TopScore> topScores) {
        this.topScores = topScores;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TopScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTopScoreRankingRowBinding binding = ItemTopScoreRankingRowBinding.inflate(layoutInflater,parent,false);
        return new TopScoreViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TopScoreViewHolder holder, int position) {
        TopScore player = topScores.get(position);
        if(player == null)
            return;
        ItemTopScoreRankingRowBinding binding = holder.getBinding();
        int index = position + 1;
        binding.tvSttTopScore.setText(player.getIndex() + "");
        binding.tvNamePlayerTopScore.setText(player.getPlayer().getName());
        binding.tvClubNameTopScore.setText(DatabaseRoute.getNameClubByIdPlayer(player.getPlayer().getId()));
        binding.tvTypePlayerTopScore.setText(DatabaseRoute.getTypeById(player.getPlayer().getType()));
        binding.tvNumScore.setText(player.getQuantity_score() + "");
    }

    @Override
    public int getItemCount() {
        return topScores.size();
    }
    public class TopScoreViewHolder extends RecyclerView.ViewHolder{
        ItemTopScoreRankingRowBinding binding;
        public TopScoreViewHolder(@NonNull ItemTopScoreRankingRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemTopScoreRankingRowBinding getBinding() {
            return binding;
        }
    }
}
