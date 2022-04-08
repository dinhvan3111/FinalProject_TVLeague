package com.example.tvleague.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.tvleague.databinding.ActivityClubRegistedBinding;
import com.example.tvleague.model.Club;
import com.example.tvleague.model.ClubAdapter;
import com.example.tvleague.model.DatabaseRoute;

import java.util.ArrayList;

public class ClubRegistedActivity extends AppCompatActivity {
    private ArrayList<Club> listAddedClub = new ArrayList<>();
    private ClubAdapter adapter;
    private ActivityClubRegistedBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClubRegistedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listAddedClub = DatabaseRoute.getAllClub();
        adapter = new ClubAdapter(this,listAddedClub);
        binding.listAddedClub.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        binding.listAddedClub.setAdapter(adapter);
    }
}