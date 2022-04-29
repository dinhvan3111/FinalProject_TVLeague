package com.example.tvleague.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvleague.databinding.ItemRegulationRowBinding;

import java.util.ArrayList;

public class RegulationAdapter extends RecyclerView.Adapter<RegulationAdapter.RegulationViewHolder> {
    ArrayList<Regulation> regulationList;

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
                binding.tvRegulationNum.setText(num);
                binding.edtRegulationNum.setText(num);
                // Thay đổi quy định trong database
                // TO DO
                // ...

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
