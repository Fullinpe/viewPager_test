package com.example.viewpager_test.frag2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.viewpager_test.DBUtils;
import com.example.viewpager_test.Globals;
import com.example.viewpager_test.R;
import com.example.viewpager_test.pickerDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class m2Fragment extends Fragment {

    private M2ViewModel mViewModel;
    Handler handler=new Handler();
    private String[][] strings;
    private String[][] temp;
    public static boolean[] cheched;
    private int num_checked=0;

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
        final TextView none_m2=view.findViewById(R.id.none_m2);
        final Button complaint=view.findViewById(R.id.complaint_m2);
        final Button recommend=view.findViewById(R.id.recommend_m2);
        final ListView listView2=view.findViewById(R.id.m2_list);
        final List<Map<String,Object>> list_m2=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                strings=null;
                strings= DBUtils.select_DB("SELECT * FROM members WHERE MGR>2 AND COMN>0 ORDER BY COMN DESC","NAME","ARRIVE","RECN","COMN");
                if(strings!=null) {
                    Map<String, Object> map = new HashMap<>();
                    for (int i=0;i<strings.length;i++) {
                        if(i>0)
                            map =new HashMap<>();
                        map.put("name",strings[i][0]);
                        map.put("arrive",strings[i][1]);
                        map.put("recn",strings[i][2]);
                        map.put("comn",strings[i][3]);
                        list_m2.add(map);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            m2_Adapter adapter_m2=new m2_Adapter(getActivity());
                            int visi=View.INVISIBLE;
                            if(list_m2.size()==0)
                                visi=View.VISIBLE;
                            none_m2.setVisibility(visi);
                            adapter_m2.setList(list_m2);
                            listView2.setAdapter(adapter_m2);
                        }
                    });
                }

            }
        }).start();
        complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complaint.setClickable(false);
                final Dialog dialog=new Dialog(Objects.requireNonNull(getActivity()));
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        complaint.setClickable(true);
                    }
                });
                dialog.setContentView(R.layout.picker_dia);
                final ListView listView=dialog.findViewById(R.id.choose_list);
                TextView textView=dialog.findViewById(R.id.choose_title);
                Button cancel =dialog.findViewById(R.id.cancel_choose);
                Button ok=dialog.findViewById(R.id.ok_choose);
                textView.setText("请选择你想投诉的同学：");
                final List<Map<String,Object>> list=new ArrayList<>();
                cheched=null;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        temp = null;
                        temp=DBUtils.select_DB("SELECT S_ID ,`NAME` FROM members WHERE MGR>1","S_ID","NAME");

                        if(temp!=null)
                        {
                            cheched=new boolean[temp.length];
                            Map<String, Object> map = new HashMap<>();
                            for (int i=0;i<temp.length;i++)
                            {
                                if(i>0)
                                    map =new HashMap<>();
                                map.put("num",temp[i][0]);
                                map.put("name",temp[i][1]);
                                list.add(map);
                            }
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    picker_Adapter adapter=new picker_Adapter(getActivity());
//                                    adapter.setList(list);
//                                    listView.setAdapter(adapter);
//                                    dialog.show();
//                                }
//                            });
                        }
                    }
                }).start();
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(true){
                            dialog.dismiss();
                            complaint.setClickable(true);
                            final StringBuilder checked_= new StringBuilder();
                            num_checked=0;
                            boolean flag=false;
                            if(listView.getCount()==cheched.length)
                                for(int i=0;i<listView.getCount();i++) {
                                    ConstraintLayout constraintLayout= (ConstraintLayout) listView.getAdapter().getView(i,null,null);
                                    TextView textView1=constraintLayout.findViewById(R.id.s_id_choose);
                                    if (cheched[i])
                                    {
                                        if(flag)
                                            checked_.append(",");
                                        flag=true;
                                        checked_.append(textView1.getText().toString());
                                        num_checked++;
                                    }
                                }
                            final Dialog dialog=new Dialog(Objects.requireNonNull(getActivity()));
                            dialog.setContentView(R.layout.psw_dia);
                            final EditText key_dia=dialog.findViewById(R.id.key_dia);
                            Button button=dialog.findViewById(R.id.ok_button);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    complaint.setClickable(true);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run()
                                        {
                                            strings=null;
                                            strings=DBUtils.select_DB("SELECT * FROM admin WHERE S_ID='"
                                                    +1713206317+"' AND Password='"
                                                    +key_dia.getText().toString()+"'","S_ID");
                                            if(strings!=null)
                                            {
                                                if(strings.length==1)
                                                {
                                                    final int rows=DBUtils._DB("UPDATE members SET COMN=COMN+"+4+" WHERE S_ID IN ("+checked_.toString()+")");

                                                    if(num_checked==rows)
                                                    {
                                                        handler.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(getActivity(),"操作成功！",Toast.LENGTH_LONG).show();
                                                            }
                                                        });
//                                                        DBUtils._DB("UPDATE members SET PRI1=0 WHERE S_ID='"+MainActivity.S_ID+"'");
//                                                        MainActivity.pri1_able=false;
//                                                        if(!MainActivity.pri2_able)
//                                                        {
//                                                            MainActivity.temp=0;
//                                                            DBUtils._DB("UPDATE members SET PRI=0 WHERE S_ID='"+MainActivity.S_ID+"'");
//                                                        }
//                                                        logs_thread(MainActivity.S_ID,"投诉投票",checked_.toString());
                                                    }
                                                    else{
                                                        handler.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(getActivity(),"存在自检问题，联系管理员 ",Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                    }

                                                }
                                                else if(strings.length>1)
                                                {
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                                                            builder.setTitle("提示：");
                                                            builder.setMessage("请确认网络链路正确");
                                                            builder.setPositiveButton("确定", null);
                                                            builder.show();
                                                        }
                                                    });
                                                }
                                                else
                                                {
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                                                            builder.setTitle("提示：");
                                                            builder.setMessage("请确认填写密码是否有误");
                                                            builder.setPositiveButton("确定", null);
                                                            builder.show();
                                                        }
                                                    });
                                                }
                                            }
                                            else
                                            {
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                                                        builder.setTitle("提示：");
                                                        builder.setMessage("请确认网络链路正确");
                                                        builder.setPositiveButton("确定", null);
                                                        builder.show();
                                                    }
                                                });
                                            }

                                        }
                                    }).start();

                                }
                            });
                            dialog.show();
                        }
                        else{
                            dialog.dismiss();
                            complaint.setClickable(true);
                            Toast.makeText(getActivity(),"当前不能投票",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        complaint.setClickable(true);
                    }
                });



            }
        });
        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDialog pickerdialog=new pickerDialog.Builder(getActivity()).show();
//                final pswDialog.Builder pswdialog=new pswDialog.Builder(getActivity());
//                pswdialog.setOk(new Runnable() {
//                    @Override
//                    public void run() {
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                Globals.maketoast(getActivity(),"OK"+pswdialog.getError_s());
//                            }
//                        });
//                    }
//                }).show();
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Globals.maketoast(getActivity(),"hide_change_"+hidden);
    }
}
