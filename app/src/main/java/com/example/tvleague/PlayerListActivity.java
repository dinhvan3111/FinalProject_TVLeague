package com.example.tvleague;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;

import com.example.tvleague.databinding.ActivityAddClubFormBinding;
import com.example.tvleague.databinding.ActivityPlayerListBinding;
import com.example.tvleague.databinding.LayoutInputPlayerInfoBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class PlayerListActivity extends AppCompatActivity {
    public static PlayerAdapter adapter;
    public static ActivityPlayerListBinding binding;
    public static ArrayList<Player> players = new ArrayList<>();
    Club club;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        else{
            club = (Club) bundle.get("clubChose");
            players = DatabaseRoute.getPlayersOfClubById(club.getId());

            adapter = new PlayerAdapter(this,players);
            binding.listPlayer.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
            binding.listPlayer.setAdapter(adapter);

            binding.tvNameClubInPlayerList.setText("Danh sách cầu thủ đội " + club.getName());
            binding.btnAddPlayerInList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inputPlayer(club);
                }
            });
        }
    }
    private void inputPlayer(Club club) {
        LayoutInputPlayerInfoBinding layoutInputPlayerInfoBinding = LayoutInputPlayerInfoBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(binding.getRoot().getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
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
                    type = 0; // Cầu thủ trong nước
                }
                if(layoutInputPlayerInfoBinding.foreignPlayer.isChecked()){
                    type = 1; // Cầu thủ nước ngoài
                }
                String note = layoutInputPlayerInfoBinding.editPlayerNote.getText().toString();
                Player player = new Player(-1,name,doB,type,note);
                players.add(player);
                DatabaseRoute.addPlayerWithIdClub(player,club.getId());//add to database
                adapter.notifyDataSetChanged();
                dialog.cancel();
            }
        });
    }
    public static void editPlayer(Player player, int index) {

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
                players.set(index,edt_player);
                System.out.println(players.get(index).getId());
                DatabaseRoute.updatePlayer(edt_player);
                dialog.cancel();
                adapter.notifyDataSetChanged();
            }
        });
    }
}