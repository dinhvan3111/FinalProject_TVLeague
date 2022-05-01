package com.example.tvleague.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.tvleague.R;
import com.example.tvleague.databinding.ActivityMainBinding;
import com.example.tvleague.model.Club;
import com.example.tvleague.model.DatabaseRoute;
import com.example.tvleague.model.LeagueRegulations;
import com.example.tvleague.model.Match;
import com.example.tvleague.model.Player;
import com.example.tvleague.model.Regulation;
import com.example.tvleague.model.Schedule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private String DB_NAME = "DB_TVLeague.db";
    private String DB_PATH = "/databases/";// lưu trữ trong thư mục cài đặt gốc
    public  static SQLiteDatabase database = null;
    public static LeagueRegulations regulations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //lần đầu thì copy
        copyDatabaseFromAssets();
        database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        regulations = new LeagueRegulations();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(DatabaseRoute.IsStartLeague() == true){
            binding.btnCreateCalendar.setEnabled(false);
            binding.btnAddClub.setEnabled(false);
            binding.btnListAddedClub.setEnabled(false);
            binding.btnLeagueManagement.setEnabled(true);
        }
        else{
            binding.btnLeagueManagement.setEnabled(false);
        }
        binding.btnAddClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addClubIntent = new Intent(MainActivity.this,AddClubForm.class);
                startActivity(addClubIntent);
            }
        });
        binding.btnListAddedClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addedClubIntent = new Intent(MainActivity.this,ClubRegistedActivity.class);
                startActivity(addedClubIntent);
            }
        });
        binding.btnLeagueManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addedClubIntent = new Intent(MainActivity.this,LeagueManagementActivity.class);
                startActivity(addedClubIntent);
            }
        });
        binding.btnCreateCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Club> clubs = DatabaseRoute.getAllClub();
                String notEnoughPlayer = getClubNotEnoughPlayer(clubs);
                String redundancyPlayer = getClubRedundancyPlayer(clubs);
                String redundancyForeign = getClubRedundancyForeignPlayer(clubs);
                String invalidAge = getPlayerInvalidAge(clubs);

                //Kiểm tra quy định

                //Hợp lệ
                if(notEnoughPlayer.equals("") && redundancyPlayer.equals("")
                    &&redundancyForeign.equals("") && invalidAge.equals("")){
                    ProgressDialog mProgressDialog  = ProgressDialog.show(MainActivity.this, "Loading","Đang lập lịch giải đấu ...", true);
                    new Thread() {
                        @Override
                        public void run() {

                            ArrayList<Schedule> listSchedules = new ArrayList<>();
                            ArrayList<String> round = new ArrayList<>();
                            ArrayList<String> first_leg = new ArrayList<>();
                            ArrayList<String> second_leg = new ArrayList<>();
                            int sizeOfClub = clubs.size();
                            int arrSchedule [][]= getArraySchedule(clubs);
                            for (int i = 0; i<sizeOfClub - 1; i++){
                                LocalDate now = LocalDate.now();
                                String dt = now.toString();
                                String dt_second_leg = now.toString();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Calendar c = Calendar.getInstance();
                                c.add(Calendar.DATE, (i+1)*7);  // number of days to add
                                dt = sdf.format(c.getTime());  // dt is now the new date
                                for (int j = 0;j<sizeOfClub/2 ;j++){
                                    int MaDoiNha = j ;
                                    int MaDoiKhach = sizeOfClub - j - 1;
                                    String date;
                                    String date_2;//luot ve

                                    Calendar d = Calendar.getInstance();
                                    d.add(Calendar.DATE, (i+1 + sizeOfClub)*7);  // number of days to add
                                    dt_second_leg = sdf.format(d.getTime());
                                    if(j < sizeOfClub/4){
                                        c.add(Calendar.DATE, 1);  // number of days to add
                                        dt = sdf.format(c.getTime());  // dt is now the new date
                                        d.add(Calendar.DATE, 1);  // number of days to add
                                        dt_second_leg = sdf.format(d.getTime());
                                    }



                                    if(j % 2 == 0){
                                        date = "17:00 " + dt;
                                        date_2 = "17:00 " + dt_second_leg;
                                    }
                                    else {
                                        date = "19:00 " + dt;
                                        date_2 = "19:00 " + dt_second_leg;
                                    }
                                    DatabaseRoute.addToSchedule(i+1,arrSchedule[i][MaDoiNha],arrSchedule[i][MaDoiKhach],date);
                                    int id_schedule_first = DatabaseRoute.getIdScheduleByTwoIdClub(arrSchedule[i][MaDoiNha],arrSchedule[i][MaDoiKhach]);
                                    DatabaseRoute.addToMatch(id_schedule_first,null);
                                    DatabaseRoute.addToSchedule(i+sizeOfClub,arrSchedule[i][MaDoiKhach],arrSchedule[i][MaDoiNha],date_2);
                                    int id_schedule_second = DatabaseRoute.getIdScheduleByTwoIdClub(arrSchedule[i][MaDoiKhach],arrSchedule[i][MaDoiNha]);
                                    DatabaseRoute.addToMatch(id_schedule_second,null);
                                }
                            }
                            try {

                                // code runs in a thread
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressDialog.dismiss();
                                        DatabaseRoute.startLeague();//bat dau giai
                                        binding.btnCreateCalendar.setEnabled(false);
                                        binding.btnAddClub.setEnabled(false);
                                        binding.btnListAddedClub.setEnabled(false);
                                        binding.btnLeagueManagement.setEnabled(true);
                                        Toast.makeText(MainActivity.this, "Giải đấu đã bắt đầu!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (final Exception ex) {

                            }
                        }
                    }.start();
                }
                else{
                    showInValid(getClubNotEnoughPlayer(clubs),
                            getClubRedundancyPlayer(clubs),getClubRedundancyForeignPlayer(clubs),
                            getPlayerInvalidAge(clubs));
                }

                //TODO

            }
        });
        binding.btnLeagueRegulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ArrayList<Club> clubs = DatabaseRoute.getAllClub();
//                showInValid(getClubNotEnoughPlayer(clubs),
//                        getClubRedundancyPlayer(clubs),getClubRedundancyForeignPlayer(clubs),
//                        getPlayerInvalidAge(clubs));


//                ArrayList<Club> clubs = DatabaseRoute.getAllClub();
//                System.out.println(getPlayerInvalidAge(clubs));
                Intent intent = new Intent(MainActivity.this,RegulationActivity.class);
                startActivity(intent);
            }
        });
    }
    private void copyDatabase(){
        try {
            InputStream inputStream = getAssets().open(DB_NAME);
            String outputFileName = getApplicationInfo().dataDir + DB_PATH + DB_NAME;// nơi lưu trữ
            File file = new File(getApplicationInfo().dataDir + DB_PATH);
            if(!file.exists()){
                file.mkdir();
            }
            OutputStream outputStream = new FileOutputStream(outputFileName);

            //chép dữ liệu
            byte[] buffer = new byte[1024];
            int len;
            while((len = inputStream.read(buffer)) > 0){
                outputStream.write(buffer,0,len);
            };
            outputStream.flush();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERROR","Lỗi sao chép");
        }
    }
    private void copyDatabaseFromAssets(){
        File dbFile = getDatabasePath(DB_NAME);
        //nếu db chưa tồn tại thì sao chép vào, không thì thoi.
        if(!dbFile.exists()){
            copyDatabase();
            Toast.makeText(this, "Successfull copy!", Toast.LENGTH_SHORT).show();
        }
//        else{
//            dbFile.delete();
//            copyDatabase();
//        }
    }
    private int[][] getArraySchedule(ArrayList<Club> clubs){
        int sizeOfClub = clubs.size();
        int arrSchedule[][] = new int[sizeOfClub - 1][sizeOfClub];
        for (int i = 0; i<sizeOfClub - 1; i++){
            for (int j = 0;j<sizeOfClub;j++){
                if(j == 0){
                    arrSchedule[i][j] = 1;
                }
                else{
                    if(j>i){
                        arrSchedule[i][j] = j - i + 1;
                    }
                    else{
                        arrSchedule[i][j] = sizeOfClub - (i - j);
                    }
                }
            }
        }
        return arrSchedule;
    }
    private String getClubNotEnoughPlayer(ArrayList<Club> clubs){
        String club = "";
        for(int i = 0 ; i< clubs.size();i++){
            if(clubs.get(i).getListPlayers().size() < regulations.getMIN_PLAYERS()){
                if(club.equals("")){
                    club += clubs.get(i).getName();
                }
                else {
                    club += ", " + clubs.get(i).getName();
                }
            }
        }
        return club;
    }
    private String getClubRedundancyPlayer(ArrayList<Club> clubs){
        String club = "";
        for(int i = 0 ; i< clubs.size();i++){
            if(clubs.get(i).getListPlayers().size() > regulations.getMAX_PLAYERS()){
                if(club.equals("")){
                    club += clubs.get(i).getName();
                }
                else {
                    club += ", " + clubs.get(i).getName();
                }
            }
        }
        return club;
    }
    private String getClubRedundancyForeignPlayer(ArrayList<Club> clubs){
        String club = "";
        for(int i = 0 ; i< clubs.size();i++){
            int numForeign = PlayerListActivity.countForeignPlayer(clubs.get(i).getListPlayers());
            if(numForeign > regulations.getMAX_FOREIGN_PLAYERS()){
                if(club.equals("")){
                    club += clubs.get(i).getName();
                }
                else {
                    club += ", " + clubs.get(i).getName();
                }
            }
        }
        return club;
    }
    private String getPlayerInvalidAge(ArrayList<Club> clubs){
        String player = "";
        for(int i = 0 ; i< clubs.size();i++){
            ArrayList<Player> players = clubs.get(i).getListPlayers();
            for(int j = 0; j < players.size();j++){
                int age = PlayerListActivity.getAge(players.get(j).getDoB());
                if(age > regulations.getMAX_AGE() || age < regulations.getMIN_AGE() ){
                    if(player.equals("")){
                        player += players.get(j).getName() + "("
                                + clubs.get(i).getName() + ")";
                    }
                    else {
                        player += ", " + players.get(j).getName() + "("
                                + clubs.get(i).getName() + ")";
                    }
                }
            }
        }
        return player;
    }
    private void showInValid(String NotEnoughPlayer, String redundancyPlayer, String RedundancyForeign, String invalidAge){
        String message ="";
        if(!NotEnoughPlayer.equals("")){
            message += "Các đội sau không đủ số lượng cầu thủ tối thiểu: " + NotEnoughPlayer + "\n\n";
        }
        if(!redundancyPlayer.equals("")){
            message += "Các đội sau vượt quá số lượng cầu thủ tối đa: " + redundancyPlayer + "\n\n";
        }
        if(!RedundancyForeign.equals("")){
            message += "Các đội sau vượt quá số lượng cầu thủ nước ngoài tối đa: " + RedundancyForeign + "\n\n";
        }
        if(!invalidAge.equals("")){
            message += "Các cầu thủ sau không đúng quy định về tuổi: " + invalidAge + "\n\n";
        }
        new AlertDialog.Builder(binding.getRoot().getContext())
                .setTitle("Không đúng quy định")
                .setMessage(message)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        regulations = new LeagueRegulations();
    }
}