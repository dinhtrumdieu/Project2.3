package com.example.trungdinh.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by TrungDinh on 4/14/2017.
 */

public class Test extends AppCompatActivity {

    FloatingActionButton fabChoose , fabFB , fabYoutube;
    Animation fabOpen, fabClose , fabRAnti , fabRclock;
    boolean isOpen = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        fabChoose = (FloatingActionButton) findViewById(R.id.fabChoose);
        fabFB = (FloatingActionButton) findViewById(R.id.fabFb);
        fabYoutube = (FloatingActionButton) findViewById(R.id.fabYou);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        fabRAnti = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rote_anticlockwise);
        fabRclock = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rote_clockwise);

        fabChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    Log.d("voday","voday1");
                    fabFB.startAnimation(fabClose);
                    fabYoutube.startAnimation(fabClose);
                    fabChoose.startAnimation(fabRAnti);
                    isOpen = false;
                }else{
                        Log.d("voday","voday2");
                        fabFB.startAnimation(fabOpen);
                        fabYoutube.startAnimation(fabOpen);
                        fabChoose.startAnimation(fabRclock);
                        isOpen = true;
                }
            }
        });



    }
}
