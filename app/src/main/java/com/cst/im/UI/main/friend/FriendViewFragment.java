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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.cst.im.R;
import com.cst.im.model.FriendModel;

import java.util.ArrayList;

import static com.cst.im.model.UserModel.localUser;


public class FriendViewFragment extends Fragment implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, View.OnClickListener {
    MyCustomAdapter adapter=null;
    private  ArrayList<Integer> letter = new ArrayList<Integer>();//储存标题上的大写字母的位置
     ArrayList<String> NameSequencebylistview;//记录item上的名字
    ListView Friendlistview;
    ImageView img_search;

    Button To_friend_group;    //用于跳转到分组界面
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        Friendlistview = (ListView)  view.findViewById(R.id.lv_friend);
        img_search=(ImageView)view.findViewById(R.id.img_search);
        InitView(img_search,Friendlistview);
        Friendlistview.setOnItemClickListener(this);    //设置单击监听，接口实现
        Friendlistview.setOnItemLongClickListener(this);  //设置长按监听，接口实现

        //byjijinping 5.18
        To_friend_group=(Button)view.findViewById(R.id.button_friend_group);
        To_friend_group.setOnClickListener(this);

        img_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (localUser != null) {
                    Intent intent = new Intent(getActivity(), SearchFriend.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("srcId", localUser.getId());
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }
            }
        });
        return view;
    }


    public void InitView(ImageView img,ListView lv){
        NameSequencebylistview= new ArrayList<String>();
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
        int len=addfriend.getTittle().length;
        for (int i = 0; i < len; i++) {
            //查看某个字母下的用户
            if (addfriend.getFriendname().get(addfriend.getTittle()[i]).size() != 0) {
                adapter.addSeparatorItem(addfriend.getTittle()[i], R.drawable.friend_icon);
                NameSequencebylistview.add(addfriend.getTittle()[i]);
                int length=addfriend.getFriendname().get(addfriend.getTittle()[i]).size();
                for (int k = 0; k <length; k++) {
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
        InitView(img_search,Friendlistview);
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

    @Override
    public void onClick(View view) {
        if(view==To_friend_group)
        {
            Toast.makeText(getActivity(),"跳转到分组界面",Toast.LENGTH_SHORT).show();
            Intent ntent=new Intent(getActivity(),friendgroupActivity.class);
            startActivity(ntent);
        }
    }
}
