package com.example.viewpager_test;

import android.app.Dialog;
import android.content.Context;
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

public class pickerDialog extends Dialog {
    public pickerDialog(@NonNull Context context) {
        super(context);
    }



    public static class Builder{
        private View mLayout;
        private RecyclerView recyclerView;
        private Button button;
        public TextView textView;
        CheckBox checkBox;
        private pickerDialog pickerDialog;
        private Handler handler = new Handler();
        Context context;
        picker_Adapter picker_adapter;

        public Builder(Context context) {
            this.context = context;
            pickerDialog = new pickerDialog(context);
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //加载布局文件
            assert inflater != null;
            mLayout = inflater.inflate(R.layout.picker_dia, (ViewGroup) pickerDialog.getCurrentFocus(), false);
            //添加布局文件到 Dialog
            pickerDialog.setContentView(mLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            recyclerView = mLayout.findViewById(R.id.choose_list);
            button = mLayout.findViewById(R.id.ok_choose);
            textView = mLayout.findViewById(R.id.choose_title);
            checkBox=mLayout.findViewById(R.id.checkBox_choose);
            picker_adapter= new picker_Adapter(context, Globals.list) {
                @Override
                public void setTitles(int picks) {
                    textView.setText(String.valueOf(picks));
                }
            };
            RecyclerView.LayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(picker_adapter);
        }


        public pickerDialog create() {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Globals.maketoast(context,"keyi显示");
                }
            });
            pickerDialog.setContentView(mLayout);
            pickerDialog.setCancelable(true);
            pickerDialog.setCanceledOnTouchOutside(false);
            return pickerDialog;
        }

        public pickerDialog show() {
            pickerDialog dialog = create();
            dialog.show();
            return dialog;
        }

    }
}
