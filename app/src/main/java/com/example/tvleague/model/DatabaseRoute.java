package com.example.tvleague.model;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.databinding.ObservableArrayList;

import com.example.tvleague.view.MainActivity;

import java.util.ArrayList;
import java.util.Observable;

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
            ArrayList<Player> listPlayers = getPlayersOfClubById(id);
            Club club = new Club(id,name,stadium,index,listPlayers);
            index++;
            clubs.add(club);
        }
        cursor.close();
        return clubs;
    }
    public static ArrayList<String> getAllNameClub(){
        ArrayList<String> clubs = new ArrayList<>();
        Cursor cursor = MainActivity.database.rawQuery("select  TenDoiBong from DoiBong", null);
        while(cursor.moveToNext()){
            String name = cursor.getString(0);
            clubs.add(name);
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
        MainActivity.database.update("CauThu", cv, "MaCauThu = ?", new String[]{id + ""});
    }
    public static Club getClubById(int id){
        Club club;
        Cursor cursor = MainActivity.database.
                rawQuery("select * from DoiBong where MaDoiBong = ?",
                        new String[] {String.valueOf(id)});
        while(cursor.moveToNext()){
            String TenDoi = cursor.getString(1);
            String SanNha = cursor.getString(2);
            club = new Club(id,TenDoi,SanNha,id,getPlayersOfClubById(id));
            return  club;
        }
        cursor.close();
        return null;
    }
    public static Player getPlayerById(int id){
        Player player;
        Cursor cursor = MainActivity.database.
                rawQuery("select * from CauThu where MaCauThu = ?",
                        new String[] {String.valueOf(id)});
        while(cursor.moveToNext()){
            int idClub = cursor.getInt(5);//MaDoiBong
            String name = cursor.getString(1);
            int type = cursor.getInt(3);
            String dob = cursor.getString(2);
            String note = cursor.getString(4);
            return  new Player(id,name,dob,type,note);
        }
        cursor.close();
        return null;
    }
    public static Player getPlayerByName(String name){
        Player player;
        Cursor cursor = MainActivity.database.
                rawQuery("select * from CauThu where TenCauThu = ?",
                        new String[] {name});
        while(cursor.moveToNext()){
            int idClub = cursor.getInt(5);//MaDoiBong
            int id = cursor.getInt(0);
            int type = cursor.getInt(3);
            String dob = cursor.getString(2);
            String note = cursor.getString(4);
            return  new Player(id,name,dob,type,note);
        }
        cursor.close();
        return null;
    }
    public static void addToSchedule(int round, int DoiNha, int DoiKhach, String date){
        ContentValues cv = new ContentValues();
        cv.put("VongDau", round);
        cv.put("MaDoiNha",DoiNha);
        cv.put("MaDoiKhach",DoiKhach);
        cv.put("NgayGio",date);
        MainActivity.database.insert("LichThiDau",null,cv);
    }
    public static void addToGoal(Goal goal){
        ContentValues cv = new ContentValues();
        cv.put("MaTranDau", goal.getMaTranDau());
        cv.put("MaCauThu",goal.getPlayer().getId());
        cv.put("MaLoaiBanThang",goal.getType());
        cv.put("ThoiDiemGhiBan",goal.getTime());
        cv.put("MaDoiBong",goal.getMaDoiBong());
        MainActivity.database.insert("BanThang",null,cv);
    }
    public static void addToMatch(int MaLichThiDau, String TiSo){
        ContentValues cv = new ContentValues();
        cv.put("MaLichThiDau",MaLichThiDau);
        cv.put("TiSo",TiSo);
        MainActivity.database.insert("TranDau",null,cv);
    }
    public static ObservableArrayList<Goal> getListGoal(int id_match, int id_club){
        ObservableArrayList<Goal> goals = new ObservableArrayList<>();
        Cursor cursor = MainActivity.database
                .rawQuery("select  * from BanThang " +
                        "where MaTranDau = " +  id_match + " and MaDoiBong = " + id_club, null);
        while(cursor.moveToNext()){
            int MaCauThu = cursor.getInt(2);
            int MaLoaiBanThang = cursor.getInt(3);
            int ThoiDiemGhiBan = cursor.getInt(4);
            Player player = getPlayerById(MaCauThu);
            Goal goal = new Goal(player,ThoiDiemGhiBan,MaLoaiBanThang,id_club,id_match);
            goals.add(goal);
        }
        cursor.close();
        return goals;
    }
    public static void startLeague(){
        ContentValues cv = new ContentValues();
        cv.put("BatDau", 1);
        MainActivity.database.update("BatDauGiaiDau", cv, "BatDau = ?", new String[]{0 + ""});
    }
    public static ObservableArrayList<Integer> get2IdClubByIdSchedule(int id){
        ObservableArrayList<Integer> clubs = new ObservableArrayList<>();
        Cursor cursor = MainActivity.database.
                rawQuery("select * from LichThiDau where MaLichThiDau = ?",
                        new String[] {String.valueOf(id)});
        while(cursor.moveToNext()){
            clubs.add(cursor.getInt(2));
            clubs.add(cursor.getInt(3));
            return clubs;
        }
        cursor.close();
        return null;
    }
    public static Match getMatchById(int id){
        Cursor cursor = MainActivity.database.
                rawQuery("select * from TranDau where MaTranDau = ?",
                        new String[] {String.valueOf(id)});
        while(cursor.moveToNext()){
            String score = cursor.getString(1);
            int MaLichThiDau = cursor.getInt(2);
            ObservableArrayList<Integer> clubs = get2IdClubByIdSchedule(MaLichThiDau);
            ObservableArrayList<Goal> listGoalHome = getListGoal(id,clubs.get(0));
            ObservableArrayList<Goal> listGoalAway = getListGoal(id,clubs.get(1));
            return new Match(id,score,listGoalHome,listGoalAway);
        }
        cursor.close();
        return null;
    }
    public static int getIdMatchBySchedule(int id){
        Cursor cursor = MainActivity.database.
                rawQuery("select * from TranDau where MaLichThiDau = ?",
                        new String[] {String.valueOf(id)});
        while(cursor.moveToNext()){
            return cursor.getInt(0);
        }
        cursor.close();
        return -1;
    }
    public static ArrayList<Schedule> getMatchByRound(int round){
        ArrayList<Schedule> schedules = new ArrayList<>();
        Schedule schedule;
        Cursor cursor = MainActivity.database.
                rawQuery("select * from LichThiDau where VongDau = ?",
                        new String[] {String.valueOf(round)});
        while(cursor.moveToNext()){
           int MaLichThiDau = cursor.getInt(0);
           int DoiNha = cursor.getInt(2);
           int DoiKhach = cursor.getInt(3);
           String date = cursor.getString(4);
           Club club1 = getClubById(DoiNha);
           Club club2 = getClubById(DoiKhach);
           int id_match = getIdMatchBySchedule(MaLichThiDau);
           Match match = getMatchById(id_match);
           schedule = new Schedule(club1.getStadium(),date,round,club1,club2,match);
           schedules.add(schedule);
        }
        cursor.close();
        return schedules;
    }
    public static int getIdClubByName(String name){
        Cursor cursor = MainActivity.database
                .rawQuery("select  * from DoiBong " +
                        "where TenDoiBong = " +"'" +  name + "'", null);
        while(cursor.moveToNext()){
            return cursor.getInt(0);
        }
        cursor.close();
        return -1;
    }
    public static ObservableArrayList<String> getPlayersByClubName(String name){
        int id = getIdClubByName(name);
        ObservableArrayList<String> players = new ObservableArrayList<>();
        System.out.println("MaDoi: " + id);
        Cursor cursor = MainActivity.database.
                rawQuery("select * from CauThu where MaDoiBong = ?",
                        new String[] {String.valueOf(id)});
        while(cursor.moveToNext()){
           players.add(cursor.getString(1));
        }
        cursor.close();
        return players;
    }
    public static int getIdScheduleByTwoIdClub(int id_home, int id_away){
        Cursor cursor = MainActivity.database
                .rawQuery("select  MaLichThiDau from LichThiDau " +
                        "where MaDoiNha = " +  id_home + " and MaDoiKhach = " + id_away, null);
        while(cursor.moveToNext()){
            return cursor.getInt(0);
        }
        cursor.close();
        return -1;
    }
    public static int getIdMatchByTwoIdClub(int id_home, int id_away){
        int id_schedule = getIdScheduleByTwoIdClub(id_home,id_away);
        Cursor cursor = MainActivity.database
                .rawQuery("select  MaTranDau from TranDau " +
                        "where MaLichThiDau = " + id_schedule, null);
        while(cursor.moveToNext()){
            return cursor.getInt(0);
        }
        cursor.close();
        return -1;
    }
}
