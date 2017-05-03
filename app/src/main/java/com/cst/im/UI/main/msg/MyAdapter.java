package com.cst.im.UI.main.msg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cst.im.R;

import java.util.LinkedList;

/**
 * Created by jijinping on 2017/4/24.
 */

//自定义BaseAdapter,绑定数据
public class MyAdapter extends BaseAdapter{

    //多个ChatItem对象，用于存放消息列表的数据
    private LinkedList<ChatItem> chatItems;
    private Context context;
    //初始化消息列表
    public MyAdapter(LinkedList<ChatItem> chatItems,Context context)
    {
        this.chatItems=chatItems;
        this.context=context;
    }
    @Override
    public int getCount() {
        return chatItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null)
        {
            view= LayoutInflater.from(this.context).inflate(R.layout.msg_item,viewGroup,false);
            holder=new ViewHolder();
            holder.img=(ImageView) view.findViewById(R.id.item_icon);
            holder.t_name=(TextView) view.findViewById(R.id.item_name);
            holder.t_msg=(TextView) view.findViewById(R.id.item_msg);
            holder.t_time=(TextView) view.findViewById(R.id.item_time);
            view.setTag(holder);
        }else {
            holder=(ViewHolder)view.getTag();
        }
        holder.img.setBackgroundResource(chatItems.get(i).getIcon());
        holder.t_name.setText(chatItems.get(i).getName());
        holder.t_msg.setText(chatItems.get(i).getLastMsg());
        holder.t_time.setText(chatItems.get(i).getLastTime());
        return view;
    }
    static class ViewHolder{
        ImageView img;
        TextView t_name;
        TextView t_msg;
        TextView t_time;
    }
}