package com.example.viewpager_test.frag2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewpager_test.R;

import java.util.List;
import java.util.Map;

public class m2_Adapter extends RecyclerView.Adapter<m2_Adapter.InnerHolder> {

    private Context context;
    public List<Map<String,Object>> list;

    m2_Adapter(Context context,List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.m2_item, parent, false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        Map<String,Object> map=list.get(position);
        holder.textView1.setText((String) map.get("name"));
        holder.textView2.setText((String) map.get("arrive"));
        holder.textView3.setText((String)map.get("recn"));
        holder.textView4.setText((String)map.get("comn"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return list.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        @SuppressLint({"ViewHolder", "InflateParams"}) View view1=inflater.inflate(R.task_edit.m2_item,null);
//
//        TextView textView1=view1.findViewById(R.id.name_m2_i);
//        TextView textView2=view1.findViewById(R.id.m2_arrive_i);
//        TextView textView3=view1.findViewById(R.id.m2_recn_i);
//        TextView textView4=view1.findViewById(R.id.comn_m2_i);
//
//
//        Map<String,Object> map=list.get(i);
//        textView1.setText((String) map.get("name"));
//        textView2.setText((String) map.get("arrive"));
//        textView3.setText((String)map.get("recn"));
//        textView4.setText((String)map.get("comn"));
//
//
//        return view1;
//    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.name_m2_i);
            textView2=itemView.findViewById(R.id.m2_arrive_i);
            textView3=itemView.findViewById(R.id.m2_recn_i);
            textView4=itemView.findViewById(R.id.comn_m2_i);
        }
    }
}
