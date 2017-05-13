package com.cst.im.UI.main.friend;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.cst.im.R;
import com.cst.im.model.FriendModel;

import java.util.ArrayList;



public class FriendViewFragment extends Fragment implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {
    MyCustomAdapter adapter=null;
    private  ArrayList<Integer> letter = new ArrayList<Integer>();//储存标题上的大写字母的位置
     ArrayList<String> NameSequencebylistview= new ArrayList<String>();//记录item上的名字
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        ListView Friendlistview = (ListView)  view.findViewById(R.id.lv_friend);
        ImageView img_search=(ImageView)view.findViewById(R.id.img_search);
        InitView(img_search,Friendlistview);
        Friendlistview.setOnItemClickListener(this);    //设置单击监听，接口实现
        Friendlistview.setOnItemLongClickListener(this);  //设置长按监听，接口实现
        img_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchFriend.class);
                Bundle bundle=new Bundle();
                bundle.putInt("srcId", FriendModel.friendModel.getId());
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }


    public void InitView(ImageView img,ListView lv){
        adapter = new MyCustomAdapter(this.getActivity());
        AddFriendName addfriend=new AddFriendName();
        if(FriendModel.friendModel ==null){
            Log.w("FriendViewFragment","InitView friendModel null");
            return;
        }
        int size= FriendModel.friendModel.getfriendlist().size();
        for(int i = 0; i< size; i++){
            addfriend.SortAndAdd(FriendModel.friendModel.getfriendlist().get(i));
        }
        for (int i = 0; i < addfriend.getTittle().length; i++) {
            //忽略第二个参数，加了图片也不会显示出来，没用的，不用它会出bug
            if (addfriend.getFriendname().get(addfriend.getTittle()[i]).size() != 0) {
                adapter.addSeparatorItem(addfriend.getTittle()[i], R.drawable.friend_icon);
                NameSequencebylistview.add(addfriend.getTittle()[i]);
                for (int k = 0; k <addfriend.getFriendname().get(addfriend.getTittle()[i]).size(); k++) {
                    adapter.addItem(addfriend.getFriendname().get(addfriend.getTittle()[i]).get(k), R.drawable.friend_icon);
                    NameSequencebylistview.add(addfriend.getFriendname().get(addfriend.getTittle()[i]).get(k));
                }
            }
        }
        letter=adapter.letterList;
        lv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        for (int i=0;i<letter.size();i++){
            if(position==letter.get(i)){
                return;
            }
        }
        Intent intent = new Intent(getActivity(), friendinformationActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("dstName",NameSequencebylistview.get(position));
        bundle.putInt("dstId", FriendModel.friendModel.getFriendNameAndID().get(NameSequencebylistview.get(position)));
        intent.putExtras(bundle);
        getActivity().startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        for (int i=0;i<letter.size();i++){
            if(position==letter.get(i)){
                return false;
            }
        }
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

}
