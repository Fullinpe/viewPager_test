package com.example.viewpager_test.frag1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewpager_test.DBUtils;
import com.example.viewpager_test.Globals;
import com.example.viewpager_test.R;
import com.example.viewpager_test.SlideRecycler;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class m1_Adapter extends RecyclerView.Adapter<m1_Adapter.InnerHolder> {

    private Context context;
    public List<Map<String, Object>> list;
    private Handler handler=new Handler();
    boolean lock_done=true;

    public m1_Adapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.m1_item, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(Globals.MGR==7)
            layoutParams.width = SlideRecycler.getScreenWidth() + SlideRecycler.dp2px(240);
        else
            layoutParams.width = SlideRecycler.getScreenWidth() + SlideRecycler.dp2px(160);
        view.setLayoutParams(layoutParams);

        View main = view.findViewById(R.id.m1_i_main);
        ViewGroup.LayoutParams mainLayoutParams = main.getLayoutParams();
        mainLayoutParams.width = SlideRecycler.getScreenWidth();
        main.setLayoutParams(mainLayoutParams);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InnerHolder holder, final int position) {

        final Map<String, Object> map = list.get(position);
        final int temp_status=Integer.parseInt((String) Objects.requireNonNull(map.get("done")));
        holder.task.setText((String) map.get("task"));
        holder.major.setText((String) map.get("major"));
        holder.name.setText((String) map.get("name"));
        holder.mgr.setText((String) map.get("mgr"));
        if(temp_status==0){
            holder.task.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.done.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            holder.done.setText("完成");
        }else{
            holder.task.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            holder.done.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            holder.done.setText("未完成");
        }
        switch (((String) Objects.requireNonNull(map.get("mgr")))){
            case "新人":
                holder.mgr.setBackgroundColor(context.getResources().getColor(R.color.member0));
                break;
            case "退休":
                holder.mgr.setBackgroundColor(context.getResources().getColor(R.color.member1));
                break;
            case "队员":
                holder.mgr.setBackgroundColor(context.getResources().getColor(R.color.member2));
                break;
            case "结构组长":
            case "硬件组长":
            case "软件组长":
                holder.mgr.setBackgroundColor(context.getResources().getColor(R.color.member3));
                break;
            case "管理员":
                holder.mgr.setBackgroundColor(context.getResources().getColor(R.color.member4));
                break;
        }
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
        holder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lock_done&&Globals.MGR==7){
                    lock_done=false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int rows=DBUtils._DB("UPDATE members SET DONE="+(temp_status==0?1:0)+" WHERE S_ID='"+map.get("sid")+"'");
                            if(rows==1)
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Globals.maketoast(context,"更改状态 成功");
                                        Globals.logs_thread(Globals.S_ID,"管理操作","设置DONE状态="+(temp_status==0?1:0));
                                        lock_done=true;
                                        if(holder.done.getText().toString().equals("未完成")){
                                            holder.task.setTextColor(context.getResources().getColor(R.color.colorAccent));
                                            holder.done.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
                                            holder.done.setText("完成");
                                        }else{
                                            holder.task.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                                            holder.done.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                                            holder.done.setText("未完成");
                                        }
                                    }
                                });
                            else
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Globals.maketoast(context,"更改状态 失败");
                                        lock_done=true;
                                    }
                                });

                        }
                    }).start();
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
        Button qq, tel,done;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.m1_i_task);
            major = itemView.findViewById(R.id.major_m1_i);
            name = itemView.findViewById(R.id.name_m1_i);
            mgr = itemView.findViewById(R.id.mgr_m1_i);
            qq = itemView.findViewById(R.id.m1_i_qq);
            tel = itemView.findViewById(R.id.m1_i_tel);
            done = itemView.findViewById(R.id.m1_i_done);
        }
    }
}
