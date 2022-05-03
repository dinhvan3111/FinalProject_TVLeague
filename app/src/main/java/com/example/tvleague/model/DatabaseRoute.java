package com.example.tvleague.model;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.databinding.ObservableArrayList;

import com.example.tvleague.view.MainActivity;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    public static ObservableArrayList<Player> getPlayersOfClubById(int id_club){
        ObservableArrayList<Player> players = new ObservableArrayList<>();
        Cursor cursor = null;
        try {
            cursor = MainActivity.database.rawQuery("select  * from CauThu", null);
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
        }
        finally {
            cursor.close();
        }
        if (players.size() == 0 ){
            return null;
        }
        return players;
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
    public static int getIdScheduleByIdMatch(int id){
        Cursor cursor = MainActivity.database.
                rawQuery("select * from TranDau where MaTranDau = ?",
                        new String[] {String.valueOf(id)});
        while(cursor.moveToNext()){
            return cursor.getInt(2);
        }
        cursor.close();
        return -1;
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
        Cursor cursor = null;
        try{
            cursor = MainActivity.database.
                    rawQuery("select * from DoiBong where MaDoiBong = ?",
                            new String[] {String.valueOf(id)});
            while(cursor.moveToNext()){
                String TenDoi = cursor.getString(1);
                String SanNha = cursor.getString(2);
                club = new Club(id,TenDoi,SanNha,id,getPlayersOfClubById(id));
                return  club;
            }
        }
        finally {
            cursor.close();
        }
        return null;
    }

    public static Player getPlayerById(int id){
        Player player;
        Cursor cursor = null;
        try {
            cursor = MainActivity.database.
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
        }
        finally {
            cursor.close();
        }
        return null;
    }
    public static String getNameClubByIdPlayer(int id){
        Cursor cursor = MainActivity.database.
                rawQuery("select * from CauThu where MaCauThu = ?",
                        new String[] {String.valueOf(id)});
        while(cursor.moveToNext()) {
            return getClubById(cursor.getInt(5)).getName();
        }
        return null;
    }
    public static String getScoreByIdSchedule(int id){
        int id_match = getIdMatchBySchedule(id);
        Cursor cursor = MainActivity.database.
                rawQuery("select * from TranDau where MaTranDau = ?",
                        new String[] {String.valueOf(id_match)});
        while(cursor.moveToNext()){
            return cursor.getString(1);
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
    public static void updateGoal(Goal goal){
        ContentValues cv = new ContentValues();
        cv.put("MaTranDau", goal.getMaTranDau());
        cv.put("MaCauThu",goal.getPlayer().getId());
        cv.put("MaLoaiBanThang",goal.getType());
        cv.put("ThoiDiemGhiBan",goal.getTime());
        cv.put("MaDoiBong",goal.getMaDoiBong());
        MainActivity.database.update("BanThang",cv,"MaBanThang=?",new String[]{goal.getId() + ""});
    }
    public static void addToMatch(int MaLichThiDau, String TiSo){
        ContentValues cv = new ContentValues();
        cv.put("MaLichThiDau",MaLichThiDau);
        cv.put("TiSo",TiSo);
        MainActivity.database.insert("TranDau",null,cv);
    }
    public static void updateScore(int MaLichThiDau, String TiSo){
        ContentValues cv = new ContentValues();
        int id_match = getIdMatchBySchedule(MaLichThiDau);
        cv.put("MaLichThiDau",MaLichThiDau);
        cv.put("TiSo",TiSo);
        MainActivity.database.update("TranDau", cv, "MaTranDau = ?", new String[]{id_match + ""});
    }
    public static ObservableArrayList<Goal> getListGoal(int id_match, int id_club){
        ObservableArrayList<Goal> goals = new ObservableArrayList<>();
        Cursor cursor = null;
        try{
           cursor = MainActivity.database
                    .rawQuery("select  * from BanThang " +
                            "where MaTranDau = " +  id_match + " and MaDoiBong = " + id_club, null);
            while(cursor.moveToNext()){
                int MaBanThang = cursor.getInt(0);
                int MaCauThu = cursor.getInt(2);
                int MaLoaiBanThang = cursor.getInt(3);
                int ThoiDiemGhiBan = cursor.getInt(4);
                Player player = getPlayerById(MaCauThu);
                Goal goal = new Goal(MaBanThang,player,ThoiDiemGhiBan,MaLoaiBanThang,id_club,id_match);
                goals.add(goal);
            }
        }
        finally {
            cursor.close();
        }
        return goals;
    }
    public static void startLeague(){
        ContentValues cv = new ContentValues();
        cv.put("BatDau", 1);
        MainActivity.database.update("BatDauGiaiDau", cv, "BatDau = ?", new String[]{0 + ""});
        ContentValues contentValues = new ContentValues();
        Cursor cursor = MainActivity.database.rawQuery("select * from LichThiDau limit 1;",null);
        String date = "";
        while(cursor.moveToNext()){
            date = cursor.getString(4);
        }
        String [] arrayString = date.split(" ");
        cursor.close();
        contentValues.put("Ngay", arrayString[1]);
        // Thêm dòng vào bảng BXH ngày diễn ra trận đầu tiên
        MainActivity.database.insert("BXH", null, contentValues);
        Cursor cursor1 = MainActivity.database.rawQuery("select * from BXH limit 1;",null);
        int idRankTable = 0;
        while(cursor1.moveToNext()){
            idRankTable = cursor1.getInt(0);
        }
        cursor1.close();
        ArrayList<RankingClub> rankingClubs = new ArrayList<>();
        ArrayList<Club> listClub = DatabaseRoute.getAllClub();
        for(int i = 0; i < listClub.size();i++){
            RankingClub rankingClub = new RankingClub(listClub.get(i),0,0,0,0,0,0);
            rankingClub.calPoint();
            rankingClub.calGoalDifference();
            rankingClubs.add(rankingClub);
        }
        Collections.sort(rankingClubs, new RankingClub.SortbyPriority1());
        // Set rank after sorting
        for(int i= 0; i< rankingClubs.size();i++){
            rankingClubs.get(i).setRank(i + 1);
        }
        // Bảng xếp hạng trước khi diễn ra trận đấu đầu tiên
        MainActivity.database.beginTransaction();
        try
        {
            for (int i =0; i < rankingClubs.size();i++){
                int idRankingTable = idRankTable;
                int idClub = rankingClubs.get(i).getClub().id;
                int winCount = rankingClubs.get(i).getWinCount();
                int drawCount = rankingClubs.get(i).getDrawCount();
                int loseCount = rankingClubs.get(i).getLoseCount();
                int goalCount = rankingClubs.get(i).getGoalCount();
                int goalConcededCount = rankingClubs.get(i).getGoalConcededCount();
                int rank = rankingClubs.get(i).getRank();
                int gd = rankingClubs.get(i).getGoalDifference();
                int played = rankingClubs.get(i).getRound();
                ContentValues ranking = new ContentValues();
                ranking.put("MaBXH",idRankingTable);
                ranking.put("MaDoiBong",idClub);
                ranking.put("SoTranThang", winCount);
                ranking.put("SoTranHoa", drawCount);
                ranking.put("SoTranThua",loseCount);
                ranking.put("SoBanThang",goalCount);
                ranking.put("SoBanThua", goalConcededCount);
                ranking.put("Hang", rank);
                ranking.put("HieuSo",gd);
                ranking.put("SoTran", played);
                MainActivity.database.insert("BXH_DoiBong",null,ranking);
            }
            // If successful commit those changes
            MainActivity.database.setTransactionSuccessful();
        }
        finally
        {
            MainActivity.database.endTransaction();
        }
    }

    public static void insertNewRankingTableByIdRankingTable(Schedule schedule, String date, int priority){
//        Collections.sort(rankingClubs, new RankingClub.SortbyPriority1());
//        // Set rank after sorting
//        for(int i= 0; i< rankingClubs.size();i++){
//            rankingClubs.get(i).setRank(i + 1);
//        }
        int newIdRankingTable = 0;
        MainActivity.database.beginTransaction();
        try
        {
            int currentIdRankingTable = 0;
            Cursor cursor = MainActivity.database.rawQuery("select * from BXH order by MaBXH desc limit 1",null);
            while (cursor.moveToNext()){
                currentIdRankingTable = cursor.getInt(0);
            }
            Cursor cursor1 = MainActivity.database.rawQuery("select * from BXH where MaBXH = " + currentIdRankingTable,null);
            String oldDate = "";
            if (cursor1.moveToNext()){
                oldDate = cursor1.getString(1);
            }
            ArrayList<RankingClub> rankingClubs = getRankingByDate(oldDate);
            newIdRankingTable = currentIdRankingTable + 1;
            ContentValues cv = new ContentValues();
            cv.put("MaBXH", newIdRankingTable);
            cv.put("Ngay", date);
            MainActivity.database.insert("BXH",null, cv);
            for (int i =0; i < rankingClubs.size();i++){
                int idClub = rankingClubs.get(i).getClub().id;
                int winCount = rankingClubs.get(i).getWinCount();
                int drawCount = rankingClubs.get(i).getDrawCount();
                int loseCount = rankingClubs.get(i).getLoseCount();
                int goalCount = rankingClubs.get(i).getGoalCount();
                int goalConcededCount = rankingClubs.get(i).getGoalConcededCount();
                int rank = rankingClubs.get(i).getRank();
                int gd = rankingClubs.get(i).getGoalDifference();
                int played = rankingClubs.get(i).getRound();
                ContentValues ranking = new ContentValues();
                ranking.put("MaBXH",newIdRankingTable);
                ranking.put("MaDoiBong",idClub);
                ranking.put("SoTranThang", winCount);
                ranking.put("SoTranHoa", drawCount);
                ranking.put("SoTranThua",loseCount);
                ranking.put("SoBanThang",goalCount);
                ranking.put("SoBanThua", goalConcededCount);
                ranking.put("Hang", rank);
                ranking.put("HieuSo",gd);
                ranking.put("SoTran", played);
                MainActivity.database.insert("BXH_DoiBong",null,ranking);
            }
            // If successful commit those changes
            MainActivity.database.setTransactionSuccessful();
        }
        finally
        {
            MainActivity.database.endTransaction();
        }
        updateRankingClubAfterMatchResult(schedule,schedule.getClub1().getId(),newIdRankingTable,0);
        updateRankingClubAfterMatchResult(schedule,schedule.getClub2().getId(),newIdRankingTable,0);
        sortRankingClubAfterMatchResult(date,priority );
    }

    public static void updateRankingTableByIdRankingTable(int idRankingTable, ArrayList<RankingClub> rankingClubs){
        MainActivity.database.beginTransaction();
        try
        {
            for (int i =0; i < rankingClubs.size();i++){
                int idClub = rankingClubs.get(i).getClub().id;
                int winCount = rankingClubs.get(i).getWinCount();
                int drawCount = rankingClubs.get(i).getDrawCount();
                int loseCount = rankingClubs.get(i).getLoseCount();
                int goalCount = rankingClubs.get(i).getGoalCount();
                int goalConcededCount = rankingClubs.get(i).getGoalConcededCount();
                int rank = rankingClubs.get(i).getRank();
                int gd = rankingClubs.get(i).getGoalDifference();
                int played = rankingClubs.get(i).getRound();
                ContentValues ranking = new ContentValues();
                ranking.put("MaBXH",idRankingTable);
                System.out.println(idRankingTable);
                ranking.put("MaDoiBong",idClub);
                ranking.put("SoTranThang", winCount);
                ranking.put("SoTranHoa", drawCount);
                ranking.put("SoTranThua",loseCount);
                ranking.put("SoBanThang",goalCount);
                ranking.put("SoBanThua", goalConcededCount);
                ranking.put("Hang", rank);
                ranking.put("HieuSo",gd);
                ranking.put("SoTran", played);
                MainActivity.database.update("BXH_DoiBong",ranking,"MaBXH = ? and MaDoiBong = ?", new String[]{idRankingTable+ "",idClub + ""});
            }
            // If successful commit those changes
            MainActivity.database.setTransactionSuccessful();
        }
        finally
        {
            MainActivity.database.endTransaction();
        }
    }

    public static RankingClub getRankingClubByIdRankingTable(int idClub, int idTable){
        Cursor cursor = MainActivity.database.rawQuery("select * from BXH_DoiBong where MaBXH = " + idTable + " and MaDoiBong = " + idClub,null);
        RankingClub rankingClub = null;
        while (cursor.moveToNext()){
            Club club = getClubById(idClub);
            int winCount = cursor.getInt(2);
            int drawCount = cursor.getInt(3);
            int loseCount = cursor.getInt(4);
            int goalCount = cursor.getInt(5);
            int goalConcededCount = cursor.getInt(6);
            int rank = cursor.getInt(7);
            int gd = cursor.getInt(8);
            int played = cursor.getInt(9);
            rankingClub = new RankingClub(club,winCount,drawCount,loseCount,goalCount,goalConcededCount,played);
            rankingClub.setRank(rank);
            rankingClub.setGoalDifference(gd);
        }
        return rankingClub;
    }

    public static int deleteClubRankInRankingTableByIdTable(int idClub, int idTable){
        int res = MainActivity.database.delete("BXH_DoiBong","MaBXH = ? and MaDoiBong = ? ",new String[]{idTable + "", idClub + ""});
        return res;
    }

    public static void insertClubRankInRankingTableByIdTable(RankingClub rankingClub,int idTable){
        ContentValues cv = new ContentValues();
        cv.put("MaBXH", idTable);
        cv.put("MaDoi", rankingClub.getClub().getId());
        cv.put("SoTranThang", rankingClub.getWinCount());
        cv.put("SoTranHoa", rankingClub.getDrawCount());
        cv.put("SoTranThua", rankingClub.getLoseCount());
        cv.put("SoBanThang", rankingClub.getGoalCount());
        cv.put("SoBanThua", rankingClub.getGoalConcededCount());
        cv.put("HieuSo", rankingClub.getGoalDifference());
        cv.put("Rank", rankingClub.getRank());
        cv.put("SoTran", rankingClub.getRound());
        MainActivity.database.insert("BXH_DoiBong", null,cv);
    }
    // Cập nhật lại thông số trên bảng xếp hạng của CLB: mode = 1 cập nhật lại kết quả cũ, mode = 0 cập nhật kết quả mới
    public static void updateRankingClubAfterMatchResult(Schedule schedule, int idClub, int idTable, int mode){
        // Xếp hạng cũ
        RankingClub oldRankingClub = getRankingClubByIdRankingTable(idClub,idTable);
        RankingClub rankingClub= null;
        if(mode == 0){
           rankingClub = getRankingClubByIdRankingTable(idClub,idTable);
        }
        else{
            if(idTable == 1){
                Club club = getClubById(idClub);
                rankingClub = new RankingClub(club,0,0,0,0,0,0);
            }
            else{
                rankingClub = getRankingClubByIdRankingTable(idClub,idTable - 1);
            }
        }
        int club1Score = schedule.getMatch().getListGoalClub1().size();
        int club2Score = schedule.getMatch().getListGoalClub2().size();
        // Xếp hạng mới
        if(mode == 0){ // Cập nhật sau kết quả trận đấu mới
            if(idClub == schedule.getClub1().getId()){ // Nếu là club1
                if(club1Score > club2Score){ // Club 1 chiến thắng
                    rankingClub.setWinCount(rankingClub.getWinCount() + 1); // +1 trận thắng
                    rankingClub.setGoalDifference(rankingClub.getDrawCount() + (club1Score - club2Score)); // Cập nhật hiệu số
                }
                else if( club1Score == club2Score){ // Club1 hoà Club2
                    rankingClub.setDrawCount(rankingClub.getDrawCount() + 1); // +1 trận hoà
                }
                else{ // Club1 thua
                    rankingClub.setLoseCount(rankingClub.getLoseCount() + 1); // +1 trận thua
                    rankingClub.setGoalDifference(rankingClub.getDrawCount() + (club1Score - club2Score)); // Cập nhật hiệu số
                }
                rankingClub.setGoalCount(rankingClub.getGoalCount() + club1Score); // Cập nhật số bàn thắng
                rankingClub.setGoalConcededCount(rankingClub.getGoalConcededCount() + club2Score); // Cập nhật số bàn thua
                rankingClub.setRound(rankingClub.getRound() + 1); // Cập nhật số trận đã chơi
            }
            else{ // Nếu là club2
                if(club2Score > club1Score){ // Club 2 chiến thắng
                    rankingClub.setWinCount(rankingClub.getWinCount() + 1); // +1 trận thắng
                    rankingClub.setGoalDifference(rankingClub.getDrawCount() + (club1Score - club2Score)); // Cập nhật hiệu số
                }
                else if( club1Score == club2Score){ // Club1 hoà Club2
                    rankingClub.setDrawCount(rankingClub.getDrawCount() + 1); // +1 trận hoà
                }
                else{ // Club2 thua
                    rankingClub.setLoseCount(rankingClub.getLoseCount() + 1); // +1 trận thua
                    rankingClub.setGoalDifference(rankingClub.getDrawCount() + (club1Score - club2Score)); // Cập nhật hiệu số
                }
                rankingClub.setGoalCount(rankingClub.getGoalCount() + club2Score); // Cập nhật số bàn thắng
                rankingClub.setGoalConcededCount(rankingClub.getGoalConcededCount() + club1Score); // Cập nhật số bàn thua
                rankingClub.setRound(rankingClub.getRound() + 1); // Cập nhật số trận đã chơi
            }
        }
        else{ // Cập nhật sau kết quả trận đấu cũ
            System.out.println(deleteClubRankInRankingTableByIdTable(idClub,idTable) + " Xoa");
            if(idClub == schedule.getClub1().getId()){ // Nếu là club1
                if(club1Score > club2Score){ // Club 1 chiến thắng
                    rankingClub.setWinCount(rankingClub.getWinCount() + 1); // +1 trận thắng
                    rankingClub.setGoalDifference(rankingClub.getDrawCount() + (club1Score - club2Score)); // Cập nhật hiệu số
                }
                else if( club1Score == club2Score){ // Club1 hoà Club2
                    rankingClub.setDrawCount(rankingClub.getDrawCount() + 1); // +1 trận hoà
                }
                else{ // Club1 thua
                    rankingClub.setLoseCount(rankingClub.getLoseCount() + 1); // +1 trận thua
                    rankingClub.setGoalDifference(rankingClub.getDrawCount() + (club1Score - club2Score)); // Cập nhật hiệu số
                }
                rankingClub.setGoalCount(rankingClub.getGoalCount() + club1Score); // Cập nhật số bàn thắng
                rankingClub.setGoalConcededCount(rankingClub.getGoalConcededCount() + club2Score); // Cập nhật số bàn thua
                rankingClub.setRound(rankingClub.getRound() + 1); // Cập nhật số trận đã chơi
            }
            else{ // Nếu là club2
                if(club2Score > club1Score){ // Club 2 chiến thắng
                    rankingClub.setWinCount(rankingClub.getWinCount() + 1); // +1 trận thắng
                    rankingClub.setGoalDifference(rankingClub.getDrawCount() + (club1Score - club2Score)); // Cập nhật hiệu số
                }
                else if( club1Score == club2Score){ // Club1 hoà Club2
                    rankingClub.setDrawCount(rankingClub.getDrawCount() + 1); // +1 trận hoà
                }
                else{ // Club2 thua
                    rankingClub.setLoseCount(rankingClub.getLoseCount() + 1); // +1 trận thua
                    rankingClub.setGoalDifference(rankingClub.getDrawCount() + (club1Score - club2Score)); // Cập nhật hiệu số
                }
                rankingClub.setGoalCount(rankingClub.getGoalCount() + club2Score); // Cập nhật số bàn thắng
                rankingClub.setGoalConcededCount(rankingClub.getGoalConcededCount() + club1Score); // Cập nhật số bàn thua
                rankingClub.setRound(rankingClub.getRound() + 1); // Cập nhật số trận đã chơi
            }
        }
        ContentValues cv = new ContentValues();
        cv.put("MaBXH", idTable);
        cv.put("MaDoiBong", idClub);
        cv.put("SoTranThang", rankingClub.getWinCount());
        cv.put("SoTranHoa", rankingClub.getDrawCount());
        cv.put("SoTranThua", rankingClub.getLoseCount());
        cv.put("SoBanThang", rankingClub.getGoalCount());
        cv.put("SoBanThua", rankingClub.getGoalConcededCount());
        cv.put("Hang", rankingClub.getRank());
        cv.put("HieuSo", rankingClub.getGoalDifference());
        cv.put("SoTran", rankingClub.getRound());
        Cursor cursor = MainActivity.database.rawQuery("select * from BXH order by MaBXH desc limit 1",null);
        int latestRankingTabbleId = 0;
        if (cursor.moveToNext()){
            latestRankingTabbleId = cursor.getInt(0);
        }
        if(mode == 0){
            MainActivity.database.update("BXH_DoiBong", cv,"MaBXH = ? and MaDoiBong = ?", new String[]{idTable + "", idClub + ""});
        }
        else{
            int isWin = rankingClub.getWinCount() - oldRankingClub.getWinCount();
            int isLose = rankingClub.getLoseCount() - oldRankingClub.getLoseCount();
            int isDraw = rankingClub.getDrawCount() - oldRankingClub.getDrawCount();
            int changeGoalCount = rankingClub.getGoalCount() - oldRankingClub.getGoalCount();
            int changeGoalConcededCount = rankingClub.getGoalConcededCount() - oldRankingClub.getGoalConcededCount();
            MainActivity.database.insert("BXH_DoiBong",null, cv);
            if(isWin > 0){ // Nếu sửa trận đấu cũ thành 1 trận thắng
//                ContentValues cv1 = new ContentValues();
//                cv1.put("SoTranThang", "SoTranThang + 1");
//                MainActivity.database.update("BXH_DoiBong",cv,"MaDoiBong = ? and MaBXH > ? and MaBXH <= ?",new String[]{idClub + "", idTable + "", latestRankingTabbleId + ""});
                String strSQL = "update BXH_DoiBong set SoTranThang = SoTranThang + 1 where MaDoiBong = " + idClub + " and MaBXH > " + idTable +" and MaBXH <= " + latestRankingTabbleId;
                MainActivity.database.execSQL(strSQL);
            }
            else if( isWin < 0){
                String strSQL = "update BXH_DoiBong set SoTranThang = SoTranThang - 1 where MaDoiBong = " + idClub + " and MaBXH > " + idTable +" and MaBXH <= " + latestRankingTabbleId;
                MainActivity.database.execSQL(strSQL);
            }
            if(isDraw > 0){ // Nếu sửa trận đấu cũ thành 1 trận thắng
//                ContentValues cv1 = new ContentValues();
//                cv1.put("SoTranHoa", "SoTranHoa + 1");
//                MainActivity.database.update("BXH_DoiBong",cv,"MaDoiBong = ? and MaBXH > ? and MaBXH <= ?",new String[]{idClub + "", idTable + "", latestRankingTabbleId + ""});
                String strSQL = "update BXH_DoiBong set SoTranHoa = SoTranHoa + 1 where MaDoiBong = " + idClub + " and MaBXH > " + idTable +" and MaBXH <= " + latestRankingTabbleId;
                MainActivity.database.execSQL(strSQL);
            }
            else if( isDraw < 0){
                String strSQL = "update BXH_DoiBong set SoTranHoa = SoTranHoa - 1 where MaDoiBong = " + idClub + " and MaBXH > " + idTable +" and MaBXH <= " + latestRankingTabbleId;
                MainActivity.database.execSQL(strSQL);
            }
            if(isLose > 0){ // Nếu sửa trận đấu cũ thành 1 trận thắng
//                ContentValues cv1 = new ContentValues();
//                cv1.put("SoTranThua", "SoTranThua + 1");
//                MainActivity.database.update("BXH_DoiBong",cv,"MaDoiBong = ? and MaBXH > ? and MaBXH <= ?",new String[]{idClub + "", idTable + "", latestRankingTabbleId + ""});
                String strSQL = "update BXH_DoiBong set SoTranThua = SoTranThua + 1 where MaDoiBong = " + idClub + " and MaBXH > " + idTable +" and MaBXH <= " + latestRankingTabbleId;
                MainActivity.database.execSQL(strSQL);
            }
            else if( isLose < 0){
                String strSQL = "update BXH_DoiBong set SoTranThua = SoTranThua - 1 where MaDoiBong = " + idClub + " and MaBXH > " + idTable +" and MaBXH <= " + latestRankingTabbleId;
                MainActivity.database.execSQL(strSQL);
            }
            // Cập nhật bàn thắng bàn thua
            String strSQL = "update BXH_DoiBong set SoBanThang = SoBanThang + " + changeGoalCount + ", SoBanThua = SoBanThua + " + changeGoalConcededCount + " where MaDoiBong = " + idClub + " and MaBXH > " + idTable +" and MaBXH <= " + latestRankingTabbleId;
            MainActivity.database.execSQL(strSQL);
            // Cập nhật hiệu số
            strSQL = "update BXH_DoiBong set HieuSo = SoBanThang - SoBanThua where MaDoiBong = " + idClub + " and MaBXH > " + idTable +" and MaBXH <= " + latestRankingTabbleId;
            MainActivity.database.execSQL(strSQL);
        }
    }

    public static void sortRankingClubAfterMatchResult(String date, int priority){
        int idRankingTable = 0;
        Cursor cursor = MainActivity.database.rawQuery("select * from BXH where Ngay = \"" + date + "\";" , null);
        while (cursor.moveToNext()){
            idRankingTable = cursor.getInt(0);
        }
        cursor.close();
        ArrayList<RankingClub> rankingClubs = getRankingByDate(date);
        for(RankingClub rankingClub : rankingClubs){
            rankingClub.calPoint();
            rankingClub.calGoalDifference();
        }
        if(priority == 1)
            Collections.sort(rankingClubs, new RankingClub.SortbyPriority1());
        else if(priority == 2)
            Collections.sort(rankingClubs, new RankingClub.SortbyPriority2());
        else if(priority == 3)
            Collections.sort(rankingClubs, new RankingClub.SortbyPriority3());
        else if(priority == 4)
            Collections.sort(rankingClubs, new RankingClub.SortbyPriority4());
        // Set rank after sorting
        for(int i= 0; i< rankingClubs.size();i++){
            rankingClubs.get(i).setRank(i + 1);
        }
        for(RankingClub rankingClub : rankingClubs){
            System.out.println(rankingClub.getClub().getName() + " " + rankingClub.getPoints());
        }
        updateRankingTableByIdRankingTable(idRankingTable,rankingClubs);
    }

    public static void sortAllRankingTableAfterEditOldMatch(String oldDate, int priority){
        Cursor cursor = MainActivity.database.rawQuery("select * from BXH where Ngay = \""+ oldDate + "\"",null);
        int oldRankingTableId = 0;
        if(cursor.moveToNext()){
            oldRankingTableId = cursor.getInt(0);
        }
        cursor.close();
        int newRankingTableId = 0;
        cursor = MainActivity.database.rawQuery("select * from BXH order by MaBXH desc limit 1", null);
        if(cursor.moveToNext());
        {
            newRankingTableId = cursor.getInt(0);
        }
        cursor.close();
        for (int i = oldRankingTableId; i <= newRankingTableId;i++){
            cursor = MainActivity.database.rawQuery("select * from BXH where MaBXH = " + i, null);
            String date = "";
            if(cursor.moveToNext());
            {
                date = cursor.getString(1);
            }
            cursor.close();
            sortRankingClubAfterMatchResult(date,priority);
        }
    }

    public static void updateRankingTableAfterMatchResult(Schedule schedule, int priority) {
        String date = schedule.getDateTime();
        String [] arrayString = date.split(" ");
        date = arrayString[1];
        Cursor cursor = MainActivity.database.rawQuery("select * from BXH where Ngay = \"" + date + "\";" , null);

        if(cursor.moveToNext()){
            System.out.println(" BXH Cu");
            System.out.println(cursor.getString(1));
            int idRankingTable = cursor.getInt(0);
            int idClub1 = schedule.getClub1().getId();
            int idClub2 = schedule.getClub2().getId();
            if(schedule.getMatch().getScore() == null){ // Cập nhật tỉ số khi trận đấu chưa diễn ra
                // Cập nhật thông số Club1
                updateRankingClubAfterMatchResult(schedule,idClub1,idRankingTable,0);
                // Cập nhật thông số Club2
                updateRankingClubAfterMatchResult(schedule,idClub2,idRankingTable,0);
                // Sắp xếp lại bảng xếp hạng
            }
            else{ // Cập nhật tỉ số khi trận đấu đã diễn ra
                // Cập nhật thông số Club1
                updateRankingClubAfterMatchResult(schedule,idClub1,idRankingTable,1);
                // Cập nhật thông số Club2
                updateRankingClubAfterMatchResult(schedule,idClub2,idRankingTable,1);
                // Sắp xếp lại bảng xếp hạng
            }
            sortAllRankingTableAfterEditOldMatch(date,priority );
        }
        else{
            // TO DO
            System.out.println(" BXH Moi");
            insertNewRankingTableByIdRankingTable(schedule,date,priority);
        }
    }

    public static ArrayList<String> getAllRankReportDate(){
        ArrayList<String> dates = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = MainActivity.database.rawQuery("select * from BXH;", null);
            while (cursor.moveToNext()){
                dates.add(cursor.getString(1));
            }
        }
        finally {
            cursor.close();
        }
        return dates;
    }

    public static ArrayList<RankingClub> getRankingByDate(String date){
        Cursor cursor1 = null;
        int id = -1;
        try{
            cursor1 = MainActivity.database.rawQuery("select * from BXH where Ngay = \"" + date + "\";",null);
            while (cursor1.moveToNext()){
                id = cursor1.getInt(0);
            }
        }
        finally {
            cursor1.close();
        }
        Cursor cursor2 = null;
        ArrayList<RankingClub> rankingClubsByDate = new ArrayList<>();
        try {
            cursor2 = MainActivity.database.rawQuery("select * from BXH_DoiBong where MaBXH = " + id + " order by Hang asc;"  ,null);
            while (cursor2.moveToNext()){
                int idClub = cursor2.getInt(1);
                Club club = getClubById(idClub);
                int winCount = cursor2.getInt(2);
                int drawCount = cursor2.getInt(3);
                int loseCount = cursor2.getInt(4);
                int goalCount = cursor2.getInt(5);
                int goalConcededCount = cursor2.getInt(6);
                int rank = cursor2.getInt(7);
                int gd = cursor2.getInt(8);
                int played = cursor2.getInt(9);
                RankingClub rankingClub = new RankingClub(club,winCount,drawCount,loseCount,goalCount,goalConcededCount,played);
                rankingClub.setRank(rank);
                rankingClub.setGoalDifference(gd);
                rankingClubsByDate.add(rankingClub);
            }
        }
        finally {
            cursor2.close();
        }
        return rankingClubsByDate;
    }
    public static int whoWinInMatch(Match match){
        String score[] = match.getScore().split(" - ");
        int score1 = Integer.parseInt(score[0]);
        int score2 = Integer.parseInt(score[1]);
        if(score1 > score2) return 1;// đội nhà thắng
        else if(score2 > score1) return -1; // đội khách thắng
        else return 0;//hòa

    }
    public static int confrontationTwoClub(int id_club_1, int id_club_2){// so sánh đối đầu
        int idMatch1 = getIdMatchByTwoIdClub(id_club_1,id_club_2);
        int idMatch2 = getIdMatchByTwoIdClub(id_club_2,id_club_1);
        Match match1  = getMatchById(idMatch1);
        Match match2 = getMatchById(idMatch2);
        if (match1.getScore() == null && match2.getScore() == null)
            return 0;
        else if(match1.getScore()!=null && match2.getScore() == null){
            String score[] = match1.getScore().split(" - ");
            int score1 = Integer.parseInt(score[0]);
            int score2 = Integer.parseInt(score[1]);
            if(score1 > score2) return 1;
            else if(score2 > score1) return -1;
            else return 0;
        }
        else if(match1.getScore()==null && match2.getScore() != null){
            String score[] = match1.getScore().split(" - ");
            int score1 = Integer.parseInt(score[0]);
            int score2 = Integer.parseInt(score[1]);
            if(score1 > score2) return -1;
            else if(score2 > score1) return 1;
            else return 0;
        }
        else{
            int winMatch1 = whoWinInMatch(match1);
            int winMatch2 = whoWinInMatch(match2);
            if((winMatch1 == 1 && winMatch2 == -1)|| (winMatch1 == 1 && winMatch2 == 0))return 1;// đội có id1 thắng
            else if((winMatch1 == -1 && winMatch2 == 1)|| (winMatch1 == -1 && winMatch2 == 0))return -1;// đội có id2 thắng
            else if((winMatch1 == 1 && winMatch2 == 1)||(winMatch1 == -1 && winMatch2 == -1)){
                String score[] = match1.getScore().split(" - ");
                int score1 = Integer.parseInt(score[0]);
                int score2 = Integer.parseInt(score[1]);

                String scoreMatch2[] = match2.getScore().split(" - ");
                score1 +=Integer.parseInt(scoreMatch2[1]);// tổng bàn thắng
                score2 +=Integer.parseInt(scoreMatch2[0]);// tổng bàn thắng

                //so sanh ban thang 2 doi dau
                if(score1 > score2 )return 1;
                else if(score2 >score1) return   -1;
                else return 0;

            }
        }
        return 0;
    }
    public static int getTotalGoalAwayMatch(int id_club){
        Cursor cursor = MainActivity.database.
                rawQuery("select * from LichThiDau where MaDoiKhach = ?",
                        new String[] {String.valueOf(id_club)});
        ObservableArrayList<Integer> id_schedule = new ObservableArrayList<>();
        while(cursor.moveToNext()){
            id_schedule.add(cursor.getInt(0));
        }
        cursor.close();
        ObservableArrayList<String> scores = new ObservableArrayList<>();
        for(int i = 0; i<id_schedule.size(); i++){
            Cursor cursor1 = MainActivity.database.
                    rawQuery("select * from TranDau where MaTranDau = ? and TiSo not NULL",
                            new String[] {String.valueOf(id_schedule.get(i))});
            while(cursor1.moveToNext()){
                scores.add(cursor1.getString(1));
            }
            cursor1.close();
        }
        int total = 0;
        for(int i = 0 ; i < scores.size(); i++){
            total += Integer.parseInt( scores.get(i).split(" - ")[1]);
        }
        return total;
    }
    public static int getTotalGoalHomeMatch(int id_club){
        Cursor cursor = MainActivity.database.
                rawQuery("select * from LichThiDau where MaDoiNha = ?",
                        new String[] {String.valueOf(id_club)});
        ObservableArrayList<Integer> id_schedule = new ObservableArrayList<>();
        while(cursor.moveToNext()){
            id_schedule.add(cursor.getInt(0));
        }
        cursor.close();
        ObservableArrayList<String> scores = new ObservableArrayList<>();
        for(int i = 0; i<id_schedule.size(); i++){
            Cursor cursor1 = MainActivity.database.
                    rawQuery("select * from TranDau where MaTranDau = ? and TiSo not NULL",
                            new String[] {String.valueOf(id_schedule.get(i))});
            while(cursor1.moveToNext()){
                scores.add(cursor1.getString(1));
            }
            cursor1.close();
        }
        int total = 0;
        for(int i = 0 ; i < scores.size(); i++){
            total += Integer.parseInt( scores.get(i).split(" - ")[0]);
        }
        return total;
    }

    public static boolean IsStartLeague(){
        Cursor cursor = MainActivity.database.
                rawQuery("select * from BatDauGiaiDau where BatDau = ?",
                        new String[] {String.valueOf(0)});
        while(cursor.moveToNext()){
            return false;
        }
        cursor.close();
        return true;
    }
    public static ObservableArrayList<Integer> get2IdClubByIdSchedule(int id){
        ObservableArrayList<Integer> clubs = new ObservableArrayList<>();
        Cursor cursor = null;
        try{
            cursor = MainActivity.database.
                    rawQuery("select * from LichThiDau where MaLichThiDau = ?",
                            new String[] {String.valueOf(id)});
            while(cursor.moveToNext()){
                clubs.add(cursor.getInt(2));
                clubs.add(cursor.getInt(3));
                return clubs;
            }
        }
        finally {
            cursor.close();
        }
        return null;
    }
    public static Match getMatchById(int id){
        Cursor cursor= null;
        try{
            cursor = MainActivity.database.
                    rawQuery("select * from TranDau where MaTranDau = ?",
                            new String[] {String.valueOf(id)});
            while(cursor.moveToNext()){
                String score = cursor.getString(1);
                int MaLichThiDau = cursor.getInt(2);
                ObservableArrayList<Integer> clubs = get2IdClubByIdSchedule(MaLichThiDau);
                ObservableArrayList<Goal> listGoalHome = getListGoal(id,clubs.get(0));
                ObservableArrayList<Goal> listGoalAway = getListGoal(id,clubs.get(1));
                Match match = new Match(id,score,listGoalHome,listGoalAway);
                return match;
            }
        }
        finally {
            cursor.close();
        }
        return null;
    }
    public static ArrayList<Schedule> getMatchByRound(int round){
        ArrayList<Schedule> schedules = new ArrayList<>();
        Schedule schedule;
        Cursor cursor = MainActivity.database.
                rawQuery("select * from LichThiDau where VongDau = ?",
                        new String[] {String.valueOf(round)});
        while(cursor.moveToNext()) {
            int MaLichThiDau = cursor.getInt(0);
            int DoiNha = cursor.getInt(2);
            int DoiKhach = cursor.getInt(3);
            String date = cursor.getString(4);
            Club club1 = getClubById(DoiNha);
            Club club2 = getClubById(DoiKhach);
            int id_match = getIdMatchBySchedule(MaLichThiDau);
            Match match = getMatchById(id_match);
            schedule = new Schedule(club1.getStadium(), date, round, club1, club2, match);
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
    public static void deleteGoalById(int id){
        int res = MainActivity.database.delete("BanThang","MaBanThang=?",new String[]{id+""});

        System.out.println("res: "  + res);
    }

    public static Player findPlayerById(int id){
        Cursor cursor = MainActivity.database.query("CauThu",null,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            if(id == cursor.getInt(0)){
                return new Player(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getString(4));
            }
        }
        return null;
    }

    public static Player findPlayerByName(String name){
        Cursor cursor = MainActivity.database.query("CauThu",null,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            if(name.equals(cursor.getString(1))){
                return new Player(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getString(4));
            }
        }
        return null;
    }
    public static String getTypeById(int id){
        Cursor cursor = MainActivity.database.query("LoaiCauThu",null,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            if(id == cursor.getInt(0)){
                return cursor.getString(1);
            }
        }
        return null;
    }
    public static String getDateByIdSchedule(int id){
        Cursor cursor = MainActivity.database.query("LichThiDau",null,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            if(id == cursor.getInt(0)){
                return cursor.getString(4);
            }
        }
        return null;
    }
    public static ObservableArrayList<Goal> getAllGoalNotOwnByDate(String date){
        ObservableArrayList<Goal> goals = new ObservableArrayList<>();
        Cursor cursor = MainActivity.database.
                rawQuery("select *  " +
                                "from BanThang where MaLoaiBanThang = 0 or MaLoaiBanThang = 1" ,null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            int id_match = cursor.getInt(1);
            int id_player = cursor.getInt(2);
            int type = cursor.getInt(3);
            int time = cursor.getInt(4);
            int id_club = cursor.getInt(5);
            int id_schedule = getIdScheduleByIdMatch(id_match);
            String dateScore = getDateByIdSchedule(id_schedule).split(" ")[1];
            Player player = getPlayerById(id_player);
            Goal goal = new Goal(id,player,time,type,id_club,id_match);
            //so sanh ngay
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date1,date2;
            try {
                date1 = sdf.parse(date);
                date2 = sdf.parse(dateScore);
                //System.out.println(date1.after(date2) || date1.equals(date2));
                if(date1.after(date2) || date1.equals(date2)){
                    goals.add(goal);
                    //System.out.println(player.getName() + " -  " + dateScore)  ;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return goals;
    }
    public static ObservableArrayList<Regulation> getRegulations(){
        ObservableArrayList<Regulation> regulations = new ObservableArrayList<>();
        Cursor cursor = MainActivity.database.
                rawQuery("select * from QuyDinh",null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int attr = cursor.getInt(3);
            int value = cursor.getInt(3);
            regulations.add(new Regulation(id,name,value,attr,""));
        }
        return regulations;
    }
    public static void deletePlayerById(int id){
        int res = MainActivity.database.delete("CauThu","MaCauThu=?",new String[]{id+""});
    }
    public  static void changeRelutationById(int id, int value){
        ContentValues cv = new ContentValues();
        cv.put("GiaTri",value);
        MainActivity.database.update("QuyDinh", cv, "MaQuyDinh = ?", new String[]{id + ""});
    }
}
