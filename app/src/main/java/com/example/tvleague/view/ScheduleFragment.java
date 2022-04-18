package com.example.tvleague.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.tvleague.R;
import com.example.tvleague.databinding.FragmentScheduleBinding;
import com.example.tvleague.model.Club;
import com.example.tvleague.model.DatabaseRoute;
import com.example.tvleague.model.Match;
import com.example.tvleague.model.Player;
import com.example.tvleague.model.Schedule;
import com.example.tvleague.model.ScheduleAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentScheduleBinding binding;
    private ArrayList<Schedule> listSchedules;
    private ScheduleAdapter scheduleAdapter;
    private ArrayList<String> round;
    int const_round = 1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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
        listSchedules = new ArrayList<>();
        listSchedules = DatabaseRoute.getMatchByRound( 1);
        round = new ArrayList<>();
        ArrayList<String> first_leg = new ArrayList<>();
        ArrayList<String> second_leg = new ArrayList<>();
//
        ArrayList<Club> clubs = DatabaseRoute.getAllClub();
        int sizeOfClub = clubs.size();
        for (int i = 0; i<sizeOfClub - 1; i++){
            int round_in_first_leg = i + 1;
            int round_in_second_leg = i + sizeOfClub;
            first_leg.add("Vòng " + round_in_first_leg);
            second_leg.add("Vòng " + round_in_second_leg);
        }
        round.addAll(first_leg);
        round.addAll(second_leg);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.binding = FragmentScheduleBinding.inflate(inflater,container,false);
        scheduleAdapter = new ScheduleAdapter(getContext(),listSchedules);
        binding.rvMatches.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        binding.rvMatches.setAdapter(scheduleAdapter);
        binding.rvMatches.addItemDecoration(new DividerItemDecoration(this.getContext(),DividerItemDecoration.VERTICAL));
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,round);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.roundSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                const_round = i + 1;
                listSchedules = DatabaseRoute.getMatchByRound(i + 1);
                scheduleAdapter.setScheduleList(listSchedules);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.roundSpinner.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        listSchedules = DatabaseRoute.getMatchByRound(const_round );
        scheduleAdapter.setScheduleList(listSchedules);
    }
}