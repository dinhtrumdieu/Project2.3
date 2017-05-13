package com.example.trungdinh.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.trungdinh.myapplication.R;
import com.example.trungdinh.myapplication.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by TrungDinh on 4/11/2017.
 */

public class FragmentHome extends Fragment {

    TextView nameBackGround , fullname , email;
    FirebaseAuth auth;

    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home,container,false);
        auth = FirebaseAuth.getInstance();
        FirebaseUser usermail = auth.getCurrentUser();


        nameBackGround = (TextView) view.findViewById(R.id.namebg);
        fullname = (TextView) view.findViewById(R.id.name);
        email = (TextView) view.findViewById(R.id.email);
        email.setText(usermail.getEmail());
        // scroll view
        ScrollView scrollView = (ScrollView) view.findViewById(R.id.scroll);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);

        String idMy = getArguments().getString("ID");


        root.child("user").child(idMy).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                nameBackGround.setText(user.getName());
                fullname.setText(user.getName());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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



        return view;

    }


}
