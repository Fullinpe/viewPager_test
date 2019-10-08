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
    private String[][] strings;
    public TextView count_m1;
    public SlideRecycler slideRecycler;
    private boolean for1 = true;
    public m1_Adapter m1_adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static m1Fragment newInstance() {
        return new m1Fragment();
    }

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
        slideRecycler = view.findViewById(R.id.member_m1);
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
                            String[][] init_temp = DBUtils.select_DB("SELECT S_ID,`NAME`,mgr_name MGR,MAJOR,QQ,TEL,TASK FROM members LEFT JOIN mgr_table ON members.MGR=mgr_table.mgr_id WHERE MGR>0 ORDER BY  members.MGR DESC",
                                    "S_ID", "NAME", "MGR", "MAJOR", "QQ", "TEL", "TASK");
                            Globals.online_m1 = new ArrayList<>();
                            if (init_temp != null) {
                                Map<String, Object> map = new HashMap<>();
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
                                    Globals.online_m1.add(map);
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



//        slideRecycler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if(!MainActivity.MGR.equals("0")){
//                    Map<String,Object> map=list.get(i);
//                    if (isGotoable(getActivity(), "com.tencent.mobileqq"))
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin="+map.get("qq")+"&version=1")));
//                    else if(isGotoable(getActivity() ,"com.tencent.tim"))
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin="+map.get("qq")+"&version=1")));
//                    else
//                        Toast.makeText(getActivity(),"本机未安装QQ应用",Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getActivity(),"你还不是成员，无法快捷通讯及获取更多信息",Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//                slideRecycler.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                    @Override
//                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        if(!MainActivity.MGR.equals("0")){
//                            Map<String,Object> map=list.get(i);
//                            startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + map.get("tel"))));
//                        }else{
//                            Toast.makeText(getActivity(),"你还不是成员，无法快捷通讯及获取更多信息",Toast.LENGTH_LONG).show();
//                        }
//                        return true;
//                    }
//                });


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                strings=null;
//                strings= DBUtils.select_DB("SELECT MAJOR,`NAME`,QQ,TEL,mgr_name FROM members LEFT JOIN mgr_table ON members.MGR=mgr_table.mgr_id WHERE MGR>1 ORDER BY MGR DESC","MAJOR","NAME","mgr_name","QQ","TEL");
//                if(strings!=null)
//                {
//                    Map<String, Object> map = new HashMap<>();
//                    for (int i=0;i<strings.length;i++)
//                    {
//                        if(i>0)
//                            map =new HashMap<>();
//                        map.put("num",strings[i][0]);
//                        map.put("name",strings[i][1]);
//                        map.put("job",strings[i][2]);
//                        map.put("qq",strings[i][3]);
//                        map.put("tel",strings[i][4]);
//                        list.add(map);
//                    }
//                    handler.post(new Runnable() {
//                        @SuppressLint("SetTextI18n")
//                        @Override
//                        public void run() {
//                            count_m1.setText("当前人数："+list.size());
//                            adapter.setList(list);
//                            count_m1.setText("当前人数："+list.size());
//                            slideRecycler.setAdapter(adapter);
//                        }
//                    });
//                }
//
//            }
//        }).start();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Globals.m1_hidden=hidden;
    }
}
