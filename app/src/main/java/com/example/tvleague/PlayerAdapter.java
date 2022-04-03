package com.example.tvleague;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvleague.databinding.ItemPlayerRowBinding;

import java.util.ArrayList;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {
    private ArrayList<Player> listPlayer;

    public PlayerAdapter(ArrayList<Player> listPlayer){
        this.listPlayer = listPlayer;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemPlayerRowBinding binding = ItemPlayerRowBinding.inflate(layoutInflater,parent,false);
        return new PlayerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = listPlayer.get(position);
        if(player == null){
            return;
        }
        ItemPlayerRowBinding binding = holder.getBinding();
        binding.tvName.setText(player.getName());
        binding.tvDoB.setText(player.getDoB());
        if(player.getType() == 0){
            binding.tvType.setText("CT nội");
        }
        else{
            binding.tvType.setText("CT ngoại");
        }
        binding.tvNote.setText(player.getNote());
    }

    @Override
    public int getItemCount() {
        if(listPlayer != null){
            return listPlayer.size();
        }
        return 0;
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvDoB;
        private TextView tvType;
        private TextView tvNote;
        private ItemPlayerRowBinding binding;

        public PlayerViewHolder(@NonNull ItemPlayerRowBinding binding) {
            super(binding.getRoot());
            tvName = binding.tvName;
            tvDoB = binding.tvDoB;
            tvType = binding.tvType;
            tvNote = binding.tvNote;
            this.binding = binding;
        }

        public  ItemPlayerRowBinding getBinding(){
            return binding;
        }
    }
}
