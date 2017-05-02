package com.cst.im.presenter;

/**
 * Created by PolluxLee on 2017/4/25.
 */

public interface IRegisterPresenter {

    int doRegister(String name, String passwd);

    int judgeUsername(String username);

    boolean judgePassword(String password);

}
