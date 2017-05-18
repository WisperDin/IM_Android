package com.cst.im;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cst.im.NetWork.ComService;
import com.cst.im.UI.main.MainActivity;
import com.cst.im.dataBase.DBManager;
import com.cst.im.model.FriendModel;
import com.cst.im.model.UserModel;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainUITest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp() throws InterruptedException {
        //初始化网络服务
        Intent startIntent = new Intent(mActivityRule.getActivity(), ComService.class);
        mActivityRule.getActivity().startService(startIntent);//记得最后结束
        //初始化数据库
        DBManager.getIntance(mActivityRule.getActivity());
        //假的好友列表
        ArrayList<String> friendlist = new ArrayList<String>();
        HashMap<String ,Integer> friendNameAndID = new HashMap<String , Integer>();
        friendlist.add("wzb");
        friendNameAndID.put("wzb",2);
        FriendModel.InitFriendModel(friendlist,friendNameAndID);

        if(UserModel.localUser==null){
            UserModel.localUser = new UserModel("lzy","123",1);
        }
        //Thread.sleep(1000);

    }
    enum Nav{
        MsgList,
        Friend,
        discover,
        me
    }
    //基础操作:--------------------------------------------------------------
    //切换页面
    private void SwitchBottomNav(final Nav nav){
        //到UI线程运行
        mActivityRule.getActivity().runOnUiThread(
                new Runnable()
                {
                    public void run()
                    {
                        BottomNavigationView navigation = (BottomNavigationView) mActivityRule.getActivity().findViewById(R.id.navigation);
                        switch (nav){
                            case MsgList:
                                navigation.setSelectedItemId(R.id.navigation_chat);
                                break;
                            case Friend:
                                navigation.setSelectedItemId(R.id.navigation_contact);
                                break;
                            case discover:
                                navigation.setSelectedItemId(R.id.navigation_discovery);
                                break;
                            case me:
                               navigation.setSelectedItemId(R.id.navigation_me);
                                break;
                        }

                    }
                });
    }
    //点击一个好友
    private void ClickFriend()throws InterruptedException{
        Thread.sleep(500);
        SwitchBottomNav(Nav.Friend);
        //检查listview是否出现
        onView(withId(R.id.lv_friend)).check(matches(isDisplayed()));
        //点击listview中的某一项
        onData(anything()).inAdapterView(withId(R.id.lv_friend)).atPosition(1).perform(click());
        //点击发信息(好友信息那里)
        onView(withId(R.id.bt_msg)).perform(click());
    }

    //和好友聊天
    private void ChatWithFriend()throws InterruptedException{
        //输入文字
        onView(withId(R.id.send_edt)).perform(typeText("UItestMsg"),closeSoftKeyboard());
        //点击发信息
        onView(withId(R.id.btn_send)).perform(click());
    }

    //发送文件给好友
    //权限阻止了这个操作
    private void SendFileToFriend()throws InterruptedException{
        //点击加号
        onView(withId(R.id.plus_iv)).perform(click());
        //点击文件图标
        onView(withId(R.id.chat_picture)).perform(click());
    }
    void SaveAge(int age,int i){
        //点击listview中的某一项
        onData(anything()).inAdapterView(withId(R.id.setting_parent_lv)).atPosition(i).perform(click());
        //清空
        onView(withId(R.id.detail_et)).perform(replaceText(""),closeSoftKeyboard());
        //更改属性
        onView(withId(R.id.detail_et)).perform(typeText(Integer.toString(age)),closeSoftKeyboard());
        //点击保存
        onView(withId(R.id.save_bt)).perform(click());
    }
    void SaveSex(boolean sex,int i){
        //点击listview中的某一项
        onData(anything()).inAdapterView(withId(R.id.setting_parent_lv)).atPosition(i).perform(click());
        //更改属性
        onView(withText("女"))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()))
                .perform(click());

    }
    void SaveRealName(String realName,int i){
        //点击listview中的某一项
        onData(anything()).inAdapterView(withId(R.id.setting_parent_lv)).atPosition(i).perform(click());
        //清空
        onView(withId(R.id.detail_et)).perform(replaceText(""),closeSoftKeyboard());
        //更改属性
        onView(withId(R.id.detail_et)).perform(typeText(realName),closeSoftKeyboard());
        //点击保存
        onView(withId(R.id.save_bt)).perform(click());
    }
    void SavePhonoNO(String phonoNo,int i){
        //点击listview中的某一项
        onData(anything()).inAdapterView(withId(R.id.setting_parent_lv)).atPosition(i).perform(click());
        //清空
        onView(withId(R.id.detail_et)).perform(replaceText(""),closeSoftKeyboard());
        //更改属性
        onView(withId(R.id.detail_et)).perform(typeText(phonoNo),closeSoftKeyboard());
        //点击保存
        onView(withId(R.id.save_bt)).perform(click());
    }
    void SaveEmail(String email,int i){
        //点击listview中的某一项
        onData(anything()).inAdapterView(withId(R.id.setting_parent_lv)).atPosition(i).perform(click());
        //清空
        onView(withId(R.id.detail_et)).perform(replaceText(""),closeSoftKeyboard());
        //更改属性
        onView(withId(R.id.detail_et)).perform(typeText(email),closeSoftKeyboard());
        //点击保存
        onView(withId(R.id.save_bt)).perform(click());
    }
    void SaveAddress(String address,int i){
        //点击listview中的某一项
        onData(anything()).inAdapterView(withId(R.id.setting_parent_lv)).atPosition(i).perform(click());
        //清空
        onView(withId(R.id.detail_et)).perform(replaceText(""),closeSoftKeyboard());
        //更改属性
        onView(withId(R.id.detail_et)).perform(typeText(address),closeSoftKeyboard());
        //点击保存
        onView(withId(R.id.save_bt)).perform(click());
    }
    void SaveSign(String sign,int i){
//点击listview中的某一项
        onData(anything()).inAdapterView(withId(R.id.setting_parent_lv)).atPosition(i).perform(click());
        //清空
        onView(withId(R.id.detail_et)).perform(replaceText(""),closeSoftKeyboard());
        //更改属性
        onView(withId(R.id.detail_et)).perform(typeText(sign),closeSoftKeyboard());
        //点击保存
        onView(withId(R.id.save_bt)).perform(click());

    }

    //点击一个好友，向那个好友发送消息
    @Test
    public void ChatWithFriendFromFriendList() throws InterruptedException {
        //点击好友
        ClickFriend();
        //聊天
        ChatWithFriend();
        Thread.sleep(1000);
    }
    //点击从点击好友，发信息生成的消息列表
    @Test
    public void ClickMsgListFromClickFriend() throws InterruptedException {
        ClickFriend();
        //两次返回键，从聊天返回到主界面
        onView(isRoot()).perform(ViewActions.pressBack());
        onView(isRoot()).perform(ViewActions.pressBack());
        //切换到消息列表
        SwitchBottomNav(Nav.MsgList);
        //点击消息列表
        //点击listview中的某一项
        onData(anything()).inAdapterView(withId(R.id.chat_list)).atPosition(0).perform(click());
        //聊天
        ChatWithFriend();
        Thread.sleep(1000);
    }
    //点击一个好友，向那个好友发送文件
    @Ignore
    @Test
    public void SendFileToFriendFromFriendList() throws InterruptedException {
        //点击好友
        ClickFriend();
        //发送文件
        SendFileToFriend();
        Thread.sleep(5000);
    }
    //添加好友
    @Ignore
    @Test
    public void SearchFriend()throws InterruptedException {
        SwitchBottomNav(Nav.Friend);
        //点击搜索好友框
        onView(withId(R.id.img_search)).perform(click());
        for(int i=0;i<=10;i++){
            //输入好友id
            onView(withId(R.id.et_search)).perform(replaceText(Integer.toString(i)),closeSoftKeyboard());
            //点击搜索
            onView(withId(R.id.bt_search)).perform(click());
        }
        Thread.sleep(1000);

    }
    //验证点击个人消息输入信息保存
    @Test
    public void ClickMe() throws InterruptedException {
        //切换至个人信息页面
        SwitchBottomNav(Nav.me);
        //检查listview是否出现
        onView(withId(R.id.lv_setting)).check(matches(isDisplayed()));
        //点击listview中的某一项，进入个人信息设置ListView
        onData(anything()).inAdapterView(withId(R.id.lv_setting)).atPosition(0).perform(click());
        int i=2;
        SaveAge(22,i++);
        SaveSex(false,i++);
        SaveRealName("lzy",i++);
        SavePhonoNO("13538842294",i++);
        SaveEmail("734800224@qq.com",i++);
        SaveAddress("GZHU",i++);
        SaveSign("GOOD",i++);
        Thread.sleep(2000);
    }
    //验证点击退出登录
    @Test
    public void ClickLogOut() throws InterruptedException {
        //切换至个人信息页面
        SwitchBottomNav(Nav.me);
        //检查listview是否出现
        onView(withId(R.id.lv_setting)).check(matches(isDisplayed()));
        //点击listview中的某一项，进入个人信息设置ListView
        onData(anything()).inAdapterView(withId(R.id.lv_setting)).atPosition(1).perform(click());
        //点击listview中的退出登录项
        onData(anything()).inAdapterView(withId(R.id.setting_parent_lv)).atPosition(2).perform(click());

    }
    




}
