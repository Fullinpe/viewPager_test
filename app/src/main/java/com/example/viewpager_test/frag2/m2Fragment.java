package com.example.viewpager_test.frag2;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.viewpager_test.DBUtils;
import com.example.viewpager_test.Globals;
import com.example.viewpager_test.R;
import com.example.viewpager_test.pickerDialog;
import com.example.viewpager_test.pswDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class m2Fragment extends Fragment {

    private M2ViewModel mViewModel;
    Handler handler = new Handler();
    private String[][] strings;
    private String[][] temp;
    public ProgressBar progressBar;
    public TextView none_m2_tv, raise_tv;

    public static m2Fragment newInstance() {
        return new m2Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.m2_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(M2ViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        none_m2_tv = view.findViewById(R.id.none_m2);
        raise_tv = view.findViewById(R.id.raise_hands);
        final Button complaint = view.findViewById(R.id.complaint_m2);
        final ListView listView2 = view.findViewById(R.id.m2_list);
        progressBar = view.findViewById(R.id.pb_m2);
        final List<Map<String, Object>> list_m2 = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                strings = null;
                strings = DBUtils.select_DB("SELECT * FROM members WHERE MGR>2 AND COMN>0 ORDER BY COMN DESC", "NAME", "ARRIVE", "RECN", "COMN");
                if (strings != null) {
                    Map<String, Object> map = new HashMap<>();
                    for (int i = 0; i < strings.length; i++) {
                        if (i > 0)
                            map = new HashMap<>();
                        map.put("name", strings[i][0]);
                        map.put("arrive", strings[i][1]);
                        map.put("recn", strings[i][2]);
                        map.put("comn", strings[i][3]);
                        list_m2.add(map);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            m2_Adapter adapter_m2 = new m2_Adapter(getActivity());
                            int visi = View.INVISIBLE;
                            if (list_m2.size() == 0)
                                visi = View.VISIBLE;
                            none_m2_tv.setVisibility(visi);
                            adapter_m2.setList(list_m2);
                            listView2.setAdapter(adapter_m2);
                        }
                    });
                }
            }
        }).start();

        complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Globals.pri_able) {
                    complaint.setClickable(false);
                    final pickerDialog.Builder pickerdialog = new pickerDialog.Builder(getActivity(),Globals.list);
                    pickerdialog.setTitle("你将要投诉").setOk("确定投诉", new Runnable() {
                        @Override
                        public void run() {
                            final pswDialog.Builder pswdialog = new pswDialog.Builder(getActivity());
                            pswdialog.setOk(new Runnable() {
                                @Override
                                public void run() {
                                    List<Boolean> checked = pickerdialog.getTorf();
                                    int num_checked = 0;
                                    final StringBuilder checked_ = new StringBuilder();
                                    boolean flag = false;
                                    for (int i = 0; i < Globals.list.size(); i++) {
                                        if (checked.get(i)) {
                                            if (flag)
                                                checked_.append(",");
                                            flag = true;
                                            checked_.append(Globals.list.get(i).get("sid"));
                                            num_checked++;
                                        }
                                    }
                                    final int rows = DBUtils._DB("UPDATE members SET COMN=COMN+" + 1 + " WHERE S_ID IN (" + checked_.toString() + ")");

                                    if (num_checked == rows) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Globals.maketoast(getActivity(), "操作成功 ！！" + "\n" + checked_);
                                            }
                                        });
                                        DBUtils._DB("UPDATE members SET PRI1=0 WHERE S_ID='" + Globals.S_ID + "'");
                                        Globals.pri_able = false;
                                        Globals.logs_thread(Globals.S_ID, "投诉投票", checked_.toString());
                                    } else {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Globals.maketoast(getActivity(), "存在自检问题，联系管理员 ");
                                            }
                                        });
                                    }
                                }
                            }).setonCancel(new Runnable() {
                                @Override
                                public void run() {
                                    complaint.setClickable(true);
                                }
                            }).show();

                        }
                    })
                            .setonCancel(new Runnable() {
                                @Override
                                public void run() {
                                    complaint.setClickable(true);
                                }
                            })
                            .setNone("放弃投诉", new Runnable() {
                                @Override
                                public void run() {
                                    Globals.maketoast(getActivity(), "取消投诉成功");
                                }
                            })
                            .show();
                }else
                    Globals.maketoast(getActivity(),"当前不能进行投票 ^_^");

            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Globals.maketoast(getActivity(), "hide_change_" + hidden);
    }
}
