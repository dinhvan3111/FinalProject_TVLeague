package com.example.tvleague.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tvleague.R;
import com.example.tvleague.databinding.ActivityMatchDetailBinding;
import com.example.tvleague.databinding.LayoutAddGoalBinding;
import com.example.tvleague.model.DatabaseRoute;
import com.example.tvleague.model.Goal;
import com.example.tvleague.model.GoalAdapter;
import com.example.tvleague.model.Match;
import com.example.tvleague.model.Player;
import com.example.tvleague.model.Schedule;

import java.io.Console;
import java.util.ArrayList;
import java.util.Observable;

public class MatchDetail extends AppCompatActivity implements NumberPicker.OnValueChangeListener {
    private ActivityMatchDetailBinding binding;
    private GoalAdapter club1GoalAdapter;
    private GoalAdapter club2GoalAdapter;
    private Schedule schedule;
    private int timeScore;
    private int id_match;//Ma Tran Dau
    ObservableArrayList<String> NameOfPlayers = new ObservableArrayList<>();
    ObservableArrayList<Integer> typeGoal = new ObservableArrayList<>();
    ObservableArrayList<String> NameOfClub = new ObservableArrayList<>();
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update_match_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnu_update_result:
                // Thêm bàn thắng để cập nhật tỉ số
                LayoutAddGoalBinding layoutAddGoalBinding = LayoutAddGoalBinding.inflate(getLayoutInflater());
                Dialog dialog = new Dialog(binding.getRoot().getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(layoutAddGoalBinding.getRoot());
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                //chọn Đội
                ArrayList<String> NameTwoClub = new ArrayList<>();
                NameTwoClub.add(schedule.getClub1().getName());
                NameTwoClub.add(schedule.getClub2().getName());
                ArrayAdapter adapter = new ArrayAdapter(layoutAddGoalBinding.getRoot().getContext(),
                        android.R.layout.simple_list_item_1,NameTwoClub);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                layoutAddGoalBinding.spinnerTeamGoal.setAdapter(adapter);

                final ArrayAdapter[] adapterPlayers = new ArrayAdapter[1];
                layoutAddGoalBinding.spinnerTeamGoal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        NameOfClub.clear();
                        NameOfClub.add(NameTwoClub.get(i));
                        NameOfPlayers = DatabaseRoute.getPlayersByClubName(NameTwoClub.get(i));
                        adapterPlayers[0] =  new ArrayAdapter(layoutAddGoalBinding.getRoot().getContext(),
                                android.R.layout.simple_list_item_1,NameOfPlayers);
                        adapterPlayers[0].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        layoutAddGoalBinding.spinnerPlayerGoal.setAdapter(adapterPlayers[0]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                //chọn cầu thủ
                ObservableArrayList<String> choosePlayer = new ObservableArrayList<>();
                layoutAddGoalBinding.spinnerPlayerGoal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        choosePlayer.clear();
                        choosePlayer.add(NameOfPlayers.get(i));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                layoutAddGoalBinding.btnAddPlayer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String club = NameOfClub.get(0);
                        String player_name = NameOfPlayers.get(0);
                        Player player = DatabaseRoute.getPlayerByName(player_name);
                        int id_club = DatabaseRoute.getIdClubByName(club);
                        int type_goal = 0;
                        if(typeGoal.size() != 0){
                            type_goal = typeGoal.get(0);
                        }
                        Goal goal;
                        if(type_goal!=3){//không phản lưới nhà
                            goal = new Goal(player,timeScore,type_goal,id_club,id_match);
                            DatabaseRoute.addToGoal(goal);
                        }
                        else{//phản lưới nhà
                        }
                    }
                });
                // Chon phút ghi bàn
                layoutAddGoalBinding.iconTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        show(layoutAddGoalBinding);
                    }
                });

                //Chọn loại bàn thắng
                layoutAddGoalBinding.radioTypeGoal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        typeGoal.clear();
                        switch (i){
                            case R.id.normalGoal:
                                typeGoal.add(0);
                                break;
                            case R.id.penaltyKick:
                                typeGoal.add(1);
                                break;
                            case R.id.ownGoal:
                                typeGoal.add(2);
                                break;
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Hàm show dialog TimePicker
    public void show(LayoutAddGoalBinding layoutAddGoalBinding)
    {

        final Dialog d = new Dialog(MatchDetail.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.timer_dialog);
        d.setCancelable(true);
        Window window = d.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(96);
        np.setMinValue(0);
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                timeScore =np.getValue();
                layoutAddGoalBinding.edtTime.setText(String.valueOf(np.getValue()));
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMatchDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        schedule = (Schedule) getIntent().getSerializableExtra("Schedule");
        Match matchResult = schedule.getMatch();
        id_match = matchResult.getId();
        System.out.println("Id match: " + id_match);
        // Nếu trận đấu chưa diên ra
        if(matchResult.getScore() == null){
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
        //tach ngay gio
        String time[] = schedule.getDateTime().split(" ");
        binding.tvDate.setText(time[1]);
        binding.tvTime.setText(time[0]);
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {

    }
}