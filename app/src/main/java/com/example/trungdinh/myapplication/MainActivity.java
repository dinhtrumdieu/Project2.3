package com.example.trungdinh.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.trungdinh.myapplication.adapter.CustomViewPager;
import com.example.trungdinh.myapplication.fragment.FragmentMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TrungDinh on 4/11/2017.
 */

public class MainActivity extends AppCompatActivity {

    TabLayout tablayout;
    ViewPager viewPager;
    List<Fragment> fragments = new ArrayList<>();

    // custom
    CustomViewPager customViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // ánh xạ

            String id = getIntent().getStringExtra("id");
            Bundle bundle = new Bundle();
            bundle.putString("ID",id);
            Log.d("giatriID",id);

        FragmentMessage fragmentMessage1 = new FragmentMessage();
        fragmentMessage1.setArguments(bundle);

        FragmentMessage fragmentMessage2 = new FragmentMessage();
        fragmentMessage2.setArguments(bundle);

        // add vô list
        fragments.add(fragmentMessage1);
        fragments.add(fragmentMessage2);


        viewPager = (ViewPager) findViewById(R.id.viewPagerMain);
        customViewPager = new CustomViewPager(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(customViewPager);

        tablayout = (TabLayout) findViewById(R.id.tabLayoutMain);
        tablayout.setupWithViewPager(viewPager);
        tablayout.getTabAt(0).setIcon(R.drawable.ic_email);
        tablayout.getTabAt(1).setIcon(R.drawable.ic_action_name);

    }
}
