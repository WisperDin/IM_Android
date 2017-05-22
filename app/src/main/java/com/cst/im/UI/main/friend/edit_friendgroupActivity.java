package com.cst.im.UI.main.friend;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.cst.im.R;
import com.cst.im.model.GroupItemModel;

import java.util.ArrayList;

/**
 * Created by jijinping on 2017/5/21.
 */

public class edit_friendgroupActivity extends Activity implements View.OnClickListener{
    private GridView group_gv;
    private ArrayList<GroupItemModel> group;    //某个组
    private static edit_friendgroupAdapter groupAdapter;

    private Button bt_back;
    private TextView title;
    private Button bt_save;
    private TextView group_name_text;
    private TextView group_title;
    private EditText group_name_edit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_friend_group);
        bt_back=(Button)findViewById(R.id.friend_group_back);
        bt_save=(Button)findViewById(R.id.friend_group_save);
        title=(TextView)findViewById(R.id.friend_group_title);
        group_name_edit=(EditText)findViewById(R.id.edit_group_name);
        InitData();
        //注册按钮监听
        bt_back.setOnClickListener(this);
        bt_save.setOnClickListener(this);
    }
    public void InitData()
    {
        //初始化数据
        group=new ArrayList<GroupItemModel>();
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("杨逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("AAaa...",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("杨逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("AAaa...",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("杨逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("AAaa...",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));
        group.add(new GroupItemModel("李逍遥",R.drawable.friend_icon));

        //设置适配器
        groupAdapter=new edit_friendgroupAdapter(this,group);
        group_gv=(GridView)findViewById(R.id.grid_group);
        group_gv.setAdapter(groupAdapter);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(bt_back))    //返回
        {
            finish();
        }else if(view.equals(bt_save))    //保存当期组的信息
        {

        }
    }
}
