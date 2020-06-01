package com.example.viewpager_test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.viewpager_test.frag1.m1Fragment;
import com.example.viewpager_test.frag1.m1_Adapter;
import com.example.viewpager_test.frag2.m2Fragment;
import com.example.viewpager_test.frag3.m3Fragment;
import com.example.viewpager_test.frag4.m4Fragment;
import com.example.viewpager_test.mainf.mainFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private boolean check_update = true;
    private boolean exit_out = true;

    int down_percent = 0;
    private boolean mIsCancel = false;
    private ProgressBar progressBar;
    private Dialog dialog;
    private File apkFile;
    public static MainActivity mainActivity;

    @SuppressLint("HandlerLeak")
    Handler handler_log = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    progressBar.setProgress(down_percent);
                    break;
                case 2:
                    dialog.dismiss();
                    Globals.installAPK(MainActivity.this);
            }
        }
    };
    private boolean bool1, bool2, bool3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        final PackageManager pm = getPackageManager();

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 0x11);


        Globals.sign_in = false;

        Globals.device_mac = Globals.getLocalMac();

        final mainFragment mf = new mainFragment();


        dialog = new Dialog(MainActivity.this);
        progressBar = new ProgressBar(MainActivity.this, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
//                progressBar.setProgress(0);
        dialog.setContentView(progressBar);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mIsCancel = true;
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!(bool1 && bool2 && bool3)) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bool1 = (PackageManager.PERMISSION_GRANTED ==
                            pm.checkPermission("android.permission.READ_EXTERNAL_STORAGE", "com.example.viewpager_test"));
                    bool2 = (PackageManager.PERMISSION_GRANTED ==
                            pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "com.example.viewpager_test"));
                    bool3 = (PackageManager.PERMISSION_GRANTED ==
                            pm.checkPermission("android.permission.CALL_PHONE", "com.example.viewpager_test"));
                }

                String[][] temp;
                temp = DBUtils.select_DB("SELECT MAX(version_id) version_id FROM version WHERE platform='Android'", "version_id");
                InputStream[] is;
                if (temp != null) {
                    Globals.onlineversion_id = temp[0][0];
                    if (!Globals.onlineversion_id.equals(Globals.version_id)) {
                        int version_len =
                                Integer.parseInt(DBUtils.select_DB("SELECT OCTET_LENGTH(version_blob) datesize from version " +
                                        "WHERE version_id=(SELECT MAX(version_id) FROM version WHERE platform='Android') AND platform='Android'", "datesize")[0][0]);

                        File dir = new File(Globals.mSavePath);
                        if (!dir.exists()) {
                            if (dir.mkdir())
                                Log.e("LogActivity", "成功创建文件夹");
                            else
                                Log.e("LogActivity", "创建文件夹失败");
                        } else
                            Log.e("LogActivity", "文件夹已存在");

                        is = DBUtils.selectBLOB("SELECT * from version WHERE platform='Android' AND version_id=" + temp[0][0], "version_blob");

                        try {
                            apkFile = new File(Globals.mSavePath, Globals.mVersion_name);
                            FileOutputStream fos = new FileOutputStream(apkFile);
                            int count = 0;
                            byte[] buffer = new byte[1024];
                            while (!mIsCancel) {
                                int numread = is[0].read(buffer);
                                count += numread;
                                // 计算进度条的当前位置
                                down_percent = (int) (((float) count / version_len) * 100);
                                // 更新进度条
                                handler_log.sendEmptyMessage(1);

                                // 下载完成
                                if (numread < 0) {
                                    handler_log.sendEmptyMessage(2);
                                    Log.e("LogActivity", "apk_len:" + version_len);
                                    break;
                                }
                                fos.write(buffer, 0, numread);
                            }
                            fos.close();
                            is[0].close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (Globals.S_ID.equals("")) {
                        dialog.dismiss();
                        String[][] trash_query;
                        trash_query = DBUtils.select_DB("SELECT x.OPER_device,x.S_ID,members.MGR FROM (SELECT OPER_device,S_ID FROM `logs` WHERE `KEY`=(SELECT MAX(`KEY`) FROM `logs` WHERE S_ID=(SELECT S_ID FROM `logs` WHERE `KEY`=(SELECT MAX(`KEY`) FROM `logs` WHERE OPER_device='"
                                        + Globals.device_mac + "' AND TYPE_operation='登录账户')) AND TYPE_operation='登录账户')) AS x LEFT JOIN members ON members.S_ID=x.S_ID",
                                "OPER_device", "S_ID", "MGR");
                        if (trash_query != null && trash_query.length > 0) {
                            if (trash_query[0][0] != null && trash_query[0][0].equals(Globals.device_mac)) {
                                Globals.S_ID = trash_query[0][1];
                                Globals.MGR = Integer.parseInt(trash_query[0][2]);
                                getSupportFragmentManager().beginTransaction().add(R.id.main_ll, mf).commitAllowingStateLoss();
                                Globals.sign_in = true;
                            } else {
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, LogActivity.class);
                                startActivity(intent);
                                check_update=false;
                                finish();
                            }
                        } else {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, LogActivity.class);
                            startActivity(intent);
                            check_update=false;
                            finish();
                        }
                    } else {
                        dialog.dismiss();
                        String[][] trash_query;
                        trash_query = DBUtils.select_DB("SELECT MGR FROM members WHERE S_ID='" + Globals.S_ID + "'",
                                "MGR");
                        if (trash_query != null) {
                            if (trash_query.length > 0) {
                                Globals.MGR = Integer.parseInt(trash_query[0][0]);
                                getSupportFragmentManager().beginTransaction().add(R.id.main_ll, mf).commitAllowingStateLoss();
                                Globals.sign_in = true;
                            } else {
                                //TODO:S_ID问题
                            }
                        } else {
                            //TODO:链路问题
                        }
                    }
                    //TODO:更新后退回&&完成自动登录
                    if (Globals.sign_in) {
                        String[][] init_temp = DBUtils.select_DB("SELECT S_ID,`NAME`,mgr_name MGR,MAJOR,QQ,TEL,TASK,DONE FROM members LEFT JOIN mgr_table ON members.MGR=mgr_table.mgr_id WHERE MGR>0 ORDER BY  members.MGR DESC",
                                "S_ID", "NAME", "MGR", "MAJOR", "QQ", "TEL", "TASK","DONE");
                        Globals.list = new ArrayList<>();
                        if (init_temp != null) {
                            Globals.members=0;
                            Globals.newers=0;
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
                                map.put("done", init_temp[i][7]);
                                Globals.list.add(map);
                                if(!init_temp[i][2].equals("新人")&&!init_temp[i][2].equals("退休"))
                                    Globals.members++;
                                else if(init_temp[i][2].equals("新人"))
                                    Globals.newers++;
                            }
                            handler.post(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    ((m1Fragment) (mf.fragments[0])).count_m1.setText("当前人数：" + Globals.list.size());
                                    ((m1Fragment) (mf.fragments[0])).m1_adapter = new m1_Adapter(MainActivity.this, Globals.list);
                                    ((m1Fragment) (mf.fragments[0])).slideRecycler.setAdapter(((m1Fragment) (mf.fragments[0])).m1_adapter);
                                    ((m1Fragment) (mf.fragments[0])).count_m2.setText("新人："+Globals.newers);
                                    ((m1Fragment) (mf.fragments[0])).count_m3.setText("正式队员："+Globals.members);
                                }
                            });
                        }
                    }
                } else {
                    //当前网络链路错误
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("提示：")
                            .setMessage("请确认网络链路正确")
                            .setPositiveButton("确定", null)
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    finish();
                                }
                            });
                    builder.show();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    while (!check_update) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!Globals.m2_hidden) {
                        String[][] init_temp = DBUtils.select_DB("SELECT * FROM members WHERE MGR>2 AND COMN>0 ORDER BY COMN DESC",
                                "NAME", "ARRIVE", "RECN", "COMN");
                        Globals.online_m2 = new ArrayList<>();
                        if (init_temp != null) {
                            Map<String, Object> map = new HashMap<>();
                            for (int i = 0; i < init_temp.length; i++) {
                                if (i > 0)
                                    map = new HashMap<>();
                                map.put("name", init_temp[i][0]);
                                map.put("arrive", init_temp[i][1]);
                                map.put("recn", init_temp[i][2]);
                                map.put("comn", init_temp[i][3]);
                                Globals.online_m2.add(map);
                            }
                            if (((m2Fragment) (mf.fragments[1])).mViewModel.online_data != null)
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((m2Fragment) (mf.fragments[1])).mViewModel.online_data.setValue(Globals.online_m2);
                                    }
                                });
                        }
                    }

                    if (!Globals.m3_hidden) {
                        String[][] init_temp  = DBUtils.select_DB("SELECT members.S_ID,members.`NAME`,members.RECN,members.MAJOR,`logs`.S_ID SID FROM members LEFT JOIN `logs` ON `logs`.`COMMENT`=members.S_ID AND `logs`.TYPE_operation='纳新投票' AND DATE_FORMAT(OPER_time,'%Y%j')=DATE_FORMAT(CURRENT_DATE,'%Y%j') AND `logs`.S_ID='"
                                +Globals.S_ID+"' WHERE MGR=1", "S_ID", "NAME", "RECN","MAJOR","SID");
                        Globals.online_m3 = new ArrayList<>();
                        if (init_temp != null) {
                            Map<String, Object> map = new HashMap<>();
                            for (int i = 0; i < init_temp.length; i++) {
                                if (i > 0)
                                    map = new HashMap<>();
                                map.put("num", init_temp[i][0]);
                                map.put("name", init_temp[i][1]);
                                map.put("accept", init_temp[i][2]);
                                map.put("major_n", init_temp[i][3]);
                                map.put("done", init_temp[i][4]);
                                Globals.online_m3.add(map);
                            }
                            if (((m3Fragment) (mf.fragments[2])).mViewModel.online_data != null)
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((m3Fragment) (mf.fragments[2])).mViewModel.online_data.setValue(Globals.online_m3);
                                    }
                                });
                        }
                    }
                    if (!Globals.m4_hidden) {
                        String[][] strings;
                        strings = DBUtils.select_DB("SELECT * FROM members LEFT JOIN mgr_table ON members.MGR=mgr_table.mgr_id WHERE S_ID='"
                                + Globals.S_ID + "'", "NAME", "S_ID", "MAJOR", "mgr_name", "QQ", "TEL","LAST");
                        final String[][] finalStrings = strings;

                        if (mf.fragments!=null&&((m4Fragment) (mf.fragments[3])).tv_1 != null)
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (finalStrings != null) {
                                        if (finalStrings.length == 1) {
                                            ((m4Fragment) (mf.fragments[3])).tv_1.setText(finalStrings[0][0]);
                                            ((m4Fragment) (mf.fragments[3])).tv_2.setText(finalStrings[0][1]);
                                            ((m4Fragment) (mf.fragments[3])).tv_3.setText(finalStrings[0][2]);
                                            ((m4Fragment) (mf.fragments[3])).tv_4.setText(finalStrings[0][3]);
                                            ((m4Fragment) (mf.fragments[3])).tv_5.setText(finalStrings[0][4]);
                                            ((m4Fragment) (mf.fragments[3])).tv_6.setText(finalStrings[0][5]);
                                            ((m4Fragment) (mf.fragments[3])).tv_7.setText(finalStrings[0][6]);
                                        } else if (finalStrings.length > 1) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                            builder.setTitle("提示：");
                                            builder.setMessage("请联系管理员，账号异常");
                                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Objects.requireNonNull(MainActivity.this).finish();
                                                }
                                            });
                                            builder.show();
                                        }
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                        builder.setTitle("提示：");
                                        builder.setMessage("请联系管理员，账号异常");
                                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Objects.requireNonNull(MainActivity.this).finish();
                                            }
                                        });
                                        builder.show();
                                    }
                                }
                            });
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

//        ViewPager viewPager=findViewById(R.id.viewpager);
//        final List<Fragment> fragmentList=new ArrayList<>();
//        fragmentList.add(new mainFragment());
//        fragmentList.add(new msgFragment());
//        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                return fragmentList.get(position);
//            }
//
//            @Override
//            public int getCount() {
//                return fragmentList.size();
//            }
//
//        });
//
//        viewPager.setCurrentItem(0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Globals.sign_in) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                boolean onetime = true, onetime2 = true;
                while (true) {
                    while (!check_update) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String[][] trash_query = null;
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (Globals.S_ID != null)
                        trash_query = DBUtils.select_DB("SELECT MAX(version_id) version_id FROM version WHERE platform='Android' UNION ALL " +
                                "SELECT OPER_device FROM `logs` WHERE `KEY`=(SELECT MAX(`KEY`) " +
                                "OPER_device FROM `logs` WHERE S_ID='" + Globals.S_ID
                                + "' AND TYPE_operation='登录账户') UNION ALL SELECT PRI FROM members WHERE S_ID='" + Globals.S_ID
                                + "' UNION ALL SELECT PRI1 FROM members WHERE S_ID='" + Globals.S_ID
                                + "' UNION ALL SELECT MGR FROM members WHERE S_ID='" + Globals.S_ID
                                + "'", "version_id");
                    if (trash_query != null) {
                        Globals.onlineversion_id = trash_query[0][0];
                        if (!Globals.onlineversion_id.equals(Globals.version_id) && onetime) {
                            onetime = false;
                            handler.post(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
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

                        //TODO:cha
//                        for (String[] strings : trash_query)
//                            Log.e("TG", "" + strings[0]);

                        if (trash_query.length > 1 && !trash_query[1][0].equals(Globals.device_mac) && onetime2) {
                            onetime2 = false;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                    dialog.setTitle("注销");
                                    dialog.setMessage("你的账号在别处登录");
                                    dialog.setNegativeButton("注销", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (exit_out) {
                                                exit_out = false;
                                                finish();
                                                startActivity(new Intent(MainActivity.this, LogActivity.class));
                                            }
                                            Globals.S_ID = "";
                                            Globals.sign_in = false;
                                            Globals.MGR = 0;
                                            check_update = false;
                                        }
                                    });
                                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialogInterface) {
                                            if (exit_out) {
                                                exit_out = false;
                                                finish();
                                                startActivity(new Intent(MainActivity.this, LogActivity.class));
                                            }
                                            Globals.S_ID = "";
                                            Globals.sign_in = false;
                                            Globals.MGR = 0;
                                            check_update = false;
                                        }
                                    });
                                    dialog.show();
                                }
                            });
                        }
                        if (trash_query.length > 4) {
                            final int temp = Integer.parseInt(trash_query[2][0]);
                            handler.post(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.P)
                                @Override
                                public void run() {
                                    if (((m2Fragment) (mf.fragments[1])).progressBar != null) {
                                        ((m2Fragment) (mf.fragments[1])).progressBar.setProgress(temp);
                                    }
                                }
                            });
                            Globals.pri_able = Integer.parseInt(trash_query[3][0]) == 1;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    if (((m2Fragment) (mf.fragments[1])).raise_tv != null)
                                        ((m2Fragment) (mf.fragments[1])).raise_tv
                                                .setVisibility(Globals.pri_able ? View.VISIBLE : View.INVISIBLE);
                                }
                            });
                            Globals.MGR=Integer.parseInt(trash_query[4][0]);
                        }
                    }
                }
            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        Globals.maketoast(this, "destroy");
        super.onDestroy();
        check_update = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        check_update=false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        check_update=true;
    }
}
