package com.example.tvleague.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.tvleague.R;
import com.example.tvleague.databinding.ActivityLeagueManagementBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class LeagueManagementActivity extends AppCompatActivity {
    private ProgressBar pgBar;
    private ActivityLeagueManagementBinding binding;
    private ScheduleFragment scheduleFragment;
    private RankingFragment rankingFragment;
    private ListClubFragment listClubFragment;
    private int currentBtn;
    public static BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeagueManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);
        addControls();
        addEvents();
        replaceFragment(scheduleFragment,R.id.btnSchedule);
        pgBar.setVisibility(View.GONE);
    }

    private void replaceFragment(Fragment fragment, int currentButtonNav) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.linearLayout,fragment);
        fragmentTransaction.commit();
        currentBtn = currentButtonNav;
        switch (currentButtonNav){
            case R.id.btnSchedule:
                setTitle("Lịch thi đấu");
                break;
            case R.id.btnRanking:
                setTitle("Bảng xếp hạng");
                break;
            case R.id.btnListClub:
                setTitle("Danh sách đội bóng");
        }
    }

    private void addEvents() {
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() != currentBtn){
                    switch (item.getItemId()){
                        case R.id.btnSchedule:
                            replaceFragment(scheduleFragment,R.id.btnSchedule);
                            break;
                        case R.id.btnRanking:
                            replaceFragment(rankingFragment,R.id.btnRanking);
                            break;
                        case R.id.btnListClub:
                            replaceFragment(listClubFragment,R.id.btnListClub);
                    }
                }
                return true;
            }
        });
    }

    private void addControls(){
        pgBar = findViewById(R.id.pgBar);
        scheduleFragment = new ScheduleFragment();
        rankingFragment = new RankingFragment();
        listClubFragment = new ListClubFragment();
    }
}