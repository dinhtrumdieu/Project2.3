package com.example.trungdinh.myapplication.adapter;

import android.support.v7.widget.RecyclerView;

import com.example.trungdinh.myapplication.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by TrungDinh on 5/8/2017.
 */

public abstract class RecyclerItemAdapter extends RecyclerView.Adapter {
    List items = new ArrayList<>();

    RecyclerItemAdapter() {
        setHasStableIds(true);
    }

    public void add(User object) {
        items.add(object);
        notifyDataSetChanged();
    }

    public void add(int index, User object) {
        items.add(index, object);
        notifyDataSetChanged();
    }

    public void addAll(Collection collection) {
        if (collection != null) {
            items.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public void addAll(User... items) {
        addAll(Arrays.asList(items));
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void remove(User object) {
        items.remove(object);
        notifyDataSetChanged();
    }
}

