package com.example.tvleague.model;

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
    // Ưu tiên 1: Điếm số -> Hiệu số bàn thắng -> Kết quả đối đầu
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
                    return 0;
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
        System.out.println("Đã tính GD");
        this.goalDifference = this.goalCount - this.goalConcededCount;
        System.out.println(this.goalDifference);
    }

    public void calPoint(){
        System.out.println("Đã tính Points");
        this.points = this.winCount * 3 + this.drawCount;
        System.out.println(this.points);
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
}
