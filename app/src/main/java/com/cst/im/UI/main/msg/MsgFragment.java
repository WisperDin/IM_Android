package com.cst.im.UI.main.msg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cst.im.R;
import com.cst.im.UI.main.chat.ChatActivity;

import java.util.LinkedList;

public class MsgFragment extends Fragment implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {


    private Context context;
    //存放消息列表信息的数据
    private LinkedList<ChatItem> chatItems=null;
    private MyAdapter myAdapter=null;
    //消息列表
    private ListView chat_lv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg, container, false);
        chatItems=new LinkedList<ChatItem>();
        chat_lv=(ListView)view.findViewById(R.id.chat_list);
        chatItems.add(new ChatItem(R.drawable.msg_icon,"21:55","小一","小一你好吗？"));
        chatItems.add(new ChatItem(R.drawable.msg_icon,"21:56","小二","小二你好吗？"));
        chatItems.add(new ChatItem(R.drawable.msg_icon,"21:57","小三","小三你好吗？"));
        chatItems.add(new ChatItem(R.drawable.msg_icon,"21:58","小四","小四你好吗？"));
        chatItems.add(new ChatItem(R.drawable.msg_icon,"21:59","小五","小五你好吗？"));
        chatItems.add(new ChatItem(R.drawable.msg_icon,"22:00","小六","小六你好吗？"));

        myAdapter=new MyAdapter(chatItems,getActivity());
        chat_lv.setAdapter(myAdapter);

        chat_lv.setOnItemClickListener(this);    //设置单击监听，接口实现
        chat_lv.setOnItemLongClickListener(this);  //设置长按监听，接口实现
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Intent it = new Intent(getActivity(), ChatActivity.class);
        startActivity(it);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        //Toast.makeText(this, "item的长按" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
        return true;
    }
}
