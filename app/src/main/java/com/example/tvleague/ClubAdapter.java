package com.example.tvleague;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvleague.databinding.ItemClubRowBinding;
import com.example.tvleague.databinding.ItemPlayerRowBinding;

import java.util.ArrayList;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ClubViewHolder> {
    private ArrayList<Club> listClub;
    private Context context;
    public ClubAdapter(Context context,ArrayList<Club> listClub){
        this.listClub = listClub;
        this.context = context;
    }

    @NonNull
    @Override
    public ClubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemClubRowBinding binding = ItemClubRowBinding.inflate(layoutInflater,parent,false);
        return new ClubViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubAdapter.ClubViewHolder holder, int position) {
        Club club = listClub.get(position);
        if(club == null){
            return;
        }
        ItemClubRowBinding binding = holder.getBinding();
        binding.tvClubName.setText(club.getName());
        binding.tvClubId.setText(club.getId() + "");
        binding.tvStadium.setText(club.getStadium());
        binding.itemClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPlayerList(club);
            }
        });
    }
    private void goToPlayerList(Club club){
        Intent intent = new Intent(context, PlayerListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("clubChose",club);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(listClub != null){
            return listClub.size();
        }
        return 0;
    }

    public class ClubViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvIdClub;
        private TextView tvStadium;
        private ItemClubRowBinding binding;
        private LinearLayout layoutItem;
        public ClubViewHolder(@NonNull ItemClubRowBinding binding) {
            super(binding.getRoot());
            tvName = binding.tvClubName;
            tvIdClub = binding.tvClubId;
            tvStadium = binding.tvStadium;
            layoutItem = binding.itemClub;
            this.binding = binding;
        }

        public  ItemClubRowBinding getBinding(){
            return binding;
        }
    }
}
