package com.cst.im.UI.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.cst.im.R;
import com.cst.im.UI.main.discovery.DiscoveryFragment;
import com.cst.im.UI.main.friend.FriendViewFragment;
import com.cst.im.UI.main.me.SettingFragment;
import com.cst.im.UI.main.msg.MsgFragment;
import com.cst.im.dataBase.DBManager;
import com.cst.im.model.ILoginUser;
import com.cst.im.model.UserModel;
import com.cst.im.presenter.IFriendPresenter;
import com.cst.im.presenter.IFriendPresenterCompl;
import com.cst.im.view.IFriendView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IFriendView{
    public static ArrayList<String> friendlist=new ArrayList<String>();//储存好友列表的名字（服务器获取）
    private IFriendPresenter myfriend=new IFriendPresenterCompl(this);//用于获取好友列表名字
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
        ILoginUser loginUser = DBManager.queryLoginUser();
        UserModel.InitLocalUser(loginUser.getUsername(),loginUser.getPassword(),loginUser.getId());


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myfriend.Getfriendlist("lzy");//登陆成功从服务器数据库获取所有好友的名字
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
    public void onRecvMsg(ArrayList<String> list){
        this.friendlist=list;
        System.out.println("运行");
    }

}
