package com.example.viewpager_test.frag3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewpager_test.R;

public class m3Fragment extends Fragment {

    private M3ViewModel mViewModel;

    public static m3Fragment newInstance() {
        return new m3Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.m3_fragment, container, false);
        Button button = view.findViewById(R.id.button);
        Log.e("TAG",getActivity().toString());
        RecyclerView recyclerView =view.findViewById(R.id.recycler_test);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(M3ViewModel.class);
        // TODO: Use the ViewModel
    }

}
