package com.example.tvleague.model;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvleague.R;
import com.example.tvleague.databinding.ItemGoalRowBinding;
import com.example.tvleague.databinding.ItemScheduleRowBinding;
import com.example.tvleague.databinding.LayoutAddGoalBinding;
import com.example.tvleague.view.MatchDetail;

import java.util.ArrayList;
import java.util.Observable;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {
    private ObservableArrayList<Goal> listGoal;
    private Context context;
    private static GoalRecyclerViewClickListener itemListener;

    public GoalAdapter(ObservableArrayList<Goal> listGoal, Context context, GoalRecyclerViewClickListener itemListener) {
        this.listGoal = listGoal;
        this.context = context;
        this.itemListener = itemListener;
    }
    public void setGoalList(  ObservableArrayList<Goal> Goal){
        this.listGoal = Goal;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemGoalRowBinding binding = ItemGoalRowBinding.inflate(inflater,parent,false);
        return new GoalAdapter.GoalViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        Goal goal = listGoal.get(position);
        ItemGoalRowBinding binding = holder.getBinding();
        binding.tvPlayerName.setText(goal.getPlayer().getName());
        binding.tvGoalTime.setText(goal.getTime() + "'");
        if(goal.getType() == 0){ // bàn thắng thường
            binding.tvGoalType.setText("");
        }
        if(goal.getType() == 1){ // Penalty
            System.out.println("penalty");
            binding.tvGoalType.setText("(P)");
        }
        if(goal.getType() == 2){ // Phản lưới nhà
            binding.tvGoalType.setText("(OG)");
        }

        // xoá bàn thắng
        binding.itemGoalRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                int pos = holder.getAdapterPosition();
                delete(pos);
                return false;
            }
        });
        //chỉnh sửa bàn thắng.
        binding.itemGoalRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                LayoutAddGoalBinding layoutAddGoalBinding = LayoutAddGoalBinding.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                Dialog dialog = new Dialog(binding.getRoot().getContext());
                layoutAddGoalBinding.btnAddPlayer.setText("Cập nhật");
                layoutAddGoalBinding.tvAddGoal.setText("Cập nhật bàn thắng");
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(layoutAddGoalBinding.getRoot());
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                //chọn Đội
                ArrayList<String> NameTwoClub = new ArrayList<>();
                NameTwoClub.add(MatchDetail.schedule.getClub1().getName());
                NameTwoClub.add(MatchDetail.schedule.getClub2().getName());
                ArrayAdapter adapter = new ArrayAdapter(layoutAddGoalBinding.getRoot().getContext(),
                        android.R.layout.simple_list_item_1,NameTwoClub);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                layoutAddGoalBinding.spinnerTeamGoal.setAdapter(adapter);
                int posOfClub = findPosClubByName(listGoal.get(pos),NameTwoClub);
                layoutAddGoalBinding.spinnerTeamGoal.setSelection(posOfClub);
                final ArrayAdapter[] adapterPlayers = new ArrayAdapter[1];
                layoutAddGoalBinding.spinnerTeamGoal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        MatchDetail.NameOfClub.clear();
                        MatchDetail.NameOfClub.add(NameTwoClub.get(i));
                        MatchDetail.NameOfClub.add(NameTwoClub.get(1 - i));
                        MatchDetail.NameOfPlayers = DatabaseRoute.getPlayersByClubName(NameTwoClub.get(i));
                        adapterPlayers[0] =  new ArrayAdapter(layoutAddGoalBinding.getRoot().getContext(),
                                android.R.layout.simple_list_item_1,MatchDetail.NameOfPlayers);
                        adapterPlayers[0].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        layoutAddGoalBinding.spinnerPlayerGoal.setAdapter(adapterPlayers[0]);
                        layoutAddGoalBinding.spinnerPlayerGoal.
                                setSelection(MatchDetail.NameOfPlayers.indexOf(listGoal.get(pos).getPlayer().getName()));

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                //chọn cầu thủ
                layoutAddGoalBinding.spinnerPlayerGoal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        MatchDetail.choosePlayer.clear();
                        MatchDetail.choosePlayer.add( MatchDetail.NameOfPlayers.get(i));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                layoutAddGoalBinding.edtTime.setText(listGoal.get(pos).getTime() + "");
                // Chon phút ghi bàn
                layoutAddGoalBinding.iconTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MatchDetail matchDetail = new MatchDetail();
                        matchDetail.show(layoutAddGoalBinding,listGoal.get(pos).getTime());
                    }
                });


                if(listGoal.get(pos).getType() == 0){
                    layoutAddGoalBinding.normalGoal.setChecked(true);
                    layoutAddGoalBinding.penaltyKick.setChecked(false);
                    layoutAddGoalBinding.ownGoal.setChecked(false);
                }
                else if(listGoal.get(pos).getType() == 1){
                    layoutAddGoalBinding.normalGoal.setChecked(false);
                    layoutAddGoalBinding.penaltyKick.setChecked(true);
                    layoutAddGoalBinding.ownGoal.setChecked(false);
                }
                else if(listGoal.get(pos).getType() == 2){
                    layoutAddGoalBinding.normalGoal.setChecked(false);
                    layoutAddGoalBinding.penaltyKick.setChecked(false);
                    layoutAddGoalBinding.ownGoal.setChecked(true);
                }
                //Chọn loại bàn thắng
                layoutAddGoalBinding.radioTypeGoal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        MatchDetail.typeGoal.clear();
                        switch (i){
                            case R.id.normalGoal:
                                MatchDetail.typeGoal.clear();
                                MatchDetail.typeGoal.add(0);
                                break;
                            case R.id.penaltyKick:
                                MatchDetail.typeGoal.clear();
                                MatchDetail.typeGoal.add(1);
                                break;
                            case R.id.ownGoal:
                                MatchDetail.typeGoal.clear();
                                MatchDetail.typeGoal.add(2);
                                break;
                            default:
                                MatchDetail.typeGoal.clear();
                                MatchDetail.typeGoal.add(0);
                                break;
                        }
                    }
                });

                layoutAddGoalBinding.btnAddPlayer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String club = MatchDetail.NameOfClub.get(0);
                        String clubOther = MatchDetail.NameOfClub.get(1);//dùng khi là bàn thắng phản lưới, ghi nhận cho đọi kia.
                        String player_name =  MatchDetail.choosePlayer.get(0);
                        Player player = DatabaseRoute.getPlayerByName(player_name);
                        int id_club = DatabaseRoute.getIdClubByName(club);
                        int id_club_other = DatabaseRoute.getIdClubByName(clubOther);
                        int type_goal = 0;
//                        if( MatchDetail.typeGoal.size() != 0){
//                            type_goal =  MatchDetail.typeGoal.get(0);
//                        }
                        if(MatchDetail.typeGoal.size() != 0){
                            type_goal = MatchDetail.typeGoal.get(MatchDetail.typeGoal.size() - 1);
                        }
                        MatchDetail.typeGoal.clear();
                        Goal goal;
                        int time = Integer.parseInt(layoutAddGoalBinding.edtTime.getText().toString());
                        if(type_goal!=2){//không phản lưới nhà
                            goal = new Goal(listGoal.get(pos).getId(),player,time,type_goal,id_club,MatchDetail.id_match);
                        }
                        else{//phản lưới nhà
                            goal = new Goal(listGoal.get(pos).getId(),player,time,type_goal,id_club_other,MatchDetail.id_match);
                        }
                        DatabaseRoute.updateGoal(goal);
                        int id_club1 = DatabaseRoute.getIdClubByName(NameTwoClub.get(0));
                        int id_club2 = DatabaseRoute.getIdClubByName(NameTwoClub.get(1));
                        ObservableArrayList<Goal> goal1 = DatabaseRoute.getListGoal(MatchDetail.id_match,id_club1);
                        ObservableArrayList<Goal> goal2 = DatabaseRoute.getListGoal(MatchDetail.id_match,id_club2);
                        String score = goal1.size() + " - " + goal2.size();
                        int id_schedule = DatabaseRoute.getIdScheduleByTwoIdClub(id_club1,id_club2);
                        DatabaseRoute.updateScore(id_schedule,score);
                        MatchDetail.binding.tvScore.setText(score);
                        MatchDetail.club1GoalAdapter.setGoalList( goal1);
                        MatchDetail.club2GoalAdapter.setGoalList(goal2);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listGoal != null){
            return listGoal.size();
        }
        else
            return 0;
    }

    public class GoalViewHolder extends RecyclerView.ViewHolder{
        private ItemGoalRowBinding binding;
        public GoalViewHolder(@NonNull ItemGoalRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public ItemGoalRowBinding getBinding(){
            return binding;
        }
    }
    public int findPosClubByName(Goal goal, ArrayList<String> NameTwoClub){
        for(int i = 0; i <NameTwoClub.size();i++){
            int id_club = goal.getMaDoiBong();
            String name_club = DatabaseRoute.getClubById(id_club).getName();
            if(name_club.equals(NameTwoClub.get(i))){
                if (goal.getType() == 2){
                    return 1 - i;
                }
                return i;
            }
        }
        return -1;
    }
    public void delete(int pos){
        AlertDialog.Builder b = new AlertDialog.Builder(MatchDetail.binding.getRoot().getContext());
        b.setTitle("Xóa bàn thắng");
        b.setMessage("Bạn có chắc chắn xóa bàn thắng này không?");
        b.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int id_match = listGoal.get(pos).getMaTranDau();
                DatabaseRoute.deleteGoalById(listGoal.get(pos).getId());
                listGoal.remove(pos);
                notifyDataSetChanged();
                int id_home = MatchDetail.schedule.getClub1().getId();
                int id_away = MatchDetail.schedule.getClub2().getId();
                ArrayList<Goal> goal1 = DatabaseRoute.getListGoal(id_match,id_home);
                ArrayList<Goal> goal2 = DatabaseRoute.getListGoal(id_match,id_away);
                int MaLichThiDau = DatabaseRoute.getIdScheduleByTwoIdClub(id_home,id_away);
                String score = goal1.size() + " - " + goal2.size();
                DatabaseRoute.updateScore(MaLichThiDau,score);
                MatchDetail.binding.tvScore.setText(score);
                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                itemListener.goalRecyclerViewListClicked(true);
            }
        });
        b.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                itemListener.goalRecyclerViewListClicked(false);
                dialog.cancel();
            }
        });
        AlertDialog al = b.create();
        al.show();
    }
}
