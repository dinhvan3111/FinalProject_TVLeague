package com.example.tvleague.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tvleague.R;
import com.example.tvleague.databinding.FragmentRegulationBinding;
import com.example.tvleague.model.DatabaseRoute;
import com.example.tvleague.model.LeagueRegulations;
import com.example.tvleague.model.Regulation;
import com.example.tvleague.model.RegulationAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegulationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegulationFragment extends Fragment {
    private FragmentRegulationBinding binding;
    private ArrayList<Regulation> regulationList;
    private RegulationAdapter regulationAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegulationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegulationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegulationFragment newInstance(String param1, String param2) {
        RegulationFragment fragment = new RegulationFragment();
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
//        // Lấy dữ liệu cho regulationList

        regulationList = DatabaseRoute.getRegulations();

//        regulationList = new ArrayList<>();
//        regulationList.add(new Regulation(-1,"Độ tuổi tối tối thiểu", LeagueRegulations.MIN_AGE,"Độ tuổi tối thiểu được phép tham gia một đội"));
//        regulationList.add(new Regulation(-1,"Độ tuổi tối tối đa",LeagueRegulations.MAX_AGE,"Độ tuổi tối đa được phép tham gia một đội"));
//        regulationList.add(new Regulation(-1,"Số cầu thủ tối đa",LeagueRegulations.MAX_PLAYERS,""));
//        regulationList.add(new Regulation(-1,"Số cầu thủ tối thiểu",LeagueRegulations.MIN_PLAYERS,"Số lượng cầu thủ tối thiểu được phép tham gia một đội"));
//        regulationList.add(new Regulation(-1,"Số cầu thủ nước ngoài tối đa",LeagueRegulations.MAX_FOREIGN_PLAYERS,""));
//        regulationList.add(new Regulation(-1,"Phút tối đa",LeagueRegulations.MAX_SCORE_TIME,"Thời gian ghi bàn tối đa trong trận đấu."));
//        regulationList.add(new Regulation(-1,"Điểm thắng",LeagueRegulations.WIN_POINT,""));
//        regulationList.add(new Regulation(-1,"Điểm hòa",LeagueRegulations.DRAW_POINT,""));
//        regulationList.add(new Regulation(-1,"Điểm thua",LeagueRegulations.LOSE_POINT,""));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentRegulationBinding.inflate(inflater,container,false);
        regulationAdapter = new RegulationAdapter(regulationList);
        binding.rvLeagueRegulation.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        binding.rvLeagueRegulation.setAdapter(regulationAdapter);
        binding.rvLeagueRegulation.addItemDecoration(new DividerItemDecoration(this.getContext(),DividerItemDecoration.VERTICAL));
        return binding.getRoot();
    }
}