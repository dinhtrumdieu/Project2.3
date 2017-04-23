package com.example.trungdinh.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.trungdinh.myapplication.adapter.ChatArrayAdapter;
import com.example.trungdinh.myapplication.models.ChatMessage;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TrungDinh on 3/24/2017.
 */

public class ChatActivity extends AppCompatActivity {

    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    FloatingActionButton  btChat;
    EditText edtInput;
    ListView lvChat;
    ChatArrayAdapter adapter;
    List<ChatMessage> list;
    String id , idMy;

    int check = 0;

    static int RESULT_LOAD_IMAGES = 1;

    // floating animation
    FloatingActionButton fabChoose , fabFB , fabYoutube , fabCamera,fabGoogle;
    Animation fabOpen, fabClose , fabRAnti , fabRclock;
    boolean isOpen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_chat);

        id = getIntent().getStringExtra("IdFriend");
        idMy = getIntent().getStringExtra("IdMy");
        // ánh xạ
        AnhXa();
        list = new ArrayList<>();
        adapter = new ChatArrayAdapter(this , R.layout.chat,list);
        lvChat.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lvChat.setAdapter(adapter);
        // scroll listview
        lvChat.setStackFromBottom(false);

        root.child("message").child(idMy).child(id).push().setValue(new ChatMessage("Hello Định","Ngọc Thạch",false,false));
        root.child("message").child(id).child(idMy).push().setValue(new ChatMessage("Hello Định","Ngọc Thạch",true,false));

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

                    // hiển thị icon
                    fabFB.setVisibility(View.VISIBLE);
                    fabYoutube.setVisibility(View.VISIBLE);
                    fabGoogle.setVisibility(View.VISIBLE);
                    fabCamera.setVisibility(View.VISIBLE);
                    // set animationn
                    fabFB.startAnimation(fabClose);
                    fabYoutube.startAnimation(fabClose);
                    fabGoogle.startAnimation(fabClose);
                    fabCamera.startAnimation(fabClose);
                    fabChoose.startAnimation(fabRAnti);
                    fabFB.setClickable(false);
                    isOpen = false;
                }else{
                    // hiển thị icon
                    fabFB.setVisibility(View.GONE);
                    fabYoutube.setVisibility(View.GONE);
                    fabGoogle.setVisibility(View.GONE);
                    fabCamera.setVisibility(View.GONE);

                    // set animation
                    fabFB.startAnimation(fabOpen);
                    fabYoutube.startAnimation(fabOpen);
                    fabGoogle.startAnimation(fabOpen);
                    fabCamera.startAnimation(fabOpen);
                    fabChoose.startAnimation(fabRclock);
                    fabFB.setClickable(true);
                    isOpen = true;
                }
            }
        });

        fabFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getOpenFacebookIntent(ChatActivity.this);
                startActivity(intent);
            }
        });

        fabYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getOpenYouTubeIntent(ChatActivity.this);
                startActivity(intent);
            }
        });

        fabGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getOpenGPlusIntent(ChatActivity.this);
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
        // lấy dữ liệu
        root.child("message").child(idMy).child(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                    list.add(message);
                    // Log.d("size","giatri"+list.size());
                    if(list.size() > 9 && check  == 0){
                        check = 1;
                        lvChat.setStackFromBottom(true);
                    }
                    Log.d("voday1","giatri");
                    adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("voday","voday");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        btChat.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  root.child("message").child(idMy).child(id).push().setValue(new ChatMessage(edtInput.getText().toString(),"Trung Định",false,false));
                  root.child("message").child(id).child(idMy).push().setValue(new ChatMessage(edtInput.getText().toString(),"Trung Định",true,false));
                  edtInput.setText("");
              }
        });

        edtInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    root.child("message").child(idMy).child(id).push().setValue(new ChatMessage(edtInput.getText().toString(),"Trung Định",false,false));
                    root.child("message").child(id).child(idMy).push().setValue(new ChatMessage(edtInput.getText().toString(),"Trung Định",true,false));
                    edtInput.setText("");
                    return true;
                }
                return false;
            }
        });
    }

    public void AnhXa(){
        btChat = ( FloatingActionButton) findViewById(R.id.btChat);
        edtInput = (EditText) findViewById(R.id.input);
        lvChat = (ListView) findViewById(R.id.list_chat);
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
