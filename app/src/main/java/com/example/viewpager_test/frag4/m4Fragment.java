package com.example.viewpager_test.frag4;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.viewpager_test.DBUtils;
import com.example.viewpager_test.Globals;
import com.example.viewpager_test.LogActivity;
import com.example.viewpager_test.R;
import com.example.viewpager_test.pswDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class m4Fragment extends Fragment {

    private M4ViewModel mViewModel;
    private Handler handler = new Handler();
    public TextView tv_1;
    public TextView tv_2;
    public TextView tv_3;
    public TextView tv_4;
    public TextView tv_5;
    public TextView tv_6;
    public TextView tv_7;
    private String[] oldmsg;
    private boolean exit = true;
    private String newpsw = null;
    private boolean newpsw_b = true;
    private int rows2;
    private boolean exit_out = true;

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
        Globals.m4_hidden = false;

        Button his_done = view.findViewById(R.id.his_done);
        final ImageButton imageButton = view.findViewById(R.id.imageButton);
        tv_1 = view.findViewById(R.id.name_m4);
        tv_2 = view.findViewById(R.id.s_id_m4);
        tv_3 = view.findViewById(R.id.major_m4);
        tv_4 = view.findViewById(R.id.mgr_m4);
        tv_5 = view.findViewById(R.id.QQ_m4);
        tv_6 = view.findViewById(R.id.TEL_m4);
        tv_7 = view.findViewById(R.id.m4_last);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[][] strings = null;
                strings = DBUtils.select_DB("SELECT * FROM members LEFT JOIN mgr_table ON members.MGR=mgr_table.mgr_id WHERE S_ID='"
                        + Globals.S_ID + "'", "NAME", "S_ID", "MAJOR", "mgr_name", "QQ", "TEL","LAST");
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
                                tv_7.setText(finalStrings[0][6]);
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
                pswDialog.Builder pswdialog = new pswDialog.Builder(getActivity());
                pswdialog.setOk(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
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
                }).show();



            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButton.setVisibility(View.INVISIBLE);
                final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        imageButton.setVisibility(View.VISIBLE);
                    }
                });
                dialog.setContentView(R.layout.menu_dia);
                Button button1 = dialog.findViewById(R.id.menu1);
                Button button2 = dialog.findViewById(R.id.menu2);
                Button button3 = dialog.findViewById(R.id.menu3);
                Button button4 = dialog.findViewById(R.id.menu4);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog1 = new Dialog(getActivity());
                        dialog1.setContentView(R.layout.edit_dia);
                        final EditText ed1 = dialog1.findViewById(R.id.ed1_dia);
                        final EditText ed2 = dialog1.findViewById(R.id.ed2_dia);
                        final EditText ed3 = dialog1.findViewById(R.id.ed3_dia);
                        final EditText ed4 = dialog1.findViewById(R.id.ed4_dia);
                        Button button = dialog1.findViewById(R.id.edb_dia);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String[][] strings = null;
                                strings = DBUtils.select_DB("SELECT * FROM members WHERE S_ID='" + Globals.S_ID + "'", "NAME", "MAJOR", "QQ", "TEL");
                                if (strings != null) {
                                    oldmsg = strings[0];
                                    ed1.setText(strings[0][0]);
                                    ed2.setText(strings[0][1]);
                                    ed3.setText(strings[0][2]);
                                    ed4.setText(strings[0][3]);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog1.show();
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            }
                        }).start();
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.dismiss();
                                pswDialog.Builder pswdialog = new pswDialog.Builder(getActivity());
                                pswdialog.setOk(new Runnable() {
                                    @Override
                                    public void run() {
                                        final int rows = DBUtils._DB("UPDATE members SET `NAME`='"
                                                + ed1.getText().toString() + "',MAJOR='"
                                                + ed2.getText().toString() + "',QQ='"
                                                + ed3.getText().toString() + "',TEL='"
                                                + ed4.getText().toString() + "' WHERE S_ID='" + Globals.S_ID + "'");
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (rows == 1) {
                                                    Toast.makeText(getActivity(), "操作成功！", Toast.LENGTH_LONG).show();
                                                    Globals.logs_thread(Globals.S_ID, "修改信息", oldmsg[0] + "-@" + oldmsg[1] + "-@" + oldmsg[2] + "-@" + oldmsg[3]);
                                                } else
                                                    Toast.makeText(getActivity(), "存在自检问题，联系管理员 " + rows, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }).show();
                            }
                        });
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        pswDialog.Builder pswdialog = new pswDialog.Builder(getActivity());
                        pswdialog.setOk(new Runnable() {
                            @Override
                            public void run() {
                                newpsw = null;
                                newpsw_b = true;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        final Dialog dialog1 = new Dialog(getActivity());
                                        dialog1.setContentView(R.layout.psw_c_dia);
                                        final EditText ed1 = dialog1.findViewById(R.id.psw1_dia);
                                        final EditText ed2 = dialog1.findViewById(R.id.psw2_dia);
                                        Button pswb = dialog1.findViewById(R.id.pswb_dia);
                                        ed2.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                            }

                                            @Override
                                            public void afterTextChanged(Editable editable) {
                                                if (ed2.getText().toString().equals(ed1.getText().toString()))
                                                    ed2.setTextColor(Color.BLACK);
                                                else
                                                    ed2.setTextColor(Color.RED);
                                            }
                                        });
                                        pswb.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (ed1.getText().toString().equals(ed2.getText().toString()) && !ed1.getText().toString().equals("")) {
                                                    newpsw = ed1.getText().toString();
                                                    dialog1.dismiss();
                                                    newpsw_b = false;
                                                } else
                                                    Toast.makeText(getActivity(), "两次填入密码不一致", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        dialog1.show();


                                    }
                                });
                                while (newpsw_b) {
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                rows2 = 0;
                                if (newpsw != null) {
                                    rows2 = DBUtils._DB("UPDATE admin SET Password='"
                                            + newpsw + "' WHERE S_ID='" + Globals.S_ID + "'");
                                    if (rows2 == 1) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Globals.maketoast(getActivity(), "已成功修改密码！");
                                                Globals.logs_thread(Globals.S_ID, "修改密码", "已成功修改密码");
                                            }
                                        });
                                    } else
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Globals.maketoast(getActivity(), "自检错误，请联系管理员");
                                            }
                                        });
                                }
                            }
                        }).show();
                    }
                });
                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String[][] temp=DBUtils.select_DB("SELECT PRI1 FROM members WHERE S_ID=1","PRI1");
                                if(temp!=null){
                                    if(temp.length>0){
                                        if(temp[0][0].equals("1")){
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    final Dialog dialog_task = new Dialog(getActivity());
                                                    dialog_task.setContentView(R.layout.task_edit);
                                                    Button button = dialog_task.findViewById(R.id.task_button);
                                                    final EditText editText = dialog_task.findViewById(R.id.task_edit);
                                                    button.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog_task.dismiss();
                                                            pswDialog.Builder pswdialog = new pswDialog.Builder(getActivity());
                                                            pswdialog.setOk(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    String temp = editText.getText().toString();
                                                                    if (temp != null) {
                                                                        rows2 = DBUtils._DB("UPDATE members SET TASK='"
                                                                                + temp + "' WHERE S_ID='" + Globals.S_ID + "'");
                                                                        if (rows2 == 1) {
                                                                            handler.post(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    Globals.maketoast(getActivity(), "已成功设置任务！");
                                                                                    Globals.logs_thread(Globals.S_ID, "修改信息", "已成功修改密码");
                                                                                }
                                                                            });
                                                                        } else
                                                                            handler.post(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    Globals.maketoast(getActivity(), "自检错误，请联系管理员");
                                                                                }
                                                                            });
                                                                    }
                                                                }
                                                            }).show();
                                                        }
                                                    });
                                                    dialog_task.show();
                                                }
                                            });

                                        }else
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Globals.maketoast(getActivity(),"当前不在设置任务时段");
                                                }
                                            });

                                    }
                                }
                            }
                        }).start();

                    }
                });
                button4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle("注销");
                        dialog.setMessage("确定注销登录？");
                        dialog.setNegativeButton("注销", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (exit_out) {
                                    exit_out = false;
                                    getActivity().finish();
                                    startActivity(new Intent(getActivity(), LogActivity.class));
                                    Globals.logs_thread(Globals.S_ID, "注销登录", "已注销");
                                    Globals.logs_thread_out(Globals.S_ID);
                                }
                                Globals.S_ID = "";
                                Globals.sign_in = false;
                                Globals.MGR = 0;
                            }
                        });
                        dialog.setPositiveButton("取消", null);
                        dialog.show();
                    }
                });
                Window win = dialog.getWindow();
                assert win != null;
                win.setWindowAnimations(R.style.dialogWindowAnim);
                win.setGravity(Gravity.LEFT | Gravity.TOP);
                WindowManager.LayoutParams lp = win.getAttributes();
                lp.x = win.getWindowManager().getDefaultDisplay().getWidth() - 700;
                lp.y = 200;//win.getWindowManager().getDefaultDisplay().getHeight() - 1070;
                lp.width = 500;
                lp.height = 800;
                lp.alpha = 0.9f;
                win.setAttributes(lp);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Globals.m4_hidden = hidden;
    }
}
