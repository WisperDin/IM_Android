package com.cst.im.UI.main.friend;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.cst.im.R;

import java.util.ArrayList;

/**
 * Created by jijinping on 2017/5/17.
 */

public class friendgroupActivity extends AppCompatActivity {
    private ListView group_lv;
    private ArrayList<friendgroupItem> mGroupList;
    private static friendgroupAdapter mGroupAdapter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_group_list);
        InitData();
    }
    public void InitData()
    {
        mGroupList=new ArrayList<friendgroupItem>();
        mGroupList.add(new friendgroupItem("家人",5));
        mGroupList.add(new friendgroupItem("朋友",5));
        mGroupList.add(new friendgroupItem("同学",15));
        mGroupList.add(new friendgroupItem("基友",5));
        mGroupList.add(new friendgroupItem("长辈",5));
        group_lv=(ListView)findViewById(R.id.friend_group);
        mGroupAdapter=new friendgroupAdapter(mGroupList,this);
        group_lv.setAdapter(mGroupAdapter);
    }
    public void onRefreshGroup()
    {
        mGroupAdapter.notifyDataSetChanged();
    }
}
