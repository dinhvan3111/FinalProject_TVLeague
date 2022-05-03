package com.example.tvleague.model;

import android.provider.ContactsContract;

import com.example.tvleague.view.MainActivity;

import java.util.Comparator;

public class RankingClub {
    private Club club;
    private int rank;
    private int winCount;
    private int drawCount;
    private int loseCount;
    private int goalCount;
    private int points;
    private int goalConcededCount;
    private int goalDifference;
    private int round;

    public RankingClub(Club club, int winCount, int drawCount, int loseCount, int goalCount, int goalConcededCount, int round) {
        this.club = club;
        this.winCount = winCount;
        this.drawCount = drawCount;
        this.loseCount = loseCount;
        this.goalCount = goalCount;
        this.goalConcededCount = goalConcededCount;
        this.round = round;
        this.goalDifference = this.goalCount - this.goalConcededCount;
    }
    // Ưu tiên 1: Điếm số -> Hiệu số bàn thắng -> Kết quả đối đầu -> bàn thắng
    public static class SortbyPriority1 implements Comparator<RankingClub>{

        @Override
        public int compare(RankingClub rankingClub1, RankingClub rankingClub2) {
            if(rankingClub1.points > rankingClub2.points){ // Hơn điểm
                return -1;
            }
            else if (rankingClub1.points < rankingClub2.points){ // Thua điểm
                return 1;
            }
            else{ // Bằng điểm
                if(rankingClub1.goalDifference > rankingClub2.goalDifference){ // Hơn hiệu số
                    return -1;
                }
                else if(rankingClub1.goalDifference < rankingClub2.goalDifference){ // Thua hiệu số
                    return 1;
                }
                else{ // Bằng hiệu số
                    // Điều kiện kết quả đối đầu
                    int confrontation = DatabaseRoute.confrontationTwoClub(rankingClub1.getClub().getId(),
                            rankingClub2.getClub().getId());
                    if(confrontation == 1)//club 1 hơn đối đầu
                        return -1;
                    else if(confrontation == -1)//club 2 hơn đối đầu
                        return 1;
                    else if(confrontation == 0){// bằng đối đầu
                        // Điều kiện tổng bàn thắng
                        return -1 * RankingClub.compareTotalGoalOfTwoClub(rankingClub1.getClub().getId(),
                                rankingClub2.getClub().getId());
                    }
                }
            }
            return 0;
        }

    }
    // Ưu tiên 2: Điếm số -> Đối đầu -> Hiệu số -> bàn thắng
    public static class SortbyPriority2 implements Comparator<RankingClub>{

        @Override
        public int compare(RankingClub rankingClub1, RankingClub rankingClub2) {
            if(rankingClub1.points > rankingClub2.points){ // Hơn điểm
                return -1;
            }
            else if (rankingClub1.points < rankingClub2.points){ // Thua điểm
                return 1;
            }
            else{ // Bằng điểm
                int confrontation = DatabaseRoute.confrontationTwoClub(rankingClub1.getClub().getId(),
                        rankingClub2.getClub().getId());
                if(confrontation == 1)//club 1 hơn đối đầu
                    return -1;
                else if(confrontation == -1)//club 2 hơn đối đầu
                    return 1;
                else if(confrontation == 0){// bằng đối đầu
                    if(rankingClub1.goalDifference > rankingClub2.goalDifference){ // Hơn hiệu số
                        return -1;
                    }
                    else if(rankingClub1.goalDifference < rankingClub2.goalDifference){ // Thua hiệu số
                        return 1;
                    }
                    else{ // Bằng hiệu số
                        // Điều kiện tổng bàn thắng
                        return -1 * RankingClub.compareTotalGoalOfTwoClub(rankingClub1.getClub().getId(),
                                rankingClub2.getClub().getId());
                    }
                }


            }
            return 0;
        }

    }

    // Ưu tiên 3: Điếm số ->  Hiệu số -> bàn thắng -> Đối đầu
    public static class SortbyPriority3 implements Comparator<RankingClub>{

        @Override
        public int compare(RankingClub rankingClub1, RankingClub rankingClub2) {
            if(rankingClub1.points > rankingClub2.points){ // Hơn điểm
                return -1;
            }
            else if (rankingClub1.points < rankingClub2.points){ // Thua điểm
                return 1;
            }
            else{ // Bằng điểm
                if(rankingClub1.goalDifference > rankingClub2.goalDifference){ // Hơn hiệu số
                    return -1;
                }
                else if(rankingClub1.goalDifference < rankingClub2.goalDifference){ // Thua hiệu số
                    return 1;
                }
                else{ // Bằng hiệu số
                    // Điều kiện bàn thắng
                    int compareScore = compareTotalGoalOfTwoClub(rankingClub1.getClub().getId(),
                            rankingClub2.getClub().getId());
                    if(compareScore == 1) return  -1;
                    else if(compareScore == -1) return 1;
                    else{//bằng tổng bàn thắng
                        return -1 * DatabaseRoute.confrontationTwoClub(rankingClub1.getClub().getId(),
                                rankingClub2.getClub().getId());
                    }
                }
            }
        }

    }

    // Ưu tiên 4: Điếm số ->  Bàn thắng -> Hiệu số -> Đối đầu
    public static class SortbyPriority4 implements Comparator<RankingClub>{

        @Override
        public int compare(RankingClub rankingClub1, RankingClub rankingClub2) {
            if(rankingClub1.points > rankingClub2.points){ // Hơn điểm
                return -1;
            }
            else if (rankingClub1.points < rankingClub2.points){ // Thua điểm
                return 1;
            }
            else{ // Bằng điểm
                //So sánh bàn thắng
                int compareScore = compareTotalGoalOfTwoClub(rankingClub1.getClub().getId(),
                        rankingClub2.getClub().getId());
                if(compareScore == 1) return  -1;
                else if(compareScore == -1) return 1;
                else{//bằng tổng bàn thắng
                    //So sánh hiệu số
                    if(rankingClub1.goalDifference > rankingClub2.goalDifference){ // Hơn hiệu số
                        return -1;
                    }
                    else if(rankingClub1.goalDifference < rankingClub2.goalDifference){ // Thua hiệu số
                        return 1;
                    }
                    else{ // Bằng hiệu số
                        //so sánh đối đầu
                        return -1 * DatabaseRoute.confrontationTwoClub(rankingClub1.getClub().getId(),
                                rankingClub2.getClub().getId());
                    }
                }
            }
        }

    }
    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    public int getDrawCount() {
        return drawCount;
    }

    public void setDrawCount(int drawCount) {
        this.drawCount = drawCount;
    }

    public int getLoseCount() {
        return loseCount;
    }

    public void setLoseCount(int loseCount) {
        this.loseCount = loseCount;
    }

    public int getGoalCount() {
        return goalCount;
    }

    public void setGoalCount(int goalCount) {
        this.goalCount = goalCount;
    }

    public int getGoalConcededCount() {
        return goalConcededCount;
    }

    public void setGoalConcededCount(int goalConcededCount) {
        this.goalConcededCount = goalConcededCount;
    }

    public int getGoalDifference() {
        return goalDifference;
    }

    public void setGoalDifference(int goalDifference) {
        this.goalDifference = goalDifference;
    }

    public void calGoalDifference(){
        this.goalDifference = this.goalCount - this.goalConcededCount;
    }

    public void calPoint(){
        System.out.println("Diem thang: " + MainActivity.regulations.getWIN_POINT());
        this.points = this.winCount * MainActivity.regulations.getWIN_POINT()
                + this.drawCount * MainActivity.regulations.getDRAW_POINT()
                + this.loseCount * MainActivity.regulations.getLOSE_POINT();
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }
    public static   int compareTotalGoalOfTwoClub(int id_club_1, int id_club_2){
        int totalScoreClub1 = DatabaseRoute.getTotalGoalHomeMatch(id_club_1)
                + DatabaseRoute.getTotalGoalAwayMatch(id_club_1);
        int totalScoreClub2 = DatabaseRoute.getTotalGoalHomeMatch(id_club_2)
                + DatabaseRoute.getTotalGoalAwayMatch(id_club_2);
        if(totalScoreClub1  > totalScoreClub2) return 1;
        else if(totalScoreClub1 < totalScoreClub2) return -1;
        else return 0;
    }
}
