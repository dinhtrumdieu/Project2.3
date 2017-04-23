package com.example.trungdinh.myapplication.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.trungdinh.myapplication.models.ChatMessage;
import com.example.trungdinh.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by TrungDinh on 3/24/2017.
 */

public class ChatArrayAdapter extends ArrayAdapter<ChatMessage>{

    LinearLayout linear;
    List<ChatMessage> list ;
    LayoutInflater layoutInflater ;
    Context context;

    public ChatArrayAdapter(Context context,  int resource , List<ChatMessage> mlist) {
        super(context, resource);
        layoutInflater = LayoutInflater.from(context);
        this.list = mlist;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public ChatMessage getItem(int position) {
        return list.get(position);
    }


    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        View view;
        if(!list.get(position).isType()){

            // nếu tin nhắn gửi không phải là ảnh
            view = layoutInflater.inflate(R.layout.chat,parent,false);
            linear = (LinearLayout) view.findViewById(R.id.message);

            ChatMessage chat = getItem(position);

            TextView tv = (TextView) view.findViewById(R.id.singleMessage);
            tv.setBackgroundResource(chat.isMine() ? R.drawable.border_friend : R.drawable.border_me);
            tv.setText(chat.getMessageText());

            linear.setGravity(chat.isMine()? Gravity.LEFT:Gravity.RIGHT);

            if(chat.isMine()){
                linear.setPadding(0,0,70,0);
            }else{
                linear.setPadding(70,0,10,0);
            }

        }else{

            // nếu tin nhắn gửi là ảnh
             view = layoutInflater.inflate(R.layout.chat_anh,parent,false);
            linear = (LinearLayout) view.findViewById(R.id.message);

            ChatMessage chat = getItem(position);
            ImageView imageView = (ImageView) view.findViewById(R.id.imagesSent);
            Picasso.with(context).load(list.get(position).getMessageText()).into(imageView);

            linear.setGravity(chat.isMine()? Gravity.LEFT:Gravity.RIGHT);
            if(chat.isMine()){
                linear.setPadding(0,0,70,0);
            }else{
                linear.setPadding(70,0,10,0);
            }
        }

        return view;
    }
}
