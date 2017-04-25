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
    //找到一行消息的的各个组件，分别为图标、昵称、最新消息、发送时间
    public void setChatItem(int i,View view,int img_id,int name_id,int msg_id,int time_id)
    {
        ImageView img=(ImageView) view.findViewById(img_id);
        TextView t_name=(TextView) view.findViewById(name_id);
        TextView t_msg=(TextView) view.findViewById(msg_id);
        TextView t_time=(TextView) view.findViewById(time_id);
        img.setBackgroundResource(chatItems.get(i).getIcon());
        t_name.setText(chatItems.get(i).getName());
        t_msg.setText(chatItems.get(i).getLastMsg());
        t_time.setText(chatItems.get(i).getLastTime());
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(this.context).inflate(R.layout.msg_item,viewGroup,false);
        setChatItem(i,view,R.id.item_icon,R.id.item_name,R.id.item_msg,R.id.item_time);
        return view;
    }
}
