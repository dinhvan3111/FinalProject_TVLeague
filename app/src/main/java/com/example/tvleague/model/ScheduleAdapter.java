package com.example.tvleague.model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvleague.databinding.ItemClubRowBinding;
import com.example.tvleague.databinding.ItemScheduleRowBinding;
import com.example.tvleague.view.MainActivity;
import com.example.tvleague.view.MatchDetail;

import java.io.Serializable;
import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private ArrayList<Schedule> schedules;
    private Context context;
    public ScheduleAdapter(Context context,ArrayList<Schedule> schedules) {
        this.schedules = schedules;
        this.context = context;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemScheduleRowBinding binding = ItemScheduleRowBinding.inflate(inflater,parent,false);
        return new ScheduleViewHolder(binding);
    }

    @NonNull


    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = schedules.get(position);
        if(schedule == null){
            return;
        }
        ItemScheduleRowBinding binding = holder.getBinding();
        binding.tvClub1.setText(schedule.getClub1().name);
        binding.tvClub2.setText(schedule.getClub2().name);
        binding.dateTime.setText(schedule.getDateTime());
        binding.itemSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMatchResult(schedule);
            }
        });
    }

    private void goToMatchResult(Schedule schedule) {
        Intent matchDetailIntent = new Intent(context, MatchDetail.class);
        matchDetailIntent.putExtra("Schedule", schedule);
        context.startActivity(matchDetailIntent);
    }


    @Override
    public int getItemCount() {
        if(schedules != null){
            return schedules.size();
        }
        else
            return 0;
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder{
        private ItemScheduleRowBinding binding;

        public ScheduleViewHolder(@NonNull ItemScheduleRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public ItemScheduleRowBinding getBinding(){
            return binding;
        }
    }
}
