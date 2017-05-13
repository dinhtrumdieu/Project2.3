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
import com.example.trungdinh.myapplication.adapter.ListViewItemAdapter;
import com.example.trungdinh.myapplication.models.Message;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by TrungDinh on 4/11/2017.
 */

public class FragmentMessage extends Fragment {


    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    ListView lvUser;
    ArrayList<Message> list;

    ListViewItemAdapter adapter;

    String idMy , nameMy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main,container,false);

        lvUser = (ListView) view.findViewById(R.id.list_of_user);
        OverScrollDecoratorHelper.setUpOverScroll(lvUser);


        list = new ArrayList<>();
        adapter = new ListViewItemAdapter(getActivity(), list);
        lvUser.setAdapter(adapter);

        idMy = getArguments().getString("ID");
        nameMy = getArguments().getString("nameMy");
        Log.d("ID user:",idMy);
        // lấy dữ liệu từ firebase
        getDataFromFireBase();



        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
                view.startAnimation(animation);*/
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("IdFriend",list.get(position).getId());
                intent.putExtra("IdMy",idMy);
                intent.putExtra("name",list.get(position).getName());
                intent.putExtra("nameMy",nameMy);
                intent.putExtra("images",list.get(position).getImages());
                startActivity(intent);
            }
        });


        return view;

    }

    public void getDataFromFireBase(){
        root.child("UserMessage").child(idMy).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Message user = dataSnapshot.getValue(Message.class);

                list.add(user);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("vodayononChildChanged","vodaynha");
               Message user = dataSnapshot.getValue(Message.class);
                for(int i = 0; i < list.size() ; i++){
                    if(list.get(i).getId().equals(user.getId())){
                        list.set(i,user);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("vodayonChildRemoved","vodaynha");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("vodayonChildMoved","vodaynha");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("vodayonCancelled","vodaynha");
            }

        });
    }

}
