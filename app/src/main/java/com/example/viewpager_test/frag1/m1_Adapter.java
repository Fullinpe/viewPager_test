package com.example.viewpager_test.frag1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.viewpager_test.Globals;
import com.example.viewpager_test.R;

import java.util.List;
import java.util.Map;

public class m1_Adapter extends BaseAdapter {

    List<Map<String,Object>> list;
    private LayoutInflater inflater;
    public m1_Adapter(Context context,List<Map<String, Object>> list) {
        this.inflater = LayoutInflater.from(context);
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
        @SuppressLint({"ViewHolder", "InflateParams"}) View view1=inflater.inflate(R.layout.m1_item,null);

        TextView major=view1.findViewById(R.id.major_m1_i);
        TextView name=view1.findViewById(R.id.name_m1_i);
        TextView mgr=view1.findViewById(R.id.job_m1_i);

        Map<String,Object> map= Globals.list.get(i);
        major.setText((String) map.get("major"));
        name.setText((String) map.get("name"));
        mgr.setText((String) map.get("mgr"));

        return view1;
    }
}
