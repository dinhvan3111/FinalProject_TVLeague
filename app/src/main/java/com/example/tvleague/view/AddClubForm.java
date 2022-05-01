package com.example.tvleague.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.tvleague.databinding.ActivityAddClubFormBinding;
import com.example.tvleague.databinding.LayoutInputPlayerInfoBinding;
import com.example.tvleague.model.Club;
import com.example.tvleague.model.DatabaseRoute;
import com.example.tvleague.model.Player;
import com.example.tvleague.model.PlayerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;

public class AddClubForm extends AppCompatActivity {
    private ObservableArrayList<Player> listAddedPlayer = new ObservableArrayList<>();
    private PlayerAdapter adapter;
    private ActivityAddClubFormBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddClubFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adapter = new PlayerAdapter(this,listAddedPlayer);
        binding.listAddedPlayer.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        binding.listAddedPlayer.setAdapter(adapter);
        binding.addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputPlayer();
            }
        });
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clubName = binding.edtClubName.getText().toString();
                String stadiumName = binding.edtClubStadium.getText().toString();
                if(clubName.length() == 0 || stadiumName.length() == 0){
                    Toast.makeText(AddClubForm.this, "Tên đội, sân nhà không thể bỏ trống!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(DatabaseRoute.findIdByNameClub(clubName)==-1){
                        if(listAddedPlayer.size() == 0){
                            Toast.makeText(AddClubForm.this, "Vui lòng thêm cầu thủ!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Club club = new Club(-1,clubName,stadiumName,-1, listAddedPlayer);
                            DatabaseRoute.addClub(club);
                            ArrayList<Club> clubs = DatabaseRoute.getAllClub();

                            int id_club = DatabaseRoute.findIdByNameClub(clubName);
                            if(id_club!=-1){
                                DatabaseRoute.addPlayersWithIdClub(listAddedPlayer,id_club);
                                //ArrayList<Player> players = DatabaseRoute.getPlayersOfClubById(DatabaseRoute.findIdByNameClub(clubName));

                                Intent addClubIntent = new Intent(AddClubForm.this,ClubRegistedActivity.class);
                                startActivity(addClubIntent);
                            }
                            else{
                                Toast.makeText(AddClubForm.this, "Thêm Đội bóng thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else{
                        Toast.makeText(AddClubForm.this, "Tên đội đã được sử dụng!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void inputPlayer() {
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
                if(doB.equals("")){
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
                listAddedPlayer.add(player);
                adapter.notifyDataSetChanged();
                for (int i =0; i < listAddedPlayer.size();i++){
                    System.out.println(listAddedPlayer.get(i).getName());
                }
//                adapter = new PlayerAdapter(listAddedPlayer);
//                binding.listAddedPlayer.setAdapter(adapter);
                dialog.cancel();
            }
        });
    }

}