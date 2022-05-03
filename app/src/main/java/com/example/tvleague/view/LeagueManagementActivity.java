package com.example.tvleague.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import com.example.tvleague.R;
import com.example.tvleague.databinding.ActivityLeagueManagementBinding;
import com.example.tvleague.databinding.LayoutInputPlayerInfoBinding;
import com.example.tvleague.databinding.LayoutSearchPlayerBinding;
import com.example.tvleague.model.DatabaseRoute;
import com.example.tvleague.model.Player;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;

public class LeagueManagementActivity extends AppCompatActivity {
    private ProgressBar pgBar;
    private ActivityLeagueManagementBinding binding;
    private ScheduleFragment scheduleFragment;
    private RankingFragment rankingFragment;
    private ListClubFragment listClubFragment;
    private RegulationFragment regulationFragment;
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnu_search:
                LayoutSearchPlayerBinding layoutSearchPlayerBinding = LayoutSearchPlayerBinding.inflate(getLayoutInflater());
                Dialog searchDialog = new Dialog(binding.getRoot().getContext());
                searchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                searchDialog.setCancelable(true);
                searchDialog.setContentView(layoutSearchPlayerBinding.getRoot());
                searchDialog.show();
                Window window = searchDialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                layoutSearchPlayerBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String searchString = layoutSearchPlayerBinding.searchField.getText().toString();
                        if(isNumeric(searchString)){
                            Player player = DatabaseRoute.findPlayerById(Integer.parseInt(searchString));
                            if(player != null)
                                editPlayer(player);
                            else{
                                layoutSearchPlayerBinding.searchField.setError("Không tìm thấy cầu thủ nào");
                            }
                        }
                        else{
                            Player player = DatabaseRoute.findPlayerByName(searchString);
                            if(player != null)
                                editPlayer(player);
                            else{
                                layoutSearchPlayerBinding.searchField.setError("Không tìm thấy cầu thủ nào");
                            }
                        }
                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
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
                break;
//            case R.id.btnRegulation:
//                setTitle("Danh sách đội bóng");
//                break;
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
                            break;
//                        case R.id.btnRegulation:
//                            replaceFragment(regulationFragment,R.id.btnRegulation);
//                            break;
                    }
                }
                return true;
            }
        });
    }

    public void editPlayer(Player player) {

        LayoutInputPlayerInfoBinding layoutInputPlayerInfoBinding = LayoutInputPlayerInfoBinding.inflate(LayoutInflater.from(binding.getRoot().getContext()));
        Dialog dialog = new Dialog(binding.getRoot().getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        layoutInputPlayerInfoBinding.btnAddPlayer.setText("Thay đổi");
        layoutInputPlayerInfoBinding.editPlayerName.setText(player.getName());
        layoutInputPlayerInfoBinding.editPlayerDoB.setText(player.getDoB());
        if(player.getType() == 0){
            layoutInputPlayerInfoBinding.nativePlayer.setChecked(true);
            layoutInputPlayerInfoBinding.foreignPlayer.setChecked(false);
        }
        else{
            layoutInputPlayerInfoBinding.foreignPlayer.setChecked(true);
            layoutInputPlayerInfoBinding.nativePlayer.setChecked(false);
        }
        layoutInputPlayerInfoBinding.editPlayerNote.setText(player.getNote());
        layoutInputPlayerInfoBinding.btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(binding.getRoot().getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        layoutInputPlayerInfoBinding.editPlayerDoB.setText(day + "/" + (month + 1) + "/" + year);
                    }
                },mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });
        dialog.setContentView(layoutInputPlayerInfoBinding.getRoot());
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutInputPlayerInfoBinding.btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = layoutInputPlayerInfoBinding.editPlayerName.getText().toString();
                if(name.equals("")){
                    layoutInputPlayerInfoBinding.editPlayerName.setError("This field can not be blank");
                    return;
                }
                int type = 0;
                String doB = layoutInputPlayerInfoBinding.editPlayerDoB.getText().toString();
                if(name.equals("")){
                    layoutInputPlayerInfoBinding.editPlayerDoB.setError("This field can not be blank");
                    return;
                }
                if(layoutInputPlayerInfoBinding.nativePlayer.isChecked()){
                    type = 0; // Cầu thủ trong nước64
                }
                if(layoutInputPlayerInfoBinding.foreignPlayer.isChecked()){
                    type = 1; // Cầu thủ nước ngoài
                }
                String note = layoutInputPlayerInfoBinding.editPlayerNote.getText().toString();
                Player edt_player = new Player(player.getId(),name,doB,type,note);
                DatabaseRoute.updatePlayer(edt_player);
                dialog.cancel();
            }
        });
    }

    private void addControls(){
        pgBar = findViewById(R.id.pgBar);
        scheduleFragment = new ScheduleFragment();
        rankingFragment = new RankingFragment();
        listClubFragment = new ListClubFragment();
        regulationFragment = new RegulationFragment();
    }
}