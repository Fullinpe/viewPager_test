package com.example.viewpager_test.frag4;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.viewpager_test.DBUtils;
import com.example.viewpager_test.Globals;
import com.example.viewpager_test.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class m4Fragment extends Fragment {

    private M4ViewModel mViewModel;
    private Handler handler = new Handler();

    public static m4Fragment newInstance() {
        return new m4Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.m4_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(M4ViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button his_done = view.findViewById(R.id.his_done);
        final TextView tv_1 = view.findViewById(R.id.name_m4);
        final TextView tv_2 = view.findViewById(R.id.s_id_m4);
        final TextView tv_3 = view.findViewById(R.id.major_m4);
        final TextView tv_4 = view.findViewById(R.id.mgr_m4);
        final TextView tv_5 = view.findViewById(R.id.QQ_m4);
        final TextView tv_6 = view.findViewById(R.id.TEL_m4);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[][] strings = null;
                strings = DBUtils.select_DB("SELECT * FROM members LEFT JOIN mgr_table ON members.MGR=mgr_table.mgr_id WHERE S_ID='"
                        + Globals.S_ID + "'", "NAME", "S_ID", "MAJOR", "mgr_name", "QQ", "TEL");
                final String[][] finalStrings = strings;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (finalStrings != null) {
                            if (finalStrings.length == 1) {
                                tv_1.setText(finalStrings[0][0]);
                                tv_2.setText(finalStrings[0][1]);
                                tv_3.setText(finalStrings[0][2]);
                                tv_4.setText(finalStrings[0][3]);
                                tv_5.setText(finalStrings[0][4]);
                                tv_6.setText(finalStrings[0][5]);
                            } else if (finalStrings.length > 1) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("提示：");
                                builder.setMessage("请联系管理员，账号异常");
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Objects.requireNonNull(getActivity()).finish();
                                    }
                                });
                                builder.show();
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("提示：");
                            builder.setMessage("请联系管理员，账号异常");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Objects.requireNonNull(getActivity()).finish();
                                }
                            });
                            builder.show();
                        }
                    }
                });
            }
        }).start();

        his_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog1 = new Dialog(getActivity());
                dialog1.setContentView(R.layout.his_dia);
                final ListView listView1 = dialog1.findViewById(R.id.his_list_dia);
                final List<Map<String, Object>> list = new ArrayList<>();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String[][] temp = null;
                        temp = DBUtils.select_DB("SELECT * FROM `logs` WHERE S_ID='" + Globals.S_ID + "' AND OPER_device NOT LIKE '---%'", "TYPE_operation", "OPER_time", "COMMENT");
                        if (temp != null) {
                            Map<String, Object> map = new HashMap<>();
                            for (int i = temp.length - 1; i >= 0; i--) {
                                if (i < temp.length - 1)
                                    map = new HashMap<>();
                                map.put("type", temp[i][0]);
                                map.put("time", temp[i][1].substring(0, 19));
                                map.put("comment", temp[i][2]);
                                list.add(map);
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    his_Adapter adapter = new his_Adapter(getActivity());
                                    adapter.setList(list);
                                    listView1.setAdapter(adapter);
                                    dialog1.show();
                                }
                            });
                        }
                    }
                }).start();
                dialog1.show();


            }
        });
    }
}
