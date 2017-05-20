package com.cst.im.presenter;

//import org.mockito.Mock;

import com.cst.im.NetWork.ComService;
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
public class LoginTest {
   //登录业务逻辑
    private LoginPresenterCompl loginPresenter;
    @Mock
    private ILoginView iLoginView;
    @InjectMocks
    private ComService comService;


    @Before
    public void setup()throws Exception{
        //init login presenter
        //MockitoAnnotations.initMocks(this);
        //mock出View对象
        iLoginView = Mockito.mock(ILoginView.class);
        loginPresenter=new LoginPresenterCompl(iLoginView);
        //启动网络连接
        comService.onCreate();
        //初始化数据库
        //DBManager.getIntance(mockContext);
    }
    @Test
    public void login_Success() throws Exception {
        loginPresenter.doLogin("lzy","123");
        Thread.sleep(1000);
        verify(iLoginView).onLoginResult(Status.Login.LOGINSUCCESS,1);
    }


}
