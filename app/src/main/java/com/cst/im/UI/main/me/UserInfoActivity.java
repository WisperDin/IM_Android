package com.cst.im.UI.main.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cst.im.R;
import com.cst.im.presenter.IUserSettingPresenter;
import com.cst.im.presenter.UserSettingPresenter;

import java.util.ArrayList;
import java.util.List;
import com.cst.im.model.UserModel;
/**
 * Created by Acring on 2017/5/7.
 */

public class UserInfoActivity extends AppCompatActivity{//用户信息显示
    private final int indexUserPicture = 1;   // 第一个为用户头像
    private final int indexUserName = 2;      // 第二个为用户名
    private final int indexUserSex = 3;       // 第三个为用户性别
    private final int indexUserRealName = 4;  // 第四个为用户真实姓名
    private final int indexUserPhone = 5;     // 第五个为用户手机号
    private final int indexUserEmail = 6;     // 第六个为用户邮箱
    private final int indexUserAddress = 7;   // 第七个为用户地址
    private final int indexUserSign = 8;      // 第八个为用户签名

    private List<SettingDetails> userInfoList = new ArrayList<>();// 具体显示信息


    IUserSettingPresenter userSettingPresenter = new UserSettingPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_detail_parent_layout);
        initUserInfoList();
        final SettingDetailsAdapter adapter = new SettingDetailsAdapter(this,
                R.layout.setting_detail_layout,userInfoList);
        final ListView listView = (ListView)findViewById(R.id.setting_parent_lv);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    private void initUserInfoList(){//初始化用户数据
        userInfoList.add(new SettingDetails("头像",UserModel.localUser.getUserPicture(),indexUserPicture));
        userInfoList.add(new SettingDetails("用户名",UserModel.localUser.getName(),indexUserName));
        userInfoList.add(new SettingDetails("性别",UserModel.localUser.getUserSex(),indexUserSex));
        userInfoList.add(new SettingDetails("真实姓名",UserModel.localUser.getUserRealName(),indexUserRealName));
        userInfoList.add(new SettingDetails("手机号",UserModel.localUser.getUserPhone(),indexUserPhone));
        userInfoList.add(new SettingDetails("邮箱",UserModel.localUser.getUserEmail(),indexUserEmail));
        userInfoList.add(new SettingDetails("地址",UserModel.localUser.getUserAddress(),indexUserAddress));
        userInfoList.add(new SettingDetails("签名",UserModel.localUser.getUserSign(),indexUserSign));

    }
}
