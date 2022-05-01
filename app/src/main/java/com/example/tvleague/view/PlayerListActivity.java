package com.example.tvleague.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.tvleague.databinding.ActivityPlayerListBinding;
import com.example.tvleague.databinding.LayoutInputPlayerInfoBinding;
import com.example.tvleague.model.Club;
import com.example.tvleague.model.DatabaseRoute;
import com.example.tvleague.model.LeagueRegulations;
import com.example.tvleague.model.Player;
import com.example.tvleague.model.PlayerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;

public class PlayerListActivity extends AppCompatActivity {
    public static PlayerAdapter adapter;
    public static ActivityPlayerListBinding binding;
    public static ObservableArrayList<Player> players = new ObservableArrayList<>();
    private LeagueRegulations regulations = new LeagueRegulations();
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
            players =  DatabaseRoute.getPlayersOfClubById(club.getId());

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
        ObservableArrayList<String> dob = new ObservableArrayList<>();
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
                        dob.clear();
                        dob.add(day + "/" + (month + 1) + "/" + year);
                    }
                },mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });
        dialog.setContentView(layoutInputPlayerInfoBinding.getRoot());

        if(players.size() >= regulations.getMAX_PLAYERS()){
            Toast.makeText(PlayerListActivity.this, "Exceeded "
                    + regulations.getMAX_PLAYERS() + " players", Toast.LENGTH_SHORT).show();
        }
        else{

            dialog.show();
        }
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
                int age = getAge(dob.get(0));
                System.out.println(age + "");
                if(age < regulations.getMIN_AGE() || age > regulations.getMAX_AGE()){
                    Toast.makeText(PlayerListActivity.this, "Just from "
                            + regulations.getMIN_AGE() + " to " +
                            regulations.getMAX_AGE() + " years old!", Toast.LENGTH_SHORT).show();
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
                if(type == 1 && countForeignPlayer(players) >= regulations.getMAX_FOREIGN_PLAYERS()){
                    Toast.makeText(PlayerListActivity.this, "Exceeded foreign Players", Toast.LENGTH_SHORT).show();
                    return;
                }
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
        if(player.getType() == 1){
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
                int type = 1;
                String doB = layoutInputPlayerInfoBinding.editPlayerDoB.getText().toString();
                if(name.equals("")){
                    layoutInputPlayerInfoBinding.editPlayerDoB.setError("This field can not be blank");
                    return;
                }
                if(layoutInputPlayerInfoBinding.nativePlayer.isChecked()){
                    type = 1; // Cầu thủ trong nước64
                }
                if(layoutInputPlayerInfoBinding.foreignPlayer.isChecked()){
                    type = 2; // Cầu thủ nước ngoài
                }
                String note = layoutInputPlayerInfoBinding.editPlayerNote.getText().toString();
                Player edt_player = new Player(player.getId(),name,doB,type,note);
                if(type == 2 && PlayerListActivity.countForeignPlayer(players) >= MainActivity.regulations.getMAX_FOREIGN_PLAYERS()){
                    Toast.makeText(dialog.getContext(), "Exceeded foreign Players", Toast.LENGTH_SHORT).show();
                    return;
                }
                players.set(index,edt_player);
                DatabaseRoute.updatePlayer(edt_player);
                dialog.cancel();
                adapter.notifyDataSetChanged();
            }
        });
    }
    public static int getAge(String dob){
        String[]age = dob.split("/");
        Calendar instance = Calendar.getInstance();
        int year = instance.get(Calendar.YEAR);
        return year - Integer.parseInt(age[2]);
    }
    public static int countForeignPlayer(ArrayList<Player> players){
        int num = 0;
        for (int i = 0 ; i<players.size(); i++){
            if(players.get(i).getType() > 1 )num++;
        }
        return num;
    }

    @Override
    protected void onResume() {
        super.onResume();
        regulations = new LeagueRegulations();
    }
}