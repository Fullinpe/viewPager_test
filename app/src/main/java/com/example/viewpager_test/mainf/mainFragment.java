package com.example.viewpager_test.mainf;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.viewpager_test.Globals;
import com.example.viewpager_test.R;
import com.example.viewpager_test.frag1.m1Fragment;
import com.example.viewpager_test.frag2.m2Fragment;
import com.example.viewpager_test.frag3.m3Fragment;
import com.example.viewpager_test.frag4.m4Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class mainFragment extends Fragment {

    private MainViewModel mViewModel;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.nav_view);
        fragments = new Fragment[]{new m1Fragment(), new m2Fragment(), new m3Fragment(), new m4Fragment()};
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_layout, fragments[lastfragment]);
        transaction.show(fragments[lastfragment]).commitAllowingStateLoss();
        return view;
    }


    public Fragment[] fragments;
    private int lastfragment = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Globals.current_frag=item.getItemId();
            switch (item.getItemId()) {
                case R.id.navigation_m1:
                    if (lastfragment != 0) {
                        switchFragment(lastfragment, 0);
                        lastfragment = 0;
                    }
                    return true;
                case R.id.navigation_m2:
                    if (lastfragment != 1) {
                        switchFragment(lastfragment, 1);
                        lastfragment = 1;
                    }
                    return true;
                case R.id.navigation_m3:
                    if (lastfragment != 2) {
                        switchFragment(lastfragment, 2);
                        lastfragment = 2;
                    }
                    return true;
                case R.id.navigation_m4:
                    if (lastfragment != 3) {
                        switchFragment(lastfragment, 3);
                        lastfragment = 3;
                    }
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        //隐藏上个Fragment
        transaction.hide(fragments[lastfragment]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.main_layout, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

}
