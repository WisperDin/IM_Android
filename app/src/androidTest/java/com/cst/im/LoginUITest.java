package com.cst.im;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cst.im.UI.LoginActivity;
import com.cst.im.dataBase.Constant;

import org.junit.Before;
import org.junit.Ignore;
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
import static org.junit.Assert.assertEquals;
/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoginUITest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
           LoginActivity.class);

    @Before
    public void Tear(){


    }

    //验证点击登录成功事件

    @Test
    public void loginWithCorrectPassword()throws InterruptedException {

        onView(withId(R.id.username)).perform(typeText("lzy"),closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("123"),closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        //Thread.sleep(5000);
        onView(withText("登录成功")).inRoot(new ToastMatcher())
           .check(matches(isDisplayed()));

        //Thread.sleep(5000);
    }


    //验证点击登录失败事件
    @Ignore
    @Test
    public void loginWithWrongPassword()throws InterruptedException {

        onView(withId(R.id.username)).perform(typeText("mvp"),closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("abc"),closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        //Thread.sleep(5000);
        onView(withText("用户名或密码不正确")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
        Thread.sleep(5000);
    }
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.cst.im", appContext.getPackageName());
    }


}
