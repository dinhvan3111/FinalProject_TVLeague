package com.example.tvleague.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvleague.databinding.ItemListClubRowBinding;
import com.example.tvleague.view.PlayerListActivity;
import com.example.tvleague.databinding.ItemClubRowBinding;

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

//    @Override
//    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
//    }

    @Override
    public void onBindViewHolder(@NonNull ClubAdapter.ClubViewHolder holder, int position) {
        Club club = listClub.get(position);
        if(club == null){
            return;
        }
        ItemClubRowBinding binding = holder.getBinding();
        binding.tvClubName.setText(club.getName());
        binding.tvClubMemNum.setText(club.getListPlayers().size() + "");
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
            tvIdClub = binding.tvClubMemNum;
            tvStadium = binding.tvStadium;
            layoutItem = binding.itemClub;
            this.binding = binding;
        }

        public  ItemClubRowBinding getBinding(){
            return binding;
        }
    }
//    public class ClubRowViewHolder2 extends RecyclerView.ViewHolder{
//        private ItemListClubRowBinding binding;
//        public ClubRowViewHolder2(@NonNull ItemListClubRowBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//    }
}
