package com.example.tvleague.view;

import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.tvleague.R;
import com.example.tvleague.databinding.FragmentRankingBinding;
import com.example.tvleague.model.Club;
import com.example.tvleague.model.DatabaseRoute;
import com.example.tvleague.model.Goal;
import com.example.tvleague.model.RankingClub;
import com.example.tvleague.model.RankingClubAdapter;
import com.example.tvleague.model.TopScore;
import com.example.tvleague.model.TopScoreAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RankingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentRankingBinding binding;
    private ArrayList<RankingClub> rankingClubs;
    private ObservableArrayList<TopScore> topScores;
    private ArrayList<String> dates;
    private RankingClubAdapter rankingClubAdapter;
    private TopScoreAdapter topScoreAdapter;
    int date_index;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RankingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RankingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RankingFragment newInstance(String param1, String param2) {
        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dates = DatabaseRoute.getAllRankReportDate();

        // Ngày báo cáo xếp hạng được chọn hiện tại
        date_index = dates.size() -1;
        for (String date : dates){
            System.out.println(date);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.binding = FragmentRankingBinding.inflate(inflater,container,false);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,dates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.roundSpinner.setAdapter(adapter);
        binding.roundSpinner.setSelection(dates.size() -1);
        binding.roundSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                date_index = i;
                rankingClubs = DatabaseRoute.getRankingByDate(dates.get(i));
                rankingClubAdapter.setRankingClubs(rankingClubs);

                topScores = getTopScoreByDate(dates.get(i));
                topScoreAdapter.setTopScore(topScores);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        rankingClubAdapter = new RankingClubAdapter(rankingClubs);
        binding.rvClubRanking.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        binding.rvClubRanking.setAdapter(rankingClubAdapter);
        binding.rvClubRanking.addItemDecoration(new DividerItemDecoration(this.getContext(),DividerItemDecoration.VERTICAL));

        //vua pha luoi
        topScoreAdapter = new TopScoreAdapter(topScores);
        binding.rvScoreRanking.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        binding.rvScoreRanking.setAdapter(topScoreAdapter);
        binding.rvScoreRanking.addItemDecoration(new DividerItemDecoration(this.getContext(),DividerItemDecoration.VERTICAL));
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.roundSpinner.setSelection(dates.size() -1);
        rankingClubs = DatabaseRoute.getRankingByDate(dates.get(date_index));
        rankingClubAdapter.setRankingClubs(rankingClubs);

        topScores = getTopScoreByDate(dates.get(date_index));
        topScoreAdapter.setTopScore(topScores);
    }
    public static boolean isIncludeInList(String name, ObservableArrayList<Goal> goals){
        for (int i = 0;i<goals.size(); i++){
            if(goals.get(i).getPlayer().getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    public static ObservableArrayList<Goal> getUniquePlayerName(ObservableArrayList<Goal> listGoal){
        ObservableArrayList<Goal> goals = new ObservableArrayList<>();
        //System.out.println("Size " + listGoal.size());
        for(int i = 0;i< listGoal.size();i++){
            if(isIncludeInList(listGoal.get(i).getPlayer().getName(),goals) == false){
                goals.add(listGoal.get(i));
            }
        }
        return goals;
    }
    private static int getQuantityGoalOfPlayer(String name, ObservableArrayList<Goal> listGoal){
        int result = 0;
        for(int i = 0; i < listGoal.size(); i++){
            if(name.equals(listGoal.get(i).getPlayer().getName())) result++;
        }
        return  result;
    }
    public static ObservableArrayList<TopScore> sortTopScoreByQuantity(ObservableArrayList<TopScore> topScores){
        Collections.sort(topScores, new Comparator<TopScore>() {
            @Override
            public int compare(TopScore player1, TopScore player2) {
                if (player1.getQuantity_score() < player2.getQuantity_score()) {
                    return 1;
                } else {
                    if (player1.getQuantity_score() == player2.getQuantity_score()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            }
        });
        return  topScores;
    }
    public static ObservableArrayList<TopScore> getTopScoreByDate(String date){
        ObservableArrayList<TopScore> topScores = new ObservableArrayList<>();
        ObservableArrayList<Goal> validListGoal = DatabaseRoute.getAllGoalNotOwnByDate(date);
        ObservableArrayList<Goal> uniquePlayer = getUniquePlayerName(validListGoal);
        for(int i = 0 ; i < uniquePlayer.size();i++){
            int quan = getQuantityGoalOfPlayer(uniquePlayer.get(i).getPlayer().getName(),validListGoal);
            TopScore player = new TopScore(-1,uniquePlayer.get(i).getPlayer(),quan);
            topScores.add(player);
        }
        topScores = sortTopScoreByQuantity(topScores);
        for(int i = 0 ; i < uniquePlayer.size();i++){
            topScores.get(i).setIndex(i + 1);
        }
        return  topScores;
    }
}