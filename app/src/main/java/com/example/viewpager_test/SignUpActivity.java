package com.example.viewpager_test;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    String[][] temp;
    String temp_major="";
    int status=0;
    boolean exit_test=false,global=false,s_id=true,ed7_change=false;
    Handler handler=new Handler();
    private String temp_log;
    private boolean check_update=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        final ImageView status1=findViewById(R.id.status1);
        final ImageView status2=findViewById(R.id.status2);
        final ImageView status3=findViewById(R.id.status3);
        final ImageView status4=findViewById(R.id.status4);
        final ImageView status5=findViewById(R.id.status5);
        final ImageView status6=findViewById(R.id.status6);
        final ImageView status7=findViewById(R.id.status7);
        final EditText ed1=findViewById(R.id.ed1_signup);
        final EditText ed2=findViewById(R.id.ed2_signup);
        final EditText ed3=findViewById(R.id.ed3_signup);
        final EditText ed4=findViewById(R.id.ed4_signup);
        final EditText ed5=findViewById(R.id.ed5_signup);
        final EditText ed6=findViewById(R.id.ed6_signup);
        final EditText ed7=findViewById(R.id.ed7_signup);
        Button signup=findViewById(R.id.signup_signup);

        ed2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ed7_change=b;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean onetime=true;
                while (check_update){
                    String[][] trash_query;
                    trash_query = DBUtils.select_DB("SELECT MAX(version_id) version_id FROM version WHERE platform='Android'","version_id");
                    if(trash_query!=null) {
                        Globals.onlineversion_id = trash_query[0][0];
                        if (!Globals.onlineversion_id.equals(Globals.version_id)&&onetime) {
                            onetime=false;
                            LogActivity.trash=false;
                            handler.post(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(SignUpActivity.this);
                                    dialog.setTitle("更新");
                                    dialog.setMessage("版本已更新，请退出后重新打开APP");
                                    dialog.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                            LogActivity.finish_.finish();
                                        }
                                    });
                                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialogInterface) {
                                            finish();
                                            LogActivity.finish_.finish();
                                        }
                                    });
                                    dialog.show();

                                }
                            });
                        }
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (!exit_test)
                {
                    temp=null;
                    temp=DBUtils.select_DB("SELECT * FROM members WHERE S_ID='"+ed2.getText().toString()+"'","S_ID");

                    if(temp!=null)
                    {
                        s_id = temp.length <= 0;
                    }
                    else
                        s_id=true;

                    temp=null;
                    String temp1=ed2.getText().toString();
                    if(temp1.length()>6)
                        temp=DBUtils.select_DB("SELECT * FROM majors WHERE MAJ_id='"+temp1.substring(2,7)+"'","MAJ_name");
                    if(temp!=null&&temp.length==1)
                        temp_major=temp1.substring(0,2)+temp[0][0];
//                        Log.e("TAGG","查看："+temp1.substring(0,2)+temp[0][0]);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(!ed1.getText().toString().equals(""))
                                status1.setVisibility(View.VISIBLE);
                            else
                                status1.setVisibility(View.INVISIBLE);
                            if(ed2.getText().toString().length()==10&&s_id)
                                status2.setVisibility(View.VISIBLE);
                            else
                                status2.setVisibility(View.INVISIBLE);

                            if(ed7_change)
                                ed7.setText(temp_major);
                            if(s_id)
                                ed2.setTextColor(Color.BLACK);
                            else
                                ed2.setTextColor(Color.RED);

                            if(!ed3.getText().toString().equals(""))
                                status3.setVisibility(View.VISIBLE);
                            else
                                status3.setVisibility(View.INVISIBLE);
                            if(!ed4.getText().toString().equals(""))
                                status4.setVisibility(View.VISIBLE);
                            else
                                status4.setVisibility(View.INVISIBLE);
                            if(!ed5.getText().toString().equals(""))
                                status5.setVisibility(View.VISIBLE);
                            else
                                status5.setVisibility(View.INVISIBLE);
                            if(ed6.getText().toString().equals(ed5.getText().toString())&&!ed6.getText().toString().equals(""))
                                status6.setVisibility(View.VISIBLE);
                            else
                                status6.setVisibility(View.INVISIBLE);
                            if(!ed7.getText().toString().equals(""))
                                status7.setVisibility(View.VISIBLE);
                            else
                                status7.setVisibility(View.INVISIBLE);
                            global = status1.getVisibility() == View.VISIBLE && status2.getVisibility() == View.VISIBLE &&
                                    status3.getVisibility() == View.VISIBLE && status4.getVisibility() == View.VISIBLE &&
                                    status5.getVisibility() == View.VISIBLE && status6.getVisibility() == View.VISIBLE &&
                                    status7.getVisibility() == View.VISIBLE;
//                            Log.e("TAGG","查看："+global);
                        }
                    });
                }
            }
        }).start();

        signup.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        status=0;
                        if(global)
                        {
                            temp_log=ed1.getText().toString();
                            status = DBUtils._DB("INSERT INTO members (MGR,S_ID,`NAME`,QQ,TEL,MAJOR) VALUES ('1','"
                                    +ed2.getText().toString() +"','"
                                    +ed1.getText().toString() +"','"
                                    +ed3.getText().toString()+"','"
                                    +ed4.getText().toString()+"','"
                                    +ed7.getText().toString()+"')");
                            if(status==1)
                                status=DBUtils._DB("INSERT INTO admin VALUES ('"
                                        +ed2.getText().toString()+"','"
                                        +ed5.getText().toString()+"')");
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder=new AlertDialog.Builder(SignUpActivity.this);
                                builder.setTitle("提示：");
                                String string;

                                if(status==1)
                                {
                                    string="恭喜你，欢迎加入";
                                    Globals.logs_thread(ed2.getText().toString(),"注册账户","欢迎你！ "+temp_log);
                                    builder.setIcon(R.mipmap.ic_launcher);
                                }
                                else if(global)
                                    string="链路或服务器故障，稍后再试";
                                else
                                    string="请确认填写信息是否有误,主键冲突";

                                builder.setMessage(string);
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(status==1)
                                            finish();
                                    }
                                });
                                builder.show();
                            }
                        });

                    }
                }).start();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exit_test=true;
        check_update=false;
    }
}
