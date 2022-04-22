package com.example.tvleague.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.tvleague.databinding.ActivityMainBinding;
import com.example.tvleague.model.Club;
import com.example.tvleague.model.DatabaseRoute;
import com.example.tvleague.model.Match;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //lần đầu thì copy
        copyDatabaseFromAssets();
        database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
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
                ProgressDialog mProgressDialog  = ProgressDialog.show(MainActivity.this, "Loading","Đang lập lịch giải đấu ...", true);
                new Thread() {
                    @Override
                    public void run() {

                        ArrayList<Schedule> listSchedules = new ArrayList<>();
                        ArrayList<String> round = new ArrayList<>();
                        ArrayList<String> first_leg = new ArrayList<>();
                        ArrayList<String> second_leg = new ArrayList<>();

                        ArrayList<Club> clubs = DatabaseRoute.getAllClub();
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
                            System.out.println("Vong: " + (i+1));
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
}