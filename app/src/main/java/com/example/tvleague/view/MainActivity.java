package com.example.tvleague.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.tvleague.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
}