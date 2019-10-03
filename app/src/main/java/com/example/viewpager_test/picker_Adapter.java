package com.example.viewpager_test;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class picker_Adapter extends RecyclerView.Adapter<picker_Adapter.InnerHolder> {

    private Context context;
    private List<Map<String,Object>> list;
    private List<Boolean> torf;

    public picker_Adapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
        torf=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            torf.add(false);
        }

        Log.e("ck",list.size()+"");
    }

    public List<Boolean> getTorf() {
        return torf;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerHolder(LayoutInflater.from(context).inflate(R.layout.picker_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {

        Map<String,Object> map=list.get(position);
        holder.name.setText((String) map.get("name"));
        holder.sid.setText((String) map.get("sid"));
        holder.checkBox.setChecked(torf.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        TextView sid,name;
        CheckBox checkBox;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            sid=itemView.findViewById(R.id.s_id_choose);
            name=itemView.findViewById(R.id.name_choose);
            checkBox=itemView.findViewById(R.id.checkBox_choose);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    torf.set(getAdapterPosition(),isChecked);
                    int pickers=0;
                    for (boolean torf_:torf){
                        if(torf_)
                            pickers++;
                    }
                    setTitles(pickers);
                    Log.e("TAG",torf.toString()+getAdapterPosition());
                }
            });
        }
    }
    public abstract void setTitles(int picks);
}
