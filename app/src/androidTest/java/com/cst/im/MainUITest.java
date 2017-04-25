package com.cst.im;

import android.support.design.widget.BottomNavigationView;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cst.im.UI.main.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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

    //验证点击消息导航栏
    @Test
    public void ClickMsg() throws InterruptedException {
        //到UI线程运行
        mActivityRule.getActivity().runOnUiThread(
                new Runnable()
                {
                    public void run()
                    {
                        BottomNavigationView navigation = (BottomNavigationView) mActivityRule.getActivity().findViewById(R.id.navigation);
                        navigation.setSelectedItemId(R.id.navigation_me);
                    }
                });
        Thread.sleep(5000);
    }

}
