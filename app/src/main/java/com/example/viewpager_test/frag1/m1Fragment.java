package com.example.viewpager_test.frag1;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.viewpager_test.R;
import com.example.viewpager_test.SlideRecycler;
import com.hitomi.refresh.view.FunGameRefreshView;

import java.util.Objects;

public class m1Fragment extends Fragment {

    private M1ViewModel mViewModel;
    Handler handler=new Handler();
    private String[][] strings;
    public TextView count_m1;
    public SlideRecycler slideRecycler;

    public static m1Fragment newInstance() {
        return new m1Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.m1_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(M1ViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        count_m1=view.findViewById(R.id.count_m1);
        slideRecycler =view.findViewById(R.id.member_m1);
        slideRecycler.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()),DividerItemDecoration.VERTICAL));
//        final List<Map<String,Object>> list=new ArrayList<>();

        FunGameRefreshView refreshView = view.findViewById(R.id.refresh_hit_block);
        refreshView.setOnRefreshListener(new FunGameRefreshView.FunGameRefreshListener() {
            @Override
            public void onPullRefreshing() {
                // 模拟后台耗时任务
                SystemClock.sleep(2000);
            }

            @Override
            public void onRefreshComplete() {
            }
        });
//        slideRecycler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if(!MainActivity.MGR.equals("0")){
//                    Map<String,Object> map=list.get(i);
//                    if (isGotoable(getActivity(), "com.tencent.mobileqq"))
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin="+map.get("qq")+"&version=1")));
//                    else if(isGotoable(getActivity() ,"com.tencent.tim"))
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin="+map.get("qq")+"&version=1")));
//                    else
//                        Toast.makeText(getActivity(),"本机未安装QQ应用",Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getActivity(),"你还不是成员，无法快捷通讯及获取更多信息",Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//                slideRecycler.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                    @Override
//                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        if(!MainActivity.MGR.equals("0")){
//                            Map<String,Object> map=list.get(i);
//                            startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + map.get("tel"))));
//                        }else{
//                            Toast.makeText(getActivity(),"你还不是成员，无法快捷通讯及获取更多信息",Toast.LENGTH_LONG).show();
//                        }
//                        return true;
//                    }
//                });



//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                strings=null;
//                strings= DBUtils.select_DB("SELECT MAJOR,`NAME`,QQ,TEL,mgr_name FROM members LEFT JOIN mgr_table ON members.MGR=mgr_table.mgr_id WHERE MGR>1 ORDER BY MGR DESC","MAJOR","NAME","mgr_name","QQ","TEL");
//                if(strings!=null)
//                {
//                    Map<String, Object> map = new HashMap<>();
//                    for (int i=0;i<strings.length;i++)
//                    {
//                        if(i>0)
//                            map =new HashMap<>();
//                        map.put("num",strings[i][0]);
//                        map.put("name",strings[i][1]);
//                        map.put("job",strings[i][2]);
//                        map.put("qq",strings[i][3]);
//                        map.put("tel",strings[i][4]);
//                        list.add(map);
//                    }
//                    handler.post(new Runnable() {
//                        @SuppressLint("SetTextI18n")
//                        @Override
//                        public void run() {
//                            count_m1.setText("当前人数："+list.size());
//                            adapter.setList(list);
//                            count_m1.setText("当前人数："+list.size());
//                            slideRecycler.setAdapter(adapter);
//                        }
//                    });
//                }
//
//            }
//        }).start();
    }
}
