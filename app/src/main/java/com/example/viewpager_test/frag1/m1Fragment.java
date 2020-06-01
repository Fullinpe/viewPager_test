package com.example.viewpager_test.frag1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.viewpager_test.DBUtils;
import com.example.viewpager_test.Globals;
import com.example.viewpager_test.R;
import com.example.viewpager_test.SlideRecycler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class m1Fragment extends Fragment {

    public M1ViewModel mViewModel;
    Handler handler = new Handler();
    public TextView count_m1,count_m2,count_m3;
    public SlideRecycler slideRecycler;
    public m1_Adapter m1_adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.m1_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(M1ViewModel.class);
        mViewModel.getOnline_data().observe(this, new Observer<List<Map<String, Object>>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(List<Map<String, Object>> maps) {
                count_m1.setText("当前人数：" + maps.size());
                count_m2.setText("新人："+Globals.newers);
                count_m3.setText("正式队员："+Globals.members);
                m1_adapter.list = maps;
                m1_adapter.notifyItemRangeChanged(0,maps.size());
                m1_adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Globals.m1_hidden=false;

        count_m1 = view.findViewById(R.id.count_m1);
        count_m2 = view.findViewById(R.id.m1_newers);
        count_m3 = view.findViewById(R.id.m1_members);
        slideRecycler = view.findViewById(R.id.member_m1);
        slideRecycler.setTopview(40);
        slideRecycler.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), DividerItemDecoration.VERTICAL));

//        FunGameRefreshView refreshView = view.findViewById(R.id.refresh_hit_block);
        swipeRefreshLayout=view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (Globals.sign_in) {
                            String[][] init_temp = DBUtils.select_DB("SELECT S_ID,`NAME`,mgr_name MGR,MAJOR,QQ,TEL,TASK,DONE FROM members LEFT JOIN mgr_table ON members.MGR=mgr_table.mgr_id WHERE MGR>0 ORDER BY  members.MGR DESC",
                                    "S_ID", "NAME", "MGR", "MAJOR", "QQ", "TEL", "TASK","DONE");
                            Globals.online_m1 = new ArrayList<>();
                            if (init_temp != null) {
                                Map<String, Object> map = new HashMap<>();
                                Globals.members=0;
                                Globals.newers=0;
                                for (int i = 0; i < init_temp.length; i++) {
                                    if (i > 0)
                                        map = new HashMap<>();
                                    map.put("sid", init_temp[i][0]);
                                    map.put("name", init_temp[i][1]);
                                    map.put("mgr", init_temp[i][2]);
                                    map.put("major", init_temp[i][3]);
                                    map.put("qq", init_temp[i][4]);
                                    map.put("tel", init_temp[i][5]);
                                    map.put("task", init_temp[i][6]);
                                    map.put("done", init_temp[i][7]);
                                    Globals.online_m1.add(map);
                                    if(!init_temp[i][2].equals("新人")&&!init_temp[i][2].equals("退休"))
                                        Globals.members++;
                                    else if(init_temp[i][2].equals("新人"))
                                        Globals.newers++;
                                }
                                if (mViewModel.online_data != null)
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mViewModel.online_data.setValue(Globals.online_m1);
                                        }
                                    });
                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }).start();
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Globals.m1_hidden=hidden;
    }
}
