package com.cst.im.presenter;

import com.cst.im.view.ILoginView;

import org.junit.Before;
import org.mockito.Mock;

/**
 * Created by ASUS on 2017/4/23.
 */

public class LoginTest {
    //登录业务逻辑
    private LoginPresenterCompl loginPresenter;
    @Mock
    private ILoginView iLoginView;

    @Before
    public void setup()throws Exception{
        //init login presenter
       loginPresenter=new LoginPresenterCompl(iLoginView);
    }
    /*
    @Test
    public void login_Failed() throws Exception {
        int errCode = loginPresenter.doLogin("123","456");
        assertEquals(errCode, -1);
    }


    @Test
    public void login_Success() throws Exception {
        int errCode = loginPresenter.doLogin("mvp","mvp");
        assertEquals(errCode, 0);
    }
    */
}
