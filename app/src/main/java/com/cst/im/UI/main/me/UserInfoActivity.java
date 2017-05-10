package com.cst.im.UI.main.me;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cst.im.R;
import com.cst.im.presenter.IUserSettingPresenter;
import com.cst.im.presenter.UserSettingPresenterCompl;

import java.util.ArrayList;
import java.util.List;
import com.cst.im.model.UserModel;
/**
 * Created by Acring on 2017/5/7.
 */

public class UserInfoActivity extends AppCompatActivity{//用户信息显示
    public final static int indexUserPicture = 0;   // 用户头像
    public final static int indexUserName = 1;      // 用户名
    public final static int indexUserAge = 2;       //年龄
    public final static int indexUserSex = 3;       // 用户性别
    public final static int indexUserRealName = 4;  // 用户真实姓名
    public final static int indexUserPhone = 5;     // 用户手机号
    public final static int indexUserEmail = 6;     // 用户邮箱
    public final static int indexUserAddress = 7;   // 用户地址
    public final static int indexUserSign = 8;      // 用户签名

    private List<SettingDetails> userInfoList = new ArrayList<>();// 具体显示信息
    private SettingDetailsAdapter adapter;

    IUserSettingPresenter userSettingPresenter = new UserSettingPresenterCompl(this);
    UserModel temperUserModel;//临时用户信息
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        userSettingPresenter.pullRemoteUserInfo();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_detail_parent_layout);
        initUserInfoList();
        adapter = new SettingDetailsAdapter(this,R.layout.setting_detail_layout,userInfoList);
        final ListView listView = (ListView)findViewById(R.id.setting_parent_lv);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SettingDetails userInfo = userInfoList.get(position);
                switch (userInfo.getIndex()){
                    case indexUserPicture:{
                        //TODO 修改头像
                        return;
                    }
                    case indexUserName:{//用户名不可修改
                        return;
                    }
                    case indexUserSex:{
                        final AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
                        builder.setTitle("性别");
                        final String[] sex = {"男","女"};

                        builder.setSingleChoiceItems(sex, 1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SettingDetails userInfo = userInfoList.get(indexUserSex);
                                userInfo.setValue(sex[which]);
                                temperUserModel = UserModel.localUser;
                                temperUserModel.setUserSex(sex[which]);

                                userSettingPresenter.pushRemoteUserInfo(temperUserModel);
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                        return;
                    }
                }
                //其他可修改的属性
                Intent intent = new Intent(UserInfoActivity.this,ChangeDetailsActivity.class);
                intent.putExtra("key",userInfo.getName());
                intent.putExtra("value",userInfo.getValue());
                intent.putExtra("index",userInfo.getIndex());
                startActivityForResult(intent,userInfo.getIndex());
            }
        });
    }


    private void initUserInfoList(){//初始化用户数据
        userInfoList.clear();
        userInfoList.add(new SettingDetails("头像",UserModel.localUser.getUserPicture(),indexUserPicture));
        userInfoList.add(new SettingDetails("用户名",UserModel.localUser.getName(),indexUserName));
        userInfoList.add(new SettingDetails("年龄",String.valueOf(UserModel.localUser.getAge()),indexUserAge));
        userInfoList.add(new SettingDetails("性别",UserModel.localUser.getUserSex(),indexUserSex));
        userInfoList.add(new SettingDetails("真实姓名",UserModel.localUser.getUserRealName(),indexUserRealName));
        userInfoList.add(new SettingDetails("手机号",UserModel.localUser.getUserPhone(),indexUserPhone));
        userInfoList.add(new SettingDetails("邮箱",UserModel.localUser.getUserEmail(),indexUserEmail));
        userInfoList.add(new SettingDetails("地址",UserModel.localUser.getUserAddress(),indexUserAddress));
        userInfoList.add(new SettingDetails("签名",UserModel.localUser.getUserSign(),indexUserSign));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED){
            return;
        }
        switch (requestCode){ //依据返回码获取修改的信息，保存到本地数据库，并更新UI
            // TODO 发送到远程数据库
            case indexUserRealName:{
                String realName = data.getStringExtra("return_real_name");
                UserModel.localUser.setUserRealName(realName);
                UserModel.saveLocalUser();

                SettingDetails settingDetails = userInfoList.get(indexUserRealName);
                settingDetails.setValue(realName);
                adapter.notifyDataSetChanged();
            }break;

            case indexUserPhone:{
                String phone = data.getStringExtra("return_phone");

                UserModel.localUser.setUserPhone(phone);
                UserModel.saveLocalUser();

                SettingDetails settingDetails = userInfoList.get(indexUserPhone);
                settingDetails.setValue(phone);
                adapter.notifyDataSetChanged();
            }break;
            case indexUserEmail:{
                String email = data.getStringExtra("return_email");

                UserModel.localUser.setUserEmail(email);
                UserModel.saveLocalUser();

                SettingDetails settingDetails = userInfoList.get(indexUserEmail);
                settingDetails.setValue(email);
                adapter.notifyDataSetChanged();
            }break;

            case indexUserAddress:{
                String address = data.getStringExtra("return_address");

                UserModel.localUser.setUserAddress(address);
                UserModel.saveLocalUser();

                SettingDetails settingDetails = userInfoList.get(indexUserAddress);
                settingDetails.setValue(address);
                adapter.notifyDataSetChanged();
            }break;

            case indexUserSign:{
                String sign = data.getStringExtra("return_sign");

                UserModel.localUser.setUserSign(sign);
                UserModel.saveLocalUser();

                SettingDetails settingDetails = userInfoList.get(indexUserSign);
                settingDetails.setValue(sign);
                adapter.notifyDataSetChanged();
            }break;

        }


    }

    public void onNetWorkError(){//网络错误
        Toast.makeText(this,"网络错误，服务器连接失败",Toast.LENGTH_LONG).show();
    }

    public void onChangeSuccess(){//修改成功
        UserModel.localUser = temperUserModel;
        UserModel.saveLocalUser();
        Toast.makeText(this,"修改成功",Toast.LENGTH_LONG).show();
        adapter.notifyDataSetChanged();
    }

    public void onChangeFail(){//修改成功
        Toast.makeText(this,"修改失败，请稍后再试",Toast.LENGTH_LONG).show();
    }

    public void onPullSuccess(){//获取数据成功
        Toast.makeText(this,"获取数据成功",Toast.LENGTH_LONG).show();
        initUserInfoList();
        adapter.notifyDataSetChanged();
    }
    public void onPullFail(){//获取数据失败
        Toast.makeText(this,"获取数据失败",Toast.LENGTH_LONG).show();
        adapter.notifyDataSetChanged();
    }
}
