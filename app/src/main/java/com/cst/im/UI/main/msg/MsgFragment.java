package com.cst.im.UI.main.msg;

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
import android.widget.ListView;
import android.widget.Toast;

import com.cst.im.R;
import com.cst.im.UI.main.chat.ChatActivity;
import com.cst.im.presenter.ChatListPresenter;
import com.cst.im.presenter.IChatListPresenter;
import com.cst.im.view.IFragmentView;

public class MsgFragment extends Fragment implements IFragmentView,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    public static MyAdapter myAdapter=null;
    //消息列表
    private ListView chat_lv;
    IChatListPresenter chatListPresenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg, container, false);
        chat_lv=(ListView)view.findViewById(R.id.chat_list);
        //实例化Presenter
        chatListPresenter=new ChatListPresenter();
        //设置自定义Adapter
        myAdapter=new MyAdapter(chatListPresenter.getMsgList(),getActivity());
        chat_lv.setAdapter(myAdapter);

        chat_lv.setOnItemClickListener(this);    //设置单击监听，接口实现
        chat_lv.setOnItemLongClickListener(this);  //设置长按监听，接口实现
        return view;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        Intent it = new Intent(getActivity(), ChatActivity.class);
        //得到被点击的对象
        ChatItem accept = chatListPresenter.getMsgList().get(position);
        //用于设置消息已读
        accept.setRead(true);
        //传送接收者名称，“AcceptName”为key
        Bundle bundle = new Bundle();
        bundle.putString("dstName",accept.getName());
        bundle.putInt("dstID",accept.getID());
        it.putExtras(bundle);
        //Toast.makeText(getActivity(), accept.getName(), Toast.LENGTH_LONG).show();
        startActivity(it);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   final int position, long id) {
        //Toast.makeText(this, "item的长按" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
        //得到被长按的对象
        final ChatItem accept=chatListPresenter.getMsgList().get(position);
        //设置对话框显示的列表名
        final String[] choice=new String[]{"","","删除该聊天"};
        if(accept.isRead()){
            choice[0]="标志为未读";
        }else{
            choice[0]="标志为已读";
        }
        if(accept.isHasTop()){
            choice[1]="取消顶置";
        }else{
            choice[1]="顶置聊天";
        }
        //实例化一个对话框，对消息进行操作
        new AlertDialog.Builder(this.getContext()).setItems(choice,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(choice[i].equals("标志为已读")){
                    accept.setRead(true);
                    //通知listView更新
                    myAdapter.notifyDataSetChanged();
                }else if(choice[i].equals("标志为未读")){
                    accept.setRead(false);
                    //通知listView更新
                    myAdapter.notifyDataSetChanged();
                }else if(choice[i].equals("取消顶置")){
                    chatListPresenter.OffsetTop(accept);
                    //设置为未顶置
                    accept.setHasTop(false);
                    //通知listView更新
                    myAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "已取消顶置", Toast.LENGTH_LONG).show();
                }else if(choice[i].equals("顶置聊天")){
                    chatListPresenter.SetTop(accept);
                    //设置为顶置
                    accept.setHasTop(true);
                    //通知listView更新
                    myAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "已顶置", Toast.LENGTH_LONG).show();
                }else if(choice[i].equals("删除该聊天")){
                    chatListPresenter.getMsgList().remove(position);
                    //通知listView更新
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