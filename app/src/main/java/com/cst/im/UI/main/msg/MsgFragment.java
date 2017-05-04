package com.cst.im.UI.main.msg;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.cst.im.R;
import com.cst.im.UI.main.chat.ChatActivity;
import com.cst.im.presenter.ChatListPresenter;
import com.cst.im.view.IFragmentView;

import java.util.LinkedList;

public class MsgFragment extends Fragment implements IFragmentView,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {


    //存放消息列表信息的数据
    private LinkedList<ChatItem> chatItems=null;
    public static MyAdapter myAdapter=null;
    //消息列表
    private ListView chat_lv;;
    ChatListPresenter chatListPresenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg, container, false);
        chatItems=new LinkedList<ChatItem>();
        chat_lv=(ListView)view.findViewById(R.id.chat_list);
        initData(chatItems);

        myAdapter=new MyAdapter(chatItems,getActivity());
        chat_lv.setAdapter(myAdapter);

        chat_lv.setOnItemClickListener(this);    //设置单击监听，接口实现
        chat_lv.setOnItemLongClickListener(this);  //设置长按监听，接口实现
        return view;
    }
    public void initData(LinkedList<ChatItem> chatItems)
    {
        chatItems.add(new ChatItem(R.drawable.msg_icon,"21:55","小一","小一你好吗？",R.drawable.msg_item_redpoint));
        chatItems.add(new ChatItem(R.drawable.msg_icon,"21:56","小二","小二你好吗？",R.drawable.msg_item_redpoint));
        chatItems.add(new ChatItem(R.drawable.msg_icon,"21:57","小三","小三你好吗？",R.drawable.msg_item_redpoint));
        chatItems.add(new ChatItem(R.drawable.msg_icon,"21:58","小四","小四你好吗？",R.drawable.msg_item_redpoint));
        chatItems.add(new ChatItem(R.drawable.msg_icon,"21:59","小五","小五你好吗？",R.drawable.msg_item_redpoint));
        chatItems.add(new ChatItem(R.drawable.msg_icon,"22:00","小六","小六你好吗？",R.drawable.msg_item_redpoint));
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        Intent it = new Intent(getActivity(), ChatActivity.class);
        ChatItem accept = chatItems.get(position);
        //用于设置消息已读
        accept.setRead(true);
       // Toast.makeText(getActivity(),chatItems.get(position).IsRead+" ", Toast.LENGTH_LONG).show();
        //传送接收者名称，“AcceptName”为key
        it.putExtra("AcceptName",accept.getName());
        it.putExtra("AcceptID",accept.getID());
        //Toast.makeText(getActivity(), accept.getName(), Toast.LENGTH_LONG).show();
        startActivity(it);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   final int position, long id) {
        //Toast.makeText(this, "item的长按" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
        final ChatItem accept=chatItems.get(position);
        final String[] choice=new String[]{"","取消顶置","删除该聊天"};
        if(accept.IsRead){
            choice[0]="标志为未读";
        }else{
            choice[0]="标志为已读";
        }
        new AlertDialog.Builder(this.getContext()).setItems(choice,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(choice[i].equals("标志为已读")){
                    accept.setRead(true);
                    myAdapter.notifyDataSetChanged();
                }else if(choice[i].equals("标志为未读")){
                    accept.setRead(false);
                    myAdapter.notifyDataSetChanged();
                }else if(choice[i].equals("取消顶置")){

                }else if(choice[i].equals("删除该聊天")){
                    chatItems.remove(position);
                    myAdapter.notifyDataSetChanged();
                }
            }
        }).create().show();

        return true;
    }

}
//class RedPoint extends Thread{
//    View ChatItemview;
//    public void setChatItemview(View view){
//        ChatItemview=view;
//    }
//    public void run(){
//        while (true){
//            try {
//                sleep(4000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            ImageView imv=(ImageView)ChatItemview.findViewById(R.id.item_red);
//            //设置消息已读
//            imv.setVisibility(ChatItemview.INVISIBLE);
//            break;
//        }
//
//    }
//}