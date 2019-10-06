package com.example.viewpager_test.frag4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.viewpager_test.R;

import java.util.List;
import java.util.Map;

public class his_Adapter extends BaseAdapter {

    List<Map<String,Object>> list;
    private LayoutInflater inflater;
    public his_Adapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint({"ViewHolder", "InflateParams"}) View view1=inflater.inflate(R.layout.his_item,null);

        TextView textView1=view1.findViewById(R.id.his_type_i);
        TextView textView2=view1.findViewById(R.id.his_time_i);
        TextView textView3=view1.findViewById(R.id.his_comment_i);

        Map<String,Object> map=list.get(i);
        textView1.setText((String) map.get("type"));
        textView2.setText((String) map.get("time"));
        textView3.setText((String) map.get("comment"));

        return view1;
    }
}
