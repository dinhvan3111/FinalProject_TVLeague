package com.example.tvleague.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tvleague.R;
import com.example.tvleague.databinding.FragmentScheduleBinding;
import com.example.tvleague.model.Club;
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
        Club club1 = new Club(1,"Manchester United", "Old Trafold", 1,null);
        Club club2 = new Club(1,"Manchester City", "Etihad", 1,null);
        listSchedules = new ArrayList<>();
        listSchedules.add(new Schedule(club1.getStadium(), "Sat, Apr 16 21:00", 1, club1,club2));
        listSchedules.add(new Schedule(club1.getStadium(), "Sat, Apr 16 22:00", 1, club1,club2));
        listSchedules.add(new Schedule(club1.getStadium(), "Sat, Apr 16 23:00", 1, club1,club2));
        listSchedules.add(new Schedule(club1.getStadium(), "Sat, Apr 16 00:00", 1, club1,club2));
        listSchedules.add(new Schedule(club1.getStadium(), "Sat, Apr 16 01:00", 1, club1,club2));
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
        return binding.getRoot();
    }
}