package com.example.tvleague.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvleague.databinding.ItemGoalRowBinding;
import com.example.tvleague.databinding.ItemScheduleRowBinding;

import java.util.ArrayList;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {
    private ArrayList<Goal> listGoal;
    private Context context;

    public GoalAdapter(ArrayList<Goal> listGoal, Context context) {
        this.listGoal = listGoal;
        this.context = context;
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemGoalRowBinding binding = ItemGoalRowBinding.inflate(inflater,parent,false);
        return new GoalAdapter.GoalViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        Goal goal = listGoal.get(position);
        ItemGoalRowBinding binding = holder.getBinding();
        binding.tvPlayerName.setText(goal.getPlayer().getName());
        binding.tvGoalTime.setText(goal.getTime());
        if(goal.getType() == 1){ // Penalty
            binding.tvGoalType.setText("(P),");
        }
        else if(goal.getType() == 2){ // Phản lưới nhà
            binding.tvGoalType.setText("(OG), ");
        }
        else{ // bàn thắng thường
            binding.tvGoalType.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(listGoal != null){
            return listGoal.size();
        }
        else
            return 0;
    }

    public class GoalViewHolder extends RecyclerView.ViewHolder{
        private ItemGoalRowBinding binding;
        public GoalViewHolder(@NonNull ItemGoalRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public ItemGoalRowBinding getBinding(){
            return binding;
        }
    }
}
