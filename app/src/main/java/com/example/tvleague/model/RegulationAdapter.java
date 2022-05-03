package com.example.tvleague.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvleague.databinding.ItemRegulationRowBinding;
import com.example.tvleague.view.MainActivity;

import java.util.ArrayList;

public class RegulationAdapter extends RecyclerView.Adapter<RegulationAdapter.RegulationViewHolder> {
    ArrayList<Regulation> regulationList;
    boolean isStarted = DatabaseRoute.IsStartLeague();
    public RegulationAdapter(ArrayList<Regulation> regulationList) {
        this.regulationList = regulationList;
    }

    @NonNull
    @Override
    public RegulationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRegulationRowBinding binding = ItemRegulationRowBinding.inflate(inflater,parent,false);
        return new RegulationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RegulationViewHolder holder, int position) {
        Regulation regulation = regulationList.get(position);
        if(regulation == null)
            return;
        ItemRegulationRowBinding binding = holder.getBinding();
        binding.regulationDes.setText(regulation.getRegulationDes());
        binding.tvRegulationNum.setText(regulation.getRegulationNum() + "");
        binding.edtRegulationNum.setText(regulation.getRegulationNum() + "");
        if(regulation.getNote().equals("") || regulation.getNote() == null){
            binding.layoutNote.setVisibility(View.GONE);
        }
        else{
            binding.layoutNote.setVisibility(View.VISIBLE);
            binding.regulationNote.setText(regulation.getNote());
        }

        if(isStarted && (regulation.getId() == 1 || regulation.getId() == 2
                ||regulation.getId() == 3 ||regulation.getId() == 4 ||
                regulation.getId() == 5||
                regulation.getId() == 10) ){
            binding.btnChangeRegulationNum.setEnabled(false);
            binding.btnChangeRegulationNum.setTextColor(Color.GRAY);
        }
        binding.btnChangeRegulationNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvRegulationNum.setVisibility(View.GONE);
                binding.edtRegulationNum.setVisibility(View.VISIBLE);
                binding.btnChangeRegulationNum.setVisibility(View.GONE);
                binding.btnApplyChangeRegulationNum.setVisibility(View.VISIBLE);
                binding.btnCancelChangeRegulationNum.setVisibility(View.VISIBLE);
            }
        });
        binding.btnApplyChangeRegulationNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = binding.edtRegulationNum.getText().toString();

                //kiểm tra thắng > hòa  > thua
                if((regulation.getId() == 9 && Integer.parseInt(num) >= MainActivity.regulations.getDRAW_POINT())||
                        (regulation.getId() == 8 && Integer.parseInt(num) >= MainActivity.regulations.getWIN_POINT())
                    ||(regulation.getId() == 7 && Integer.parseInt(num) <= MainActivity.regulations.getDRAW_POINT())){
                    Toast.makeText(view.getContext(), "Điểm Thắng > Điểm Hòa > Điểm Thua", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                //kiểm tra priority từ 1->4
                if(regulation.getId() == 10 &&(Integer.parseInt(num) > 4) ||(Integer.parseInt(num) == 0)){
                    Toast.makeText(view.getContext(), "Ưu tiên chỉ từ 1->4", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                binding.tvRegulationNum.setText(num);
                binding.edtRegulationNum.setText(num);
                // Thay đổi quy định trong database
                DatabaseRoute.changeRelutationById(regulation.getId(),Integer.parseInt(num));
                Toast.makeText(view.getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                binding.tvRegulationNum.setVisibility(View.VISIBLE);
                binding.edtRegulationNum.setVisibility(View.GONE);
                binding.btnChangeRegulationNum.setVisibility(View.VISIBLE);
                binding.btnApplyChangeRegulationNum.setVisibility(View.GONE);
                binding.btnCancelChangeRegulationNum.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
        binding.btnCancelChangeRegulationNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvRegulationNum.setText(regulation.getRegulationNum() + "");
                binding.edtRegulationNum.setText(regulation.getRegulationNum() + "");
                binding.tvRegulationNum.setVisibility(View.VISIBLE);
                binding.edtRegulationNum.setVisibility(View.GONE);
                binding.btnChangeRegulationNum.setVisibility(View.VISIBLE);
                binding.btnApplyChangeRegulationNum.setVisibility(View.GONE);
                binding.btnCancelChangeRegulationNum.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(regulationList != null)
            return regulationList.size();
        else
            return 0;
    }

    public class RegulationViewHolder extends RecyclerView.ViewHolder{
        ItemRegulationRowBinding binding;
        public RegulationViewHolder(@NonNull ItemRegulationRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemRegulationRowBinding getBinding() {
            return binding;
        }
    }
}
