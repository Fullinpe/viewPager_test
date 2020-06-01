package com.example.viewpager_test;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class LogActivity extends AppCompatActivity {

    String[][] strings;
    Handler handler = new Handler();
    boolean trouble = true;

    static boolean trash = true;

    @SuppressLint("StaticFieldLeak")
    public static LogActivity finish_;
    static int max_ = 0;
    private boolean check_update=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        max_ = getResources().getInteger(R.integer.max_pro);
        Log.e("MAC", Globals.device_mac);

        final EditText ed1 = findViewById(R.id.s_id);
        final EditText ed2 = findViewById(R.id.password);
        Button sign_up = findViewById(R.id.sign_up);
        Button sign_in = findViewById(R.id.sign_in);



        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Objects.requireNonNull(LogActivity.this), SignUpActivity.class);
                startActivity(intent);
                finish_ = LogActivity.this;
            }
        });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trouble) {
                    trouble = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            strings = null;
                            strings = DBUtils.select_DB("SELECT * FROM admin WHERE S_ID='"
                                    + ed1.getText().toString() + "' AND Password='"
                                    + ed2.getText().toString() + "'", "S_ID");

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (strings != null) {
                                        if (strings.length > 0) {
                                            Globals.logs_thread(ed1.getText().toString(), "登录账户", "你好啊！");
                                            Intent intent = new Intent();
                                            Globals.S_ID=ed1.getText().toString();
                                            intent.setClass(Objects.requireNonNull(LogActivity.this), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(LogActivity.this);
                                            builder.setTitle("提示：");
                                            builder.setMessage("请确认填写学号或密码是否有误");
                                            builder.setPositiveButton("确定", null);
                                            builder.show();

                                            trouble = true;
                                        }
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LogActivity.this);
                                        builder.setTitle("提示：");
                                        builder.setMessage("请确认网络链路正确");
                                        builder.setPositiveButton("确定", null);
                                        builder.show();

                                        trouble = true;
                                    }
                                }
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(LogActivity.this, "疯狂加载中...", Toast.LENGTH_LONG).show();
                }

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean onetime = true;
                while (check_update) {
                    String[][] trash_query;
                    trash_query = DBUtils.select_DB("SELECT MAX(version_id) version_id FROM version WHERE platform='Android'", "version_id");
                    if (trash_query != null) {
                        Globals.onlineversion_id = trash_query[0][0];
                        if (!Globals.onlineversion_id.equals(Globals.version_id) && onetime) {
                            onetime = false;
                            LogActivity.trash = false;
                            handler.post(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(LogActivity.this);
                                    dialog.setTitle("更新");
                                    dialog.setMessage("版本已更新，请退出后重新打开APP");
                                    dialog.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    });
                                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialogInterface) {
                                            finish();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        check_update=false;
    }
}
