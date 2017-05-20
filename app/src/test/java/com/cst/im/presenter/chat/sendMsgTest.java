package com.cst.im.presenter.chat;

//import org.mockito.Mock;

import com.cst.im.NetWork.ComService;
import com.cst.im.model.UserModel;
import com.cst.im.presenter.ChatPresenter;
import com.cst.im.presenter.LoginPresenterCompl;
import com.cst.im.presenter.Status;
import com.cst.im.view.IChatView;
import com.cst.im.view.ILoginView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * Created by ASUS on 2017/4/23.
 */
@RunWith(MockitoJUnitRunner.class)
public class sendMsgTest {
    //登录业务逻辑
    private LoginPresenterCompl loginPresenter;
    @Mock
    private ILoginView iLoginView;

    //聊天业务逻辑
    private ChatPresenter chatPresenter;
    @Mock
    private IChatView iChatView;
    @InjectMocks
    private ComService comService;


    @Before
    public void setup()throws Exception{
        //mock出View对象
        iLoginView = Mockito.mock(ILoginView.class);
        iChatView = Mockito.mock(IChatView.class);
        //初始化登录Presenter
        loginPresenter=new LoginPresenterCompl(iLoginView);
        //启动网络连接
        comService.onCreate();
        //登录先
        loginPresenter.doLogin("lzy","123");
        Thread.sleep(1000);
        verify(iLoginView).onLoginResult(Status.Login.LOGINSUCCESS,1);
        //初始化localUser
        UserModel.localUser = new UserModel("lzy","",1);
        //初始化聊天Presenter
        chatPresenter = new ChatPresenter(iChatView,new UserModel[]{new UserModel("wzb","",2)});
    }
    @Test
    public void login_Success() throws Exception {
        chatPresenter.SendMsg("hello from unittest");

    }


}
