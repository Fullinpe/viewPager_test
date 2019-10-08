package com.example.viewpager_test.frag3;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewpager_test.DBUtils;
import com.example.viewpager_test.Globals;
import com.example.viewpager_test.R;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.List;
import java.util.Map;

public class m3_Adapter extends RecyclerView.Adapter<m3_Adapter.InnerHolder> {

    private Context context;
    public List<Map<String,Object>> list;
    private boolean lock_b=true;


    public m3_Adapter(Context context,List<Map<String, Object>> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.m3_item, parent, false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InnerHolder holder, final int position) {

        final Map<String,Object> map=list.get(position);
        holder.textView1.setText((String) map.get("name"));
        holder.textView2.setText((String) map.get("num"));
        holder.textView3.setText((String)map.get("major_n"));
        holder.textView4.setText((String)map.get("accept"));
        if(map.get("done")==null)
            holder.button.setBtnColor(context.getResources().getColor(R.color.gray));
        else
            holder.button.setBtnColor(context.getResources().getColor(R.color.colorPrimaryDark));
        holder.button.setEnabled(map.get("done")==null);
        holder.button.enableFlashing(map.get("done")==null);

        final Handler handler=new Handler();
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(lock_b&&Globals.MGR>2){
                    lock_b =false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String[][] strings= DBUtils.select_DB("SELECT `KEY` FROM `logs` WHERE S_ID='"+Globals.S_ID+"' AND TYPE_operation='纳新投票' AND `COMMENT`='"+map.get("num")+"' AND DATE_FORMAT(OPER_time,'%Y%j')=DATE_FORMAT(CURRENT_DATE,'%Y%j')","KEY");
                            if(strings!=null){
                                if(strings.length>0){
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            lock_b =true;
                                            holder.button.setEnabled(false);
                                            Globals.maketoast(context,"已为"+ map.get("name")+"投过票，不能再投");
                                        }
                                    });
                                }else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Globals.logs_thread(Globals.S_ID,"纳新投票",(String) map.get("num"));
                                            lock_b =true;
                                            holder.button.setEnabled(false);
                                            Globals.maketoast(context,"已为"+ map.get("name")+"投票");
                                        }
                                    });
                                }
                            }
                        }
                    }).start();
                }else if(Globals.MGR<3)
                    Globals.maketoast(context,"权限不足，请先成为正式队员 ^_^");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class InnerHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        ShineButton button;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.name_m3_i);
            textView2=itemView.findViewById(R.id.num_m3_i);
            textView3=itemView.findViewById(R.id.m3_major_name_i);
            textView4=itemView.findViewById(R.id.m3_accept_num_i);
            button=itemView.findViewById(R.id.po_image1);
        }
    }
}
