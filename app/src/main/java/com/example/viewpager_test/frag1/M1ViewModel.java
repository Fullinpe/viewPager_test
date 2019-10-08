package com.example.viewpager_test.frag1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

public class M1ViewModel extends ViewModel {
    public MutableLiveData<List<Map<String,Object>>> online_data;

    public M1ViewModel() {
        online_data=new MutableLiveData<>();
    }

    public LiveData<List<Map<String, Object>>> getOnline_data() {
        return online_data;
    }
}
