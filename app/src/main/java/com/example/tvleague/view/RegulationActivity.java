package com.example.tvleague.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.tvleague.R;
import com.example.tvleague.databinding.ActivityMatchDetailBinding;
import com.example.tvleague.databinding.ActivityRegulationBinding;
import com.example.tvleague.databinding.FragmentRegulationBinding;
import com.example.tvleague.model.DatabaseRoute;
import com.example.tvleague.model.GoalAdapter;
import com.example.tvleague.model.Regulation;
import com.example.tvleague.model.RegulationAdapter;
import com.example.tvleague.model.Schedule;

import java.util.ArrayList;

public class RegulationActivity extends AppCompatActivity {
    private ActivityRegulationBinding binding;
    private ArrayList<Regulation> regulationList = new ArrayList<>();
    private RegulationAdapter regulationAdapter;
    private boolean isStarted = DatabaseRoute.IsStartLeague();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegulationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        regulationList= DatabaseRoute.getRegulations();
        regulationAdapter = new RegulationAdapter(regulationList);
        binding.rvLeagueRegulation.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        binding.rvLeagueRegulation.setAdapter(regulationAdapter);
        binding.rvLeagueRegulation.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));

    }
}