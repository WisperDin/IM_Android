package com.cst.im;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cst.im.NetWork.ComService;
import com.cst.im.UI.RegisterActivity;
import com.cst.im.dataBase.DBManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RegisterUITest {



    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule<>(
            RegisterActivity.class);

    @Before
    public void setUp(){
        //初始化网络服务
        Intent startIntent = new Intent(mActivityRule.getActivity(), ComService.class);
        mActivityRule.getActivity().startService(startIntent);//记得最后结束
        //初始化数据库
        DBManager.getIntance(mActivityRule.getActivity());
    }

    //验证点击注册成功事件

    @Test
    public void Register()throws InterruptedException {

        onView(withId(R.id.register_username)).perform(typeText("UITEST"),closeSoftKeyboard());
        onView(withId(R.id.register_password)).perform(typeText("123"),closeSoftKeyboard());
        onView(withId(R.id.register_button)).perform(click());

        //第二次启动，，账号已经注册了
        onView(withText("注册成功")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));


        //Thread.sleep(5000);
    }




}
