package com.cst.im.view;

/**
 * Created by ASUS on 2017/4/23.
 */
public interface ILoginView {
    //参数为状态码与状态信息
    public void onLoginResult(final int rslCode,final int id);
    void onNetworkError();
    void onEditTip();
}