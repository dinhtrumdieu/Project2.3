package com.example.trungdinh.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.trungdinh.myapplication.adapter.ViewPagerAdapter;
import com.example.trungdinh.myapplication.fragment.FragmentContact;
import com.example.trungdinh.myapplication.fragment.FragmentHome;
import com.example.trungdinh.myapplication.fragment.FragmentMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TrungDinh on 4/11/2017.
 */

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    List<Fragment> fragments = new ArrayList<>();

    // custom
    ViewPagerAdapter customViewPager;

    // floating animation
    FloatingActionButton fabChoose , fabFB , fabYoutube , fabCamera,fabGoogle;
    Animation fabOpen, fabClose , fabRAnti , fabRclock;
    boolean isOpen = true;
    static int RESULT_LOAD_IMAGES = 1;


    // bottom navigation
    BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnsearch:
                Toast.makeText(MainActivity.this, "Ban vua chon nut Search",
                        Toast.LENGTH_SHORT).show();
                SearchView sv = (SearchView) item.getActionView();
                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Không hiện tiêu đề
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Hiện nút back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // ánh xạ

            String id = getIntent().getStringExtra("id");
            String name = getIntent().getStringExtra("nameMy");
            Bundle bundle = new Bundle();
            bundle.putString("ID",id);
            bundle.putString("nameMy",name);
            Log.d("giatriID",id);

        FragmentMessage fragmentMessage1 = new FragmentMessage();
        fragmentMessage1.setArguments(bundle);

        FragmentHome fragmentMessage2 = new FragmentHome();
        fragmentMessage2.setArguments(bundle);

        FragmentContact fragmentMessage3 = new FragmentContact();
        fragmentMessage3.setArguments(bundle);

        // add vô list
        fragments.add(fragmentMessage2);
        fragments.add(fragmentMessage1);
        fragments.add(fragmentMessage3);



        viewPager = (ViewPager) findViewById(R.id.viewPagerMain);
        customViewPager = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(customViewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // khi dich chuyen view
            }

            @Override
            public void onPageSelected(int position) {

                MenuItem menuItem = null;
                switch (position){
                    case 0:

                        menuItem = navigation.getMenu().findItem(R.id.navigation_home);
                        menuItem.setChecked(true);
                        break;
                    case 1:

                        menuItem = navigation.getMenu().findItem(R.id.navigation_dashboard);
                        menuItem.setChecked(true);
                        break;
                    case 2:

                        menuItem = navigation.getMenu().findItem(R.id.navigation_notifications);
                        menuItem.setChecked(true);
                }

            }


            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("TAG11",state+"/aaa/");
            }
        });
            // bottom navigarion
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // xử lý float animation
        fabChoose = (FloatingActionButton) findViewById(R.id.fabChoose);
        fabFB = (FloatingActionButton) findViewById(R.id.fabFb);
        fabYoutube = (FloatingActionButton) findViewById(R.id.fabYou);
        fabCamera = (FloatingActionButton) findViewById(R.id.fabCamera);
        fabGoogle = (FloatingActionButton) findViewById(R.id.fabGoogle);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        fabRAnti = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rote_anticlockwise);
        fabRclock = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rote_clockwise);

        fabChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){

                    // set animation
                    fabFB.startAnimation(fabOpen);
                    fabYoutube.startAnimation(fabOpen);
                    fabGoogle.startAnimation(fabOpen);
                    fabCamera.startAnimation(fabOpen);
                    fabChoose.startAnimation(fabRclock);

                    // hiển thị icon
                    fabFB.setVisibility(View.VISIBLE);
                    fabYoutube.setVisibility(View.VISIBLE);
                    fabGoogle.setVisibility(View.VISIBLE);
                    fabCamera.setVisibility(View.VISIBLE);

                    fabFB.setClickable(true);
                    fabYoutube.setClickable(true);
                    fabGoogle.setClickable(true);
                    fabCamera.setClickable(true);

                    isOpen = false;

                }else{

                    // set animationn
                    fabFB.startAnimation(fabClose);
                    fabYoutube.startAnimation(fabClose);
                    fabGoogle.startAnimation(fabClose);
                    fabCamera.startAnimation(fabClose);
                    fabChoose.startAnimation(fabRAnti);

                    // không hiển thị icon
                    fabFB.setVisibility(View.GONE);
                    fabYoutube.setVisibility(View.GONE);
                    fabGoogle.setVisibility(View.GONE);
                    fabCamera.setVisibility(View.GONE);

                    fabFB.setClickable(false);
                    fabYoutube.setClickable(false);
                    fabGoogle.setClickable(false);
                    fabCamera.setClickable(false);

                    isOpen = true;
                }
            }
        });

        fabFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getOpenFacebookIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        fabYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getOpenYouTubeIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        fabGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getOpenGPlusIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent , RESULT_LOAD_IMAGES);
            }
        });


    }

    public static Intent getOpenYouTubeIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.google.android.youtube", 0); //Checks if YT is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/karthikm128")); //Trys to make intent with YT's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/karthikm128")); //catches and opens a url to the desired page
        }
    }

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://page/376227335860239")); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/karthikofficialpage")); //catches and opens a url to the desired page
        }
    }

    public static Intent getOpenGPlusIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.google.android.apps.plus", 0); //Checks if G+ is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://plus.google.com/u/0/+KarthikM128")); //Trys to make intent with G+'s URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://plus.google.com/u/0/+KarthikM128")); //catches and opens a url to the desired page
        }
    }
}
