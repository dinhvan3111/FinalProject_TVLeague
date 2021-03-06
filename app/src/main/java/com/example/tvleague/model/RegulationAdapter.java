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

                //ki???m tra tu???i t???i ??a > tu???i t???i thi???u
                if((regulation.getId() == 1 && Integer.parseInt(num) >= regulationList.get(1).getRegulationNum())||
                        (regulation.getId() == 2 && Integer.parseInt(num) <= regulationList.get(0).getRegulationNum())){
                    Toast.makeText(view.getContext(), "Tu???i t???i ??a > Tu???i t???i thi???u", Toast.LENGTH_SHORT).show();
                    return;
                }

                //ki???m tra s??? l?????ng c???u th??? t???i ??a > s??? l?????ng c???u th??? t???i thi???u
                if((regulation.getId() == 3 && Integer.parseInt(num) >= regulationList.get(3).getRegulationNum())||
                        (regulation.getId() == 4 && Integer.parseInt(num) <= regulationList.get(2).getRegulationNum())){
                    Toast.makeText(view.getContext(), "S??? c???u th??? t???i ??a > S??? c???u th??? t???i thi???u", Toast.LENGTH_SHORT).show();
                    return;
                }

                //ki???m tra th???ng > h??a  > thua
                if((regulation.getId() == 9 && Integer.parseInt(num) >= regulationList.get(7).getRegulationNum())||
                        (regulation.getId() == 8 && Integer.parseInt(num) >= regulationList.get(6).getRegulationNum())
                    ||(regulation.getId() == 7 && Integer.parseInt(num) <= regulationList.get(7).getRegulationNum())){
                    Toast.makeText(view.getContext(), "??i???m Th???ng > ??i???m H??a > ??i???m Thua", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                //ki???m tra priority t??? 1->4
                if(regulation.getId() == 10 &&((Integer.parseInt(num) > 4) ||(Integer.parseInt(num) == 0))){
                    Toast.makeText(view.getContext(), "??u ti??n ch??? t??? 1->4", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                binding.tvRegulationNum.setText(num);
                binding.edtRegulationNum.setText(num);
                // Thay ?????i quy ?????nh trong database
                DatabaseRoute.changeRelutationById(regulation.getId(),Integer.parseInt(num));
                regulationList.get(holder.getAdapterPosition()).setRegulationNum(Integer.parseInt(num));
                Toast.makeText(view.getContext(), "C???p nh???t th??nh c??ng!", Toast.LENGTH_SHORT).show();
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
