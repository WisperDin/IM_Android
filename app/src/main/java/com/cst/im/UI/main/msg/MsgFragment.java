package com.cst.im.UI.main.msg;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cst.im.R;

import java.util.LinkedList;

public class MsgFragment extends Fragment {


    private Context context;
    //存放消息列表信息的数据
    private LinkedList<ChatItem> chatItems=null;
    private MyAdapter myAdapter=null;
    //消息列表
    private ListView chat_list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg, container, false);
        chatItems=new LinkedList<ChatItem>();
        chat_list=(ListView)view.findViewById(R.id.chat_list);
        chatItems.add(new ChatItem(R.drawable.msg_icon,"21:55","小一","小一你好吗？"));
        chatItems.add(new ChatItem(R.drawable.msg_icon,"21:56","小二","小二你好吗？"));
        chatItems.add(new ChatItem(R.drawable.msg_icon,"21:57","小三","小三你好吗？"));
        chatItems.add(new ChatItem(R.drawable.msg_icon,"21:58","小四","小四你好吗？"));
        chatItems.add(new ChatItem(R.drawable.msg_icon,"21:59","小五","小五你好吗？"));
        chatItems.add(new ChatItem(R.drawable.msg_icon,"22:00","小六","小六你好吗？"));

        myAdapter=new MyAdapter(chatItems,getActivity());
        chat_list.setAdapter(myAdapter);
        return view;
    }

}
