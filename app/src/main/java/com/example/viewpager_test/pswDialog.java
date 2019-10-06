package com.example.viewpager_test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class pswDialog extends Dialog {
    public pswDialog(@NonNull Context context) {
        super(context);
    }

    public static class Builder {
        private View mLayout;
        private Button button;
        private EditText editText;
        private Runnable runnable_ok;
        private Runnable runnable_error;
        private pswDialog pswdialog;
        private Handler handler = new Handler();
        private int error_s = 3;
        Context context;
        private boolean exit_out=true;

        public Builder(Context context) {
            this.context = context;
            pswdialog = new pswDialog(context);
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //加载布局文件
            assert inflater != null;
            mLayout = inflater.inflate(R.layout.psw_dia, (ViewGroup) pswdialog.getCurrentFocus(), true);
            //添加布局文件到 Dialog
            pswdialog.setContentView(mLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            editText = mLayout.findViewById(R.id.key_dia);
            button = mLayout.findViewById(R.id.ok_button);
        }

        public int getError_s(){
            return error_s;
        }

        public Builder setOk(Runnable runnable_ok) {
            this.runnable_ok = runnable_ok;
            return this;
        }

        public Builder setError(Runnable runnable_error) {
            this.runnable_error = runnable_error;
            return this;
        }

        public Builder setonCancel(final Runnable cancel) {
            pswdialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    cancel.run();
                }
            });
            return this;
        }

        public pswDialog create() {

            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String[][] strings = null;
                            strings = DBUtils.select_DB("SELECT * FROM admin WHERE S_ID='"
                                    + Globals.S_ID + "' AND Password='"
                                    + editText.getText().toString() + "'", "S_ID");
                            if (strings != null) {
                                if (strings.length == 1) {
                                    pswdialog.dismiss();
                                    runnable_ok.run();
                                } else {
                                    error_s--;
                                    if (error_s <= 0) {
                                        pswdialog.dismiss();
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                                                dialog.setTitle("注销");
                                                dialog.setMessage("你的账号在别处登录");
                                                dialog.setNegativeButton("注销", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        if (exit_out) {
                                                            exit_out = false;
                                                            MainActivity.mainActivity.finish();
                                                            Globals.logs_thread_out(Globals.S_ID);
                                                            context.startActivity(new Intent(context, LogActivity.class));
                                                        }
                                                        Globals.S_ID = "";
                                                        Globals.sign_in = false;
                                                        Globals.MGR = 0;
                                                    }
                                                });
                                                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                    @Override
                                                    public void onCancel(DialogInterface dialogInterface) {
                                                        if (exit_out) {
                                                            exit_out = false;
                                                            MainActivity.mainActivity.finish();
                                                            context.startActivity(new Intent(context, LogActivity.class));
                                                        }
                                                        Globals.S_ID = "";
                                                        Globals.sign_in = false;
                                                        Globals.MGR = 0;
                                                    }
                                                });
                                                dialog.show();
                                            }
                                        });
                                    }else {
                                        final int finalError_s = error_s;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                builder.setTitle("提示：");
                                                builder.setMessage("请确认填写密码是否有误,您还有" + finalError_s + "次输入机会");
                                                builder.setPositiveButton("确定", null);
                                                builder.show();
                                            }
                                        });
                                    }
                                }
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
            editText.requestFocus();
            pswdialog.setContentView(mLayout);
            pswdialog.setCancelable(true);
            pswdialog.setCanceledOnTouchOutside(false);
            return pswdialog;
        }

        public pswDialog show() {
            pswDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
