package com.example.tvleague.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;

import com.example.tvleague.R;
import com.example.tvleague.databinding.ActivityMatchDetailBinding;
import com.example.tvleague.databinding.LayoutAddGoalBinding;
import com.example.tvleague.model.GoalAdapter;
import com.example.tvleague.model.Match;
import com.example.tvleague.model.Schedule;

public class MatchDetail extends AppCompatActivity implements NumberPicker.OnValueChangeListener {
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
                layoutAddGoalBinding.btnAddPlayer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TO DO
                        // Xác nhận thêm bàn thắng
                    }
                });
                // Chon phút ghi bàn
                layoutAddGoalBinding.iconTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        show(layoutAddGoalBinding);
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
        // Dữ liệu giả
        binding.tvDate.setText("12/04/222");
        binding.tvTime.setText("19:00");
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {

    }
}