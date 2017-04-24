package com.cst.im;

import android.os.IBinder;
import android.support.test.espresso.Root;
import android.view.WindowManager;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

//用于自动验证toast的一个匹配器
/**
 * Created by ASUS on 2017/4/24.
 */public class ToastMatcher extends TypeSafeMatcher<Root> {
    @Override public void describeTo(Description description) {
        description.appendText("is toast");
    }

    @Override public boolean matchesSafely(Root root) {
        int type = root.getWindowLayoutParams().get().type;
        if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
            IBinder windowToken = root.getDecorView().getWindowToken();
            IBinder appToken = root.getDecorView().getApplicationWindowToken();
            if (windowToken == appToken) {
                //means this window isn't contained by any other windows.
                    return true;
            }
        }
        return false;
    }
}