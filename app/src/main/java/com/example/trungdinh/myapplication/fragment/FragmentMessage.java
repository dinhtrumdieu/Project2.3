package com.example.trungdinh.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.trungdinh.myapplication.ChatActivity;
import com.example.trungdinh.myapplication.R;
import com.example.trungdinh.myapplication.adapter.CustomListView;
import com.example.trungdinh.myapplication.models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by TrungDinh on 4/11/2017.
 */

public class FragmentMessage extends Fragment {


    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    ListView lvUser;
    ArrayList<User> list;

    CustomListView adapter;

    String idMy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main,container,false);

        lvUser = (ListView) view.findViewById(R.id.list_of_user);

        list = new ArrayList<>();
        adapter = new CustomListView(getActivity(), list);
        lvUser.setAdapter(adapter);

         idMy = getArguments().getString("ID");
        Log.d("ID user:",idMy);
        // lấy dữ liệu từ firebase
       // root.child("users").child(idMy).push().setValue(new User("lMerz6wKW2Sc8oVjfj692jjp6qu2","Trung Định","https://firebasestorage.googleapis.com/v0/b/chatapp-26120.appspot.com/o/mountains.jpg?alt=media&token=f95fcc80-2f98-4039-b330-a66bde049ed3"));
        getDataFromFireBase();



        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("IdFriend",list.get(position).getId());
                intent.putExtra("IdMy",idMy);
                startActivity(intent);
            }
        });


        return view;

    }

    public void getDataFromFireBase(){
        root.child("users").child(idMy).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                User user = dataSnapshot.getValue(User.class);
                list.add(user);
                adapter.notifyDataSetChanged();

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
    }

}
