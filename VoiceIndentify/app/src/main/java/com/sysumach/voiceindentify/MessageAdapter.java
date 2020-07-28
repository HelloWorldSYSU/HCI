package com.sysumach.voiceindentify;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private int resourceId;

    public MessageAdapter(@NonNull Context context, int resource, List<Message> messages) {
        super(context, resource, messages);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Message msg = getItem(position);
        View view;
        ViewHolder viewHolder = new ViewHolder();

        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder.left = view.findViewById(R.id.left_layout);
            viewHolder.right = view.findViewById(R.id.right_layout);
            viewHolder.leftMsg = view.findViewById(R.id.left_message);
            viewHolder.rightMsg = view.findViewById(R.id.right_message);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if(msg.type == 1){
            viewHolder.left.setVisibility(View.VISIBLE);
            viewHolder.right.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(msg.message);
        }else {
            viewHolder.right.setVisibility(View.VISIBLE);
            viewHolder.left.setVisibility(View.GONE);
            viewHolder.rightMsg.setText(msg.message);
        }

        return view;
    }

    class ViewHolder{
        public LinearLayout left;
        public LinearLayout right;
        public TextView leftMsg;
        public TextView rightMsg;
    }
}
