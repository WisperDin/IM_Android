package com.cst.im.UI.main.friend;

import android.app.Dialog;
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
import com.cst.im.presenter.IFriendPresenter;
import com.cst.im.presenter.IFriendPresenterCompl;
import com.cst.im.view.IFriendView;

import java.util.ArrayList;

import static com.cst.im.UI.LoginActivity.friendlist;

public class FriendViewFragment extends Fragment implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

//    private static ArrayList <String> friendlist=new ArrayList<String>();
//    private IFriendPresenter myfriend=new IFriendPresenterCompl(this);
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        //TextView textView = (TextView) view.findViewById(R.id.fragment_text_view);
        //textView.setText("ok");

        //myfriend=new IFriendPresenterCompl(this);
//        myfriend.Getfriendlist("lzy");
        ListView Friendlistview = (ListView)  view.findViewById(R.id.lv_friend);
        MyCustomAdapter adapter = new MyCustomAdapter(this.getActivity());
        AddFriendName addfriend=new AddFriendName();
        System.out.println("获取list："+friendlist.toString());
        for(int i=0;i<friendlist.size();i++){
            addfriend.SortAndAdd(friendlist.get(i));
        }
        for (int i = 0; i < addfriend.getTittle().length; i++) {
            //忽略第二个参数，加了图片也不会显示出来，没用的，不用它会出bug
            if (addfriend.getFriendname().get(addfriend.getTittle()[i]).size() != 0) {
                adapter.addSeparatorItem(addfriend.getTittle()[i], R.drawable.friend_icon);
                for (int k = 0; k <addfriend.getFriendname().get(addfriend.getTittle()[i]).size(); k++) {
                    adapter.addItem(addfriend.getFriendname().get(addfriend.getTittle()[i]).get(k), R.drawable.friend_icon);
                }
            }
        }
        Friendlistview.setAdapter(adapter);
        Friendlistview.setOnItemClickListener(this);    //设置单击监听，接口实现
        Friendlistview.setOnItemLongClickListener(this);  //设置长按监听，接口实现
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Intent intent = new Intent(getActivity(), friendinformationActivity.class);
        getActivity().startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        final Customdialog.Builder builder = new Customdialog.Builder(getActivity());
        final Dialog dialog=builder.create();
        dialog.show();
        builder.gettextview().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("click");
                dialog.cancel();
                Intent intent = new Intent(getActivity(), friendremarkActivity.class);
                startActivity(intent);
            }
        });
        return true;
    }
//    @Override
//    public void onRecvMsg(ArrayList<String> list){
//        this.friendlist=list;
//        System.out.println("运行");
//    }
}
