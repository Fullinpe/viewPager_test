package com.example.viewpager_test.frag3;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

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

public class m3Fragment extends Fragment {

    private M3ViewModel mViewModel;
    private Handler handler = new Handler();

    public static m3Fragment newInstance() {
        return new m3Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.m3_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(M3ViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final LinearLayout ll_m3 = view.findViewById(R.id.ll_m3);
        final Button recom = view.findViewById(R.id.recommend_m3);
        final ListView listView_m3 = view.findViewById(R.id.newmember_m3);
        recom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Globals.MGR == 4 || Globals.MGR == 5 || Globals.MGR == 6 || Globals.MGR == 7) {
                    recom.setClickable(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String[][] init_temp = DBUtils.select_DB("SELECT `NAME`, S_ID FROM members WHERE MGR=0 AND S_ID!=1 ORDER BY S_ID",
                                    "S_ID", "NAME");
                            final List<Map<String, Object>> mapList = new ArrayList<>();
                            if (init_temp != null) {
                                Map<String, Object> map = new HashMap<>();
                                for (int i = 0; i < init_temp.length; i++) {
                                    if (i > 0)
                                        map = new HashMap<>();
                                    map.put("sid", init_temp[i][0]);
                                    map.put("name", init_temp[i][1]);
                                    mapList.add(map);
                                }

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        final pickerDialog.Builder pickerdialog = new pickerDialog.Builder(getActivity(), mapList);
                                        pickerdialog.setOk("推荐新人", new Runnable() {
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
                                                        for (int i = 0; i < mapList.size(); i++) {
                                                            if (checked.get(i)) {
                                                                if (flag)
                                                                    checked_.append(",");
                                                                flag = true;
                                                                checked_.append(mapList.get(i).get("sid"));
                                                                num_checked++;
                                                            }
                                                        }
                                                        final int rows = DBUtils._DB("UPDATE members SET  MGR='1' WHERE S_ID IN (" + checked_.toString() + ")");

                                                        if (num_checked == rows) {
                                                            handler.post(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Globals.maketoast(getActivity(), "操作成功 ！！" + "\n" + checked_);
                                                                }
                                                            });
                                                            Globals.logs_thread(Globals.S_ID, "管理操作", checked_.toString());
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
                                                        recom.setClickable(true);
                                                    }
                                                }).show();

                                            }
                                        })
                                                .setonCancel(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        recom.setClickable(true);
                                                    }
                                                })
                                                .setNone("清理未竟", new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        //todo
                                                        final pswDialog.Builder pswdialog = new pswDialog.Builder(getActivity());
                                                        pswdialog.setOk(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                List<Boolean> checked = pickerdialog.getTorf();
                                                                int num_checked = 0;
                                                                final StringBuilder checked_ = new StringBuilder();
                                                                boolean flag = false;
                                                                for (int i = 0; i < mapList.size(); i++) {
                                                                    if (checked.get(i)) {
                                                                        if (flag)
                                                                            checked_.append(",");
                                                                        flag = true;
                                                                        checked_.append(mapList.get(i).get("sid"));
                                                                        num_checked++;
                                                                    }
                                                                }
                                                                final int rows = DBUtils._DB("DELETE FROM members WHERE S_ID IN (" + checked_.toString() + ")");
                                                                if (num_checked == rows) {
                                                                    handler.post(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            Globals.maketoast(getActivity(), "操作成功 ！！" + "\n" + checked_);
                                                                        }
                                                                    });
                                                                    Globals.logs_thread(Globals.S_ID, "管理操作", checked_.toString());
                                                                } else {
                                                                    handler.post(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            Globals.maketoast(getActivity(), "存在自检问题，联系管理员 ");
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        })
                                                                .show();
                                                    }
                                                })
                                                .show();
                                    }
                                });
                            } else {
                                //TODO:null
                            }

                        }
                    }).start();

                } else {
                    Toast.makeText(getActivity(), "权限不足", Toast.LENGTH_LONG).show();
                }
            }
        });
        final List<Map<String, Object>> list_m3 = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[][] strings = null;
                strings = DBUtils.select_DB("SELECT * FROM members WHERE MGR=1 AND RECN>0", "S_ID", "NAME", "RECN", "MAJOR");
                if (strings != null) {
                    Map<String, Object> map = new HashMap<>();
                    for (int i = 0; i < strings.length; i++) {
                        if (i > 0)
                            map = new HashMap<>();
                        map.put("num", strings[i][0]);
                        map.put("name", strings[i][1]);
                        map.put("accept", strings[i][2]);
                        map.put("major_n", strings[i][3]);
                        list_m3.add(map);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            newmAdapter adapter_m3 = new newmAdapter(getActivity());
                            int visi = View.INVISIBLE;
                            if (list_m3.size() > 0)
                                visi = View.VISIBLE;
                            ll_m3.setVisibility(visi);
                            adapter_m3.setList(list_m3);
                            listView_m3.setAdapter(adapter_m3);
                        }
                    });
                }

            }
        }).start();
    }
}
