package com.example.tvleague;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.databinding.ObservableArrayList;

import java.util.ArrayList;

public class DatabaseRoute {
    public static ArrayList<Club> getAllClub(){
        ArrayList<Club> clubs = new ArrayList<>();
        Cursor cursor = MainActivity.database.query("DoiBong",
                null,null,null,null,null,null);
        int index = 0;
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String stadium= cursor.getString(2);
            Club club = new Club(id,name,stadium,index);
            index++;
            clubs.add(club);
        }
        cursor.close();
        return clubs;
    }
    public static void addClub(Club club){
        ContentValues cv = new ContentValues();
        cv.put("TenDoiBong",club.name);
        cv.put("TenSan",club.stadium);
        MainActivity.database.insert("DoiBong",null,cv);
    }
    public static int findIdByNameClub(String name){
        Cursor cursor = MainActivity.database.query("DoiBong",
                null,null,null,null,null,null);
        while(cursor.moveToNext()){
            if(name.equals(cursor.getString(1)))
                return cursor.getInt(0);
        }
        cursor.close();
        return -1;
    }
    public static void addPlayersWithIdClub(ArrayList<Player> player, int id_club){
        for(int i = 0;i<player.size();i++){
            ContentValues cv = new ContentValues();
            cv.put("MaCauThu", (Integer) null);
            cv.put("MaDoiBong",id_club);
            cv.put("TenCauThu",player.get(i).getName());
            cv.put("MaLoaiCauThu",player.get(i).getType());
            cv.put("NgaySinh",player.get(i).getDoB());
            cv.put("GhiChu",player.get(i).getNote());
            System.out.println("Ma Doi: " + id_club + ", ten cau thu: " + player.get(i).getName() + ".ghi chu: "
             + player.get(i).getNote());
            MainActivity.database.insert("CauThu",null,cv);
        }
    }
    public static void addPlayerWithIdClub(Player player, int id_club){
        ContentValues cv = new ContentValues();
        cv.put("MaCauThu", (Integer) null);
        cv.put("MaDoiBong",id_club);
        cv.put("TenCauThu",player.getName());
        cv.put("MaLoaiCauThu",player.getType());
        cv.put("NgaySinh",player.getDoB());
        cv.put("GhiChu",player.getNote());
        MainActivity.database.insert("CauThu",null,cv);
    }
    public static ArrayList<Player> getPlayersOfClubById(int id_club){
        ArrayList<Player> players = new ArrayList<>();
        Cursor cursor = MainActivity.database.rawQuery("select  * from CauThu", null);
        while(cursor.moveToNext()){
            int idPlayer = cursor.getInt(0);
            int idClub = cursor.getInt(5);//MaDoiBong
            String name = cursor.getString(1);
            int type = cursor.getInt(3);
            String dob = cursor.getString(2);
            String note = cursor.getString(4);


            if(id_club == idClub){
                Player player = new Player(idPlayer,name,dob,type,note);
                players.add(player);
            }
        }
        if (players.size() == 0 ){
            return null;
        }
        return players;
    }
    public static void updatePlayer(Player player){
        ContentValues cv = new ContentValues();
        cv.put("TenCauThu",player.getName());
        cv.put("NgaySinh",player.getDoB());
        cv.put("MaLoaiCauThu",player.getType());
        cv.put("GhiChu",player.getNote());
        int id = player.getId();
        System.out.println(id + "");
        MainActivity.database.update("CauThu", cv, "MaCauThu = ?", new String[]{id + ""});
    }
}
