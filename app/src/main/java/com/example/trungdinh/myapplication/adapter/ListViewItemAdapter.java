package com.example.trungdinh.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.trungdinh.myapplication.R;
import com.example.trungdinh.myapplication.models.Message;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by TrungDinh on 3/22/2017.
 */

public class ListViewItemAdapter extends BaseAdapter {

    List<Message> list;
    Context context;
    LayoutInflater layoutInflater;
    public ListViewItemAdapter(Context mcontext , List<Message> list){
        this.list = list;
        this.context = mcontext;
        layoutInflater = LayoutInflater.from(mcontext);

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = layoutInflater.inflate(R.layout.item_listview, parent , false);

        TextView tvName = (TextView) view.findViewById(R.id.name);
        TextView tvMessageText =  (TextView) view.findViewById(R.id.messageText);
        TextView tvTime = (TextView) view.findViewById(R.id.messagetime);
        CircleImageView imageView = (CircleImageView) view.findViewById(R.id.imgSong);

        if(list.size() > 0){
            tvName.setText(list.get(position).getName());
            tvMessageText.setText(list.get(position).getMessageText());
            tvTime.setText(list.get(position).getTime());
            Picasso.with(context).load(list.get(position).getImages()).error(R.drawable.images).into(imageView);
        }


        return view;
    }
}
