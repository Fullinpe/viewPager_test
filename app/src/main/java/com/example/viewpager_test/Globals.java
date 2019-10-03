package com.example.viewpager_test;

import android.content.Context;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class Globals {

    public static String S_ID = "1713206317";
    public static int MGR = 0;
    public static List<Map<String, Object>> list;

    public static void maketoast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setText(msg);
        toast.show();
    }
}
