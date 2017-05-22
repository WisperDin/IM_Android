package com.cst.im.UI.main.friend;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cst.im.R;
import com.cst.im.model.FriendgroupModel;

import java.util.ArrayList;

/**
 * Created by jijinping on 2017/5/17.
 */

public class friendgroupActivity extends Activity implements
        AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener,View.OnClickListener{
    private ListView group_lv;
    private static ArrayList<FriendgroupModel> mGroupList;
    private static friendgroupAdapter mGroupAdapter=null;
    private Button bt_back;
    private Button bt_new;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_group_list);
        bt_back=(Button)findViewById(R.id.friend_group_back);
        bt_new=(Button)findViewById(R.id.friend_group_new);
        title=(TextView)findViewById(R.id.friend_group_title);

        //注册按钮监听
        bt_back.setOnClickListener(this);
        bt_new.setOnClickListener(this);
        InitData();
    }
    public void InitData()
    {
        mGroupList=new ArrayList<FriendgroupModel>();
        mGroupList.add(new FriendgroupModel("家人",5));
        mGroupList.add(new FriendgroupModel("朋友",5));
        mGroupList.add(new FriendgroupModel("同学",15));
        mGroupList.add(new FriendgroupModel("基友",5));
        mGroupList.add(new FriendgroupModel("长辈",5));
        group_lv=(ListView)findViewById(R.id.friend_group);
        mGroupAdapter=new friendgroupAdapter(mGroupList,this);
        group_lv.setAdapter(mGroupAdapter);
        group_lv.setOnItemClickListener(this);
        group_lv.setOnItemLongClickListener(this);

    }
    public void onRefreshGroup()
    {
        mGroupAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(this,edit_friendgroupActivity.class);
        FriendgroupModel item=mGroupList.get(i);
        //传送必要的数据给edit_friendgroupActivity
        Bundle bundle=new Bundle();
        bundle.putString("GroupName",item.getName());
        bundle.putInt("GroupSize",item.getNumber());
        intent.putExtras(intent);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
        //得到被长按的对象
        final FriendgroupModel group_item=mGroupList.get(position);
        //设置对话框显示的列表名
        final String[] choices=new String[]{"删除","编辑"};
        ////实例化一个对话框，进行操作
        new AlertDialog.Builder(this).setItems(choices,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(choices[i].equals("删除"))
                {
                    mGroupList.remove(position);
                    onRefreshGroup();
                }else if(choices[i].equals("编辑")){
                    Intent intent=new Intent(friendgroupActivity.this,edit_friendgroupActivity.class);
                    FriendgroupModel item=mGroupList.get(position);
                    //传送必要的数据给edit_friendgroupActivity
                    Bundle bundle=new Bundle();
                    bundle.putString("GroupName",item.getName());
                    bundle.putInt("GroupSize",item.getNumber());
                    intent.putExtras(intent);
                    startActivity(intent);
                }
            }
        }).create().show();
        return true;
    }


    @Override
    public void onClick(View view) {
        if (view.equals(bt_back)) {
            finish();
        } else if (view.equals(bt_new))
        {

        }
    }
}
