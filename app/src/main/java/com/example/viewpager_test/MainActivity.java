package com.example.viewpager_test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.viewpager_test.frag1.m1Fragment;
import com.example.viewpager_test.frag1.m1_Adapter;
import com.example.viewpager_test.mainf.mainFragment;
import com.example.viewpager_test.msg.msgFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager=findViewById(R.id.viewpager);
        final List<Fragment> fragmentList=new ArrayList<>();
        fragmentList.add(new msgFragment());
        fragmentList.add(new mainFragment());

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

        });
        viewPager.setCurrentItem(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[][] temp = DBUtils.select_DB("SELECT S_ID,`NAME`,mgr_name MGR,MAJOR,QQ,TEL FROM members LEFT JOIN mgr_table ON members.MGR=mgr_table.mgr_id WHERE MGR>1 ORDER BY  members.MGR DESC",
                        "S_ID", "NAME", "MGR","MAJOR","QQ","TEL");
                Globals.list=new ArrayList<>();
                if(temp!=null) {
                    Map<String, Object> map = new HashMap<>();
                    for (int i = 0; i < temp.length; i++) {
                        if (i > 0)
                            map = new HashMap<>();
                        map.put("sid", temp[i][0]);
                        map.put("name", temp[i][1]);
                        map.put("mgr", temp[i][2]);
                        map.put("major", temp[i][3]);
                        map.put("qq", temp[i][4]);
                        map.put("tel", temp[i][5]);
                        Globals.list.add(map);
                    }
                    handler.post(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            ((m1Fragment)((mainFragment)fragmentList.get(1)).fragments[0]).count_m1.setText("当前人数："+Globals.list.size());
                            m1_Adapter m1_adapter=new m1_Adapter(MainActivity.this,Globals.list);
                            ((m1Fragment)((mainFragment)fragmentList.get(1)).fragments[0]).listView.setAdapter(m1_adapter);
                        }
                    });
                }
            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        Globals.maketoast(this,"destroy");

        super.onDestroy();
        finish();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onPause() {
        this.onDestroy();
    }


}
