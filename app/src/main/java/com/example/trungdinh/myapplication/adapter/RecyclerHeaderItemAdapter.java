package com.example.trungdinh.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trungdinh.myapplication.ChatActivity;
import com.example.trungdinh.myapplication.R;
import com.example.trungdinh.myapplication.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by TrungDinh on 5/8/2017.
 */

public class RecyclerHeaderItemAdapter extends RecyclerItemAdapter implements StickyRecyclerHeadersAdapter {
    private List<User> mList;
    private Context context;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser usermail = auth.getCurrentUser();


    public RecyclerHeaderItemAdapter(Context context, List<User> list) {
        this.mList = list;
        this.context = context;
    }

    @Override
    public long getHeaderId(int position) {
        if (position == 0) {
            return -1;
        } else {
            return getItemId(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header, parent, false);
        return new ItemHeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHeaderViewHolder) {
            if (getItem(position).getName() != null) {
                String header = String.valueOf(getItem(position).getName().charAt(0));
                Log.d("giatriHeader",header);
                ((ItemHeaderViewHolder) holder).header.setText(header);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final User itemModel = mList.get(position);
        ((ItemViewHolder) holder).name.setText(itemModel.getName());
        String imageUrl = itemModel.getImages();
        if (imageUrl != null) {
            Picasso.with(context).load(itemModel.getImages()).error(R.drawable.images).into(((ItemViewHolder) holder).imageView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("IdFriend",itemModel.getId());
                intent.putExtra("IdMy",usermail.getUid());
                intent.putExtra("name",itemModel.getName());
                intent.putExtra("nameMy",usermail.getEmail().split("@")[0]);
                intent.putExtra("images",itemModel.getImages());
                context.startActivity(intent);
                Log.d("giatriIDMy","IDfriend"+itemModel.getId()+"idMy"+usermail.getUid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public User getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        CircleImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            imageView = (CircleImageView) itemView.findViewById(R.id.imageView);


        }
    }

    public static class ItemHeaderViewHolder extends RecyclerView.ViewHolder {

        TextView header;

        public ItemHeaderViewHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.textHeader);
        }
    }
}

