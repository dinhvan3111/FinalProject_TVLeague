package com.example.tvleague.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.tvleague.R;
import com.example.tvleague.databinding.ActivityMatchDetailBinding;
import com.example.tvleague.model.GoalAdapter;
import com.example.tvleague.model.Match;
import com.example.tvleague.model.Schedule;

public class MatchDetail extends AppCompatActivity {
    private ActivityMatchDetailBinding binding;
    private GoalAdapter club1GoalAdapter;
    private GoalAdapter club2GoalAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update_match_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnu_update_score:
                // TO DO
                break;
            case R.id.mnu_update_goals_club1:
                // TO DO
                break;
            case R.id.mnu_update_goals_club2:
                // TO DO
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMatchDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Schedule schedule = (Schedule) getIntent().getSerializableExtra("Schedule");
        Match matchResult = schedule.getMatch();
        // Nếu trận đấu chưa diên ra
        if(matchResult == null){
            binding.tvScore.setText("? - ?");
            binding.tvState.setText("Chưa diễn ra");
        }
        // Nếu trận đấu đã kết thúc
        else{
            binding.tvScore.setText(matchResult.getScore());
            binding.rvListGoalClub1.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
            binding.rvListGoalClub2.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
            club1GoalAdapter = new GoalAdapter(matchResult.getListGoalClub1(),getApplicationContext());
            club2GoalAdapter = new GoalAdapter(matchResult.getListGoalClub2(),getApplicationContext());
            binding.tvState.setText("Kết thúc");
        }
        binding.tvClub1Name.setText(schedule.getClub1().getName());
        binding.tvClub2Name.setText(schedule.getClub2().getName());
        binding.tvStadium.setText(schedule.getStadium());
        binding.tvDate.setText("12/04/222");
        binding.tvTime.setText("19:00");

    }
}