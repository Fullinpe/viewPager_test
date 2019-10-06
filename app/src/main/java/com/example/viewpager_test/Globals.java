package com.example.viewpager_test;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.List;
import java.util.Map;

public class Globals {

    public static String S_ID = "";
    public static int MGR = 0;
    public static boolean sign_in=false;
    public static List<Map<String, Object>> list;
    public static String onlineversion_id="";
    public static String version_id="3";
    public static String device_mac="";
    public static boolean pri_able=false;
    public static String mSavePath = Environment.getExternalStorageDirectory() + "/" + "LOTogether";
    public static String mVersion_name = "temp.apk";

    public static void maketoast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setText(msg);
        toast.show();
    }

    public static boolean isInstalled(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            maketoast(context, "休想和null联系！");
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void logs_thread(final String s_id, final String type_operation, final String comment) {
        if (device_mac != null)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DBUtils._DB("INSERT INTO `logs` (S_ID,TYPE_operation,`COMMENT`,OPER_device) VALUES ('" + s_id + "','" + type_operation + "','" + comment + "','" + device_mac + "')");
                }
            }).start();
        else
            Log.e("mac", "无法获取Mac地址");
    }

    public static void logs_thread_out(final String s_id) {
        if (device_mac != null)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DBUtils._DB("INSERT INTO `logs` (S_ID,TYPE_operation,`COMMENT`,OPER_device) VALUES ('" + s_id + "','登录账户','---','" + "---" + device_mac + "')");
                }
            }).start();
        else
            Log.e("mac", "无法获取Mac地址");
    }

    /**
     * 获取设备MAC 地址 由于 6.0 以后 WifiManager 得到的 MacAddress得到都是 相同的没有意义的内容
     * 所以采用以下方法获取Mac地址
     */
    public static String getLocalMac() {

        String macAddress;
        StringBuilder buf = new StringBuilder();
        NetworkInterface networkInterface;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "";
            }
            byte[] addr = networkInterface.getHardwareAddress();


            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "";
        }
        return macAddress;
    }

    public static void installAPK(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String filePath = new File(Globals.mSavePath, Globals.mVersion_name).getAbsolutePath();
        Log.e("安装文件路径：", filePath);
        File file = new File(filePath);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) {//大于7.0使用此方法
            Uri apkUri = FileProvider.getUriForFile(context, "com.example.viewpager_test.fileprovider", file);///-----ide文件提供者名
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {//小于7.0就简单了
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

}
