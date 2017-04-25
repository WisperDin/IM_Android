package com.cst.im.view;

import android.widget.EditText;

/**
 * Created by ASUS on 2017/4/23.
 */
public interface ILoginView {
    public void onLoginResult(Boolean result, int code);
    void onNetworkError();
    void onEditTip();
}