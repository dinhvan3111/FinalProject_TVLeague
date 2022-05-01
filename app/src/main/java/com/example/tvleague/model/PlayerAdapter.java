package com.example.tvleague.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvleague.view.MatchDetail;
import com.example.tvleague.view.PlayerListActivity;
import com.example.tvleague.databinding.ItemPlayerRowBinding;

import java.util.ArrayList;
import java.util.Observable;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {
    private ObservableArrayList<Player> listPlayer;
    Context context;
    int index = 0;
    public PlayerAdapter(Context context, ObservableArrayList<Player> listPlayer){
        this.context = context;
        this.listPlayer =  listPlayer;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemPlayerRowBinding binding = ItemPlayerRowBinding.inflate(layoutInflater,parent,false);
        return new PlayerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = listPlayer.get(position);
        index = holder.getAdapterPosition();
        if(player == null){
            return;
        }
        ItemPlayerRowBinding binding = holder.getBinding();

        //chỉnh sửa cầu thủ
        binding.tvName.setText(player.getName());
        binding.tvDoB.setText(player.getDoB());
        if(player.getType() == 0 || player.getType() == 1){
            binding.tvType.setText("CT nội");
        }
        else{
            binding.tvType.setText("CT ngoại");
        }
        binding.tvNote.setText(player.getNote());
        binding.itemPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { PlayerListActivity.editPlayer(player,holder.getAdapterPosition());
            }
        });



        //Xóa cầu thủ
        binding.itemPlayer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                delete(holder.getAdapterPosition(), player.getName());
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        if(listPlayer != null){
            return listPlayer.size();
        }
        return 0;
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvDoB;
        private TextView tvType;
        private TextView tvNote;
        private ItemPlayerRowBinding binding;
        private LinearLayout layout;

        public PlayerViewHolder(@NonNull ItemPlayerRowBinding binding) {
            super(binding.getRoot());
            tvName = binding.tvName;
            tvDoB = binding.tvDoB;
            tvType = binding.tvType;
            tvNote = binding.tvNote;
            layout = binding.itemPlayer;
            this.binding = binding;
        }

        public  ItemPlayerRowBinding getBinding(){
            return binding;
        }
    }
    public void delete(int pos, String name){
        Player player = listPlayer.get(pos);
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle("Xóa cầu thủ");
        b.setMessage("Bạn có chắc chắn xóa cầu thủ " + name + " không?");
        b.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DatabaseRoute.deletePlayerById(player.getId());
                listPlayer.remove(pos);
                notifyDataSetChanged();
                Toast.makeText(context, "Đã xóa " + name, Toast.LENGTH_SHORT).show();
            }
        });
        b.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog al = b.create();
        al.show();
    }
}
