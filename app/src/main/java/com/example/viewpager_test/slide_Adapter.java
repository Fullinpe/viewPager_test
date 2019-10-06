package com.example.viewpager_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class slide_Adapter extends RecyclerView.Adapter<slide_Adapter.InnerHolder> {

    private Context context;
    private List<Map<String, Object>> list;

    public slide_Adapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slide_item, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = SlideRecycler.getScreenWidth() + SlideRecycler.dp2px(180);
        view.setLayoutParams(layoutParams);

        View main = view.findViewById(R.id.slide_tv);
        ViewGroup.LayoutParams mainLayoutParams = main.getLayoutParams();
        mainLayoutParams.width = SlideRecycler.getScreenWidth();
        main.setLayoutParams(mainLayoutParams);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {

        Map<String, Object> map = list.get(position);
        holder.slideT.setText((String) map.get("name"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        TextView slideT;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            slideT = itemView.findViewById(R.id.slide_tv);
        }
    }
}
