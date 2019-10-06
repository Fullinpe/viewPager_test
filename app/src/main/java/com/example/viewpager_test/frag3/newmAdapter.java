package com.example.viewpager_test.frag3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viewpager_test.DBUtils;
import com.example.viewpager_test.Globals;
import com.example.viewpager_test.R;

import java.util.List;
import java.util.Map;

public class newmAdapter extends BaseAdapter {

    private List<Map<String,Object>> list;
    private LayoutInflater inflater;

    public newmAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, final View view, ViewGroup viewGroup) {
        @SuppressLint({"ViewHolder", "InflateParams"}) View view1=inflater.inflate(R.layout.newm_item,null);

        TextView textView1=view1.findViewById(R.id.name_m3_i);
        TextView textView2=view1.findViewById(R.id.num_m3_i);
        TextView textView3=view1.findViewById(R.id.m3_major_name_i);
        TextView textView4=view1.findViewById(R.id.m3_accept_num_i);
        final Button button=view1.findViewById(R.id.accept_b);
        button.setEnabled(false);

        final Map<String,Object> map=list.get(i);
        textView1.setText((String) map.get("name"));
        textView2.setText((String) map.get("num"));
        textView3.setText((String)map.get("major_n"));
        textView4.setText((String)map.get("accept"));

        final Handler handler=new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String[][] strings= DBUtils.select_DB("SELECT `KEY` FROM `logs` WHERE S_ID='"+Globals.S_ID+"' AND TYPE_operation='纳新投票' AND `COMMENT`='"+map.get("num")+"' AND DATE_FORMAT(OPER_time,'%Y%j')=DATE_FORMAT(CURRENT_DATE,'%Y%j')","KEY");
                if(strings!=null) {
                    if (strings.length > 0) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                button.setEnabled(false);
                            }
                        });
                    }else{
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                button.setEnabled(true);
                            }
                        });
                    }
                }
            }
        }).start();

        final boolean[] lock_b = {true};
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(lock_b[0]){
                    lock_b[0] =false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String[][] strings=DBUtils.select_DB("SELECT `KEY` FROM `logs` WHERE S_ID='"+Globals.S_ID+"' AND TYPE_operation='纳新投票' AND `COMMENT`='"+map.get("num")+"'","KEY");
                            if(strings!=null){
                                if(strings.length>0){
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            button.setEnabled(false);
                                            lock_b[0] =true;
                                            Toast.makeText(getView(i,view,null).getContext(),"已为"+ map.get("name")+"投过票，不能再投",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Globals.logs_thread(Globals.S_ID,"纳新投票",(String) map.get("num"));
                                            button.setEnabled(false);
                                            Toast.makeText(getView(i,view,null).getContext(),"已为"+ map.get("name")+"投票",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }
                    }).start();
                }
            }
        });


        return view1;
    }
}
