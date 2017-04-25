package com.cst.im.UI.main.friend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cst.im.R;

public class FriendViewFragment extends Fragment implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private String[] string = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };


    public static FriendViewFragment newInstance(){
        FriendViewFragment homeFragment = new FriendViewFragment();
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        //TextView textView = (TextView) view.findViewById(R.id.fragment_text_view);
        //textView.setText("ok");


        ListView Friendlistview = (ListView)  view.findViewById(R.id.lv_friend);
        MyCustomAdapter adapter = new MyCustomAdapter(this.getActivity());

        int size = string.length;
        for (int i = 0; i < size; i++) {
            //忽略第二个参数，加了图片也不会显示出来，没用的，不用它会出bug
            adapter.addSeparatorItem(string[i],R.drawable.friend_icon);
            for (int k = 0; k < 5; k++) {
                adapter.addItem("item " + k,R.drawable.friend_icon);
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
        //Toast.makeText(this, "item的点击" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        //Toast.makeText(this, "item的长按" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
        return true;
    }
}
