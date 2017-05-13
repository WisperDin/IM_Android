package com.cst.im.UI.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.cst.im.R;
import com.cst.im.UI.SplashActivity;
import com.cst.im.UI.main.discovery.DiscoveryFragment;
import com.cst.im.UI.main.friend.FriendViewFragment;
import com.cst.im.UI.main.me.SettingFragment;
import com.cst.im.UI.main.msg.MsgFragment;
import com.cst.im.dataBase.DBManager;
import com.cst.im.model.FriendModel;
import com.cst.im.model.ILoginUser;
import com.cst.im.model.UserModel;
import com.cst.im.presenter.IFriendPresenter;
import com.cst.im.presenter.FriendPresenterCompl;
import com.cst.im.presenter.ILoginPresenter;
import com.cst.im.presenter.LoginPresenterCompl;
import com.cst.im.presenter.Status;
import com.cst.im.view.IFriendView;
import com.cst.im.view.ILoginView;

import java.util.ArrayList;
import java.util.HashMap;



public class MainActivity extends AppCompatActivity implements IFriendView,ILoginView {
    private IFriendPresenter myfriend=new FriendPresenterCompl(this);
    ILoginPresenter loginPresenter;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //导航栏切换的Fragment切换 相当于Mux
            switch (item.getItemId()) {
                case R.id.navigation_chat:
                    transaction.replace(R.id.content, new MsgFragment()).commit();
                    return true;
                case R.id.navigation_contact:
                    transaction.replace(R.id.content, new FriendViewFragment()).commit();
                    return true;
                case R.id.navigation_discovery:
                    transaction.replace(R.id.content, new DiscoveryFragment()).commit();
                    return true;
                case R.id.navigation_me:
                    transaction.replace(R.id.content, new SettingFragment()).commit();
                    return true;
            }
            return false;
        }

    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBManager.getIntance(this);
        ILoginUser loginUser = DBManager.queryLoginUser();
        UserModel.InitLocalUser(loginUser.getId());
        loginPresenter=new LoginPresenterCompl(this);
        loginPresenter.doLogin(loginUser.getUsername(),loginUser.getPassword());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myfriend.Getfriendlist(loginUser.getId());//登陆成功从服务器数据库获取所有好友的名字
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setCurrentFragment();


    }

    private void setCurrentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MsgFragment msgFragment = new MsgFragment();
        transaction.replace(R.id.content, msgFragment).commit();
    }

    @Override
    public void onRecvMsg(ArrayList<String> list,HashMap<String ,Integer> NameAndID){
        FriendModel.InitFriendModel(list,NameAndID);
        System.out.println("运行");
    }
    @Override
    public void onReaultCode(int code,String name){}


    @Override
    public void onLoginResult(int rslCode, int id) {
        if(rslCode == Status.Login.LOGINFAILED){
            Toast.makeText(this,"自动登录失败",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public void onEditTip() {

    }
    @Override
    public void onReaultCodebyAddFriend(int code,int id,String name){

    }
    @Override
    protected void onNewIntent(Intent intent) {//退出登录
        super.onNewIntent(intent);
        Intent intent1 = new Intent(this, SplashActivity.class);
        startActivity(intent1);
        this.finish();
    }
}
