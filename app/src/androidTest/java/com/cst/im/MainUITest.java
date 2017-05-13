package com.cst.im;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cst.im.NetWork.ComService;
import com.cst.im.UI.main.MainActivity;
import com.cst.im.dataBase.DBManager;
import com.cst.im.model.FriendModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
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

        friendlist.add("lzy");
        friendNameAndID.put("lzy",1);
        FriendModel.InitFriendModel(friendlist,friendNameAndID);
        Thread.sleep(1000);
    }
    enum Nav{
        MsgList,
        Friend,
        discover,
        me
    }
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
    //验证点击消息导航栏
    @Test
    public void ClickFriend() throws InterruptedException {
        Thread.sleep(500);
        SwitchBottomNav(Nav.Friend);
        //检查listview是否出现
        onView(withId(R.id.lv_friend)).check(matches(isDisplayed()));
        //点击listview中的某一项
        onData(anything()).inAdapterView(withId(R.id.lv_friend)).atPosition(1).perform(click());
        //点击发信息(好友信息那里)
        onView(withId(R.id.bt_msg)).perform(click());
        //输入文字
        onView(withId(R.id.send_edt)).perform(typeText("UItestMsg"),closeSoftKeyboard());
        //点击发信息
        onView(withId(R.id.btn_send)).perform(click());
        Thread.sleep(1000);
    }

    //验证点击个人消息
    @Test
    public void ClickMe() throws InterruptedException {
        //切换至个人信息页面
        SwitchBottomNav(Nav.me);
        //检查listview是否出现
        onView(withId(R.id.lv_setting)).check(matches(isDisplayed()));
        //点击listview中的某一项
        onData(anything()).inAdapterView(withId(R.id.lv_setting)).atPosition(0).perform(click());

        //点击listview中的某一项
        onData(anything()).inAdapterView(withId(R.id.setting_parent_lv)).atPosition(0).perform(click());

        Thread.sleep(5000);
    }




}
