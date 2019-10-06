package com.example.viewpager_test;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class pickerDialog extends Dialog {
    public pickerDialog(@NonNull Context context) {
        super(context);
    }

    public static class Builder {
        View mLayout;
        Button ok_b;
        Button none_b;
        TextView textView;
        CheckBox checkBox;
        pickerDialog pickerdialog;
        Handler handler = new Handler();
        Runnable ok_R,none_R;
        String title = "你已选择";
        Context context;
        picker_Adapter picker_adapter;

        public Builder(Context context, List<Map<String,Object>> list) {
            this.context = context;
            pickerdialog = new pickerDialog(context);
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //加载布局文件
            assert inflater != null;
            mLayout = inflater.inflate(R.layout.picker_dia, (ViewGroup) pickerdialog.getCurrentFocus(), false);
            //添加布局文件到 Dialog
            pickerdialog.setContentView(mLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            RecyclerView recyclerView = mLayout.findViewById(R.id.choose_list);
            ok_b = mLayout.findViewById(R.id.ok_choose);
            none_b = mLayout.findViewById(R.id.none_choose);
            textView = mLayout.findViewById(R.id.choose_title);
            checkBox = mLayout.findViewById(R.id.checkBox_choose);
            picker_adapter = new picker_Adapter(context, list) {
                @SuppressLint("SetTextI18n")
                @Override
                public void setTitles(int picks) {
                    textView.setText(title + picks + "人");
                }
            };
            RecyclerView.LayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(picker_adapter);
        }

        public Builder setTitle(String string) {
            title = string;
            return this;
        }

        public List<Boolean> getTorf(){
            return picker_adapter.getTorf();
        }

        public Builder setOk(String string,Runnable runnable) {
            ok_b.setText(string);
            ok_R=runnable;
            return this;
        }
        public Builder setNone(String string,Runnable runnable) {
            none_b.setText(string);
            none_R=runnable;
            return this;
        }

        pickerDialog create() {
            ok_b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pickerdialog.dismiss();
                    ok_R.run();
                }
            });
            none_b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pickerdialog.dismiss();
                    none_R.run();
                }
            });
            pickerdialog.setContentView(mLayout);
            pickerdialog.setCancelable(true);
            pickerdialog.setCanceledOnTouchOutside(false);
            return pickerdialog;
        }
        public Builder setonCancel(final Runnable runnable){
            pickerdialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    runnable.run();
                }
            });
            return this;
        }

        public pickerDialog show() {
            pickerDialog dialog = create();
            dialog.show();
            return dialog;
        }

    }
}
