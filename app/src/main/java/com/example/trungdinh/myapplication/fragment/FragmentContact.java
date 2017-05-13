package com.example.trungdinh.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trungdinh.myapplication.R;
import com.example.trungdinh.myapplication.adapter.RecyclerHeaderItemAdapter;
import com.example.trungdinh.myapplication.models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by TrungDinh on 5/8/2017.
 */

public class FragmentContact extends Fragment {

    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    RecyclerView recyclerView;
    RecyclerHeaderItemAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contact, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        String nameMy = getArguments().getString("nameMy");
        List listItems = getList(nameMy);

        mAdapter = new RecyclerHeaderItemAdapter(getActivity(), listItems);
        recyclerView.setAdapter(mAdapter);

        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        recyclerView.addItemDecoration(headersDecor);

        //  Add decoration for dividers between list items
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        StickyRecyclerHeadersTouchListener touchListener = new StickyRecyclerHeadersTouchListener(recyclerView, headersDecor);
        recyclerView.addOnItemTouchListener(touchListener);


        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });




        return view;

    }

    private List<User> getList(final String name) {
        final List<User> list = new ArrayList<User>();
        root.child("contact").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                if(!user.getName().equals(name)){
                    list.add(user);
                    if (list.size() > 0) {
                        Collections.sort(list, new Comparator<User>() {
                            @Override
                            public int compare(User o1, User o2) {
                                return o1.getName().compareTo(o2.getName());
                            }
                        });
                    }
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("vodayonChildChanged","voday");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("vodayoChildRemoved","voday");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("vodayonChildMoved","voday");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("vodayonCancelled","voday");
            }
        });




        return list;
    }

}
