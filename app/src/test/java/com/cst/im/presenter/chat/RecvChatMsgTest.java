package com.cst.im.presenter.chat;

import com.cst.im.model.IBaseMsg;
import com.cst.im.model.UserModel;
import com.cst.im.presenter.ChatPresenter;
import com.cst.im.view.IChatView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ASUS on 2017/5/16.
 */

public class RecvChatMsgTest {

    //登录业务逻辑
    private static ChatPresenter chatPresenter;
    @Mock
    private IChatView iChatViewView;

    @Before
    public void setup()throws Exception{
        //init login presenter
        List<IBaseMsg> msg = new ArrayList<IBaseMsg>();
        UserModel a = new UserModel();
        a.setId(1);
        UserModel b = new UserModel();
        b.setId(3);
        UserModel c = new UserModel();
        c.setId(2);
        UserModel[] dst = new UserModel[]{a,b,c};

        chatPresenter=new ChatPresenter(iChatViewView,msg,dst);
    }
    //    @Test
//    public void login_Failed() throws Exception {
//        int errCode = loginPresenter.doLogin("123","456");
//        assertEquals(errCode, -1);
//    }
//
//
   @Test
    public void CheckSrcId() throws Exception {
        assertEquals(true,chatPresenter.CheckSrcID(1));
    }
    @Test
    public void CheckDstId() throws Exception {
        UserModel.localUser = new UserModel();
        UserModel.localUser.setId(1);
        assertEquals(true,chatPresenter.CheckDstID(new int[]{1,2,3}));
    }
}
