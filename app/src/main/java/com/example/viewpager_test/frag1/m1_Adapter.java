package com.example.viewpager_test.frag1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewpager_test.Globals;
import com.example.viewpager_test.R;
import com.example.viewpager_test.SlideRecycler;

import java.util.List;
import java.util.Map;

public class m1_Adapter extends RecyclerView.Adapter<m1_Adapter.InnerHolder> {

    private Context context;
    private List<Map<String, Object>> list;

    public m1_Adapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.m1_item, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = SlideRecycler.getScreenWidth() + SlideRecycler.dp2px(160);
        view.setLayoutParams(layoutParams);

        View main = view.findViewById(R.id.m1_i_main);
        ViewGroup.LayoutParams mainLayoutParams = main.getLayoutParams();
        mainLayoutParams.width = SlideRecycler.getScreenWidth();
        main.setLayoutParams(mainLayoutParams);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, final int position) {

        final Map<String, Object> map = list.get(position);
        holder.task.setText((String) map.get("task"));
        holder.major.setText((String) map.get("major"));
        holder.name.setText((String) map.get("name"));
        holder.mgr.setText((String) map.get("mgr"));
        holder.qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Globals.MGR != 0) {
                    if (Globals.isInstalled(context, "com.tencent.mobileqq"))
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + map.get("qq") + "&version=1")));
                    else if (Globals.isInstalled(context, "com.tencent.tim"))
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + map.get("qq") + "&version=1")));
                    else
                        Toast.makeText(context, "本机未安装QQ应用", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "你还不是成员，无法快捷通讯及获取更多信息", Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.tel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                if (Globals.MGR!=0) {
                    context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + map.get("tel"))));
                } else {
                    Toast.makeText(context, "你还不是成员，无法快捷通讯及获取更多信息", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        TextView major, name, mgr, task;
        Button qq, tel;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.m1_i_task);
            major = itemView.findViewById(R.id.major_m1_i);
            name = itemView.findViewById(R.id.name_m1_i);
            mgr = itemView.findViewById(R.id.mgr_m1_i);
            qq = itemView.findViewById(R.id.m1_i_qq);
            tel = itemView.findViewById(R.id.m1_i_tel);
        }
    }
}

//package com.example.viewpager_test.frag1;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import com.example.viewpager_test.Globals;
//import com.example.viewpager_test.R;
//
//import java.util.List;
//import java.util.Map;
//
//public class m1_Adapter extends BaseAdapter {
//
//    List<Map<String,Object>> list;
//    private LayoutInflater inflater;
//    public m1_Adapter(Context context,List<Map<String, Object>> list) {
//        this.inflater = LayoutInflater.from(context);
//        this.list = list;
//    }
//
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return list.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        @SuppressLint({"ViewHolder", "InflateParams"}) View view1=inflater.inflate(R.layout.m1_item,null);
//
//        TextView major=view1.findViewById(R.id.major_m1_i);
//        TextView name=view1.findViewById(R.id.name_m1_i);
//        TextView mgr=view1.findViewById(R.id.job_m1_i);
//
//        Map<String,Object> map= Globals.list.get(i);
//        major.setText((String) map.get("major"));
//        name.setText((String) map.get("name"));
//        mgr.setText((String) map.get("mgr"));
//
//        return view1;
//    }
//}
