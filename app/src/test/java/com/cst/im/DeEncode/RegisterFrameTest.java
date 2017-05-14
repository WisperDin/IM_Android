package com.cst.im.DeEncode;

import com.cst.im.NetWork.proto.BuildFrame;
import com.cst.im.model.ILoginUser;
import com.cst.im.model.LoginUserModel;

import org.junit.Before;
import org.junit.Test;

import protocol.Protocol;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ASUS on 2017/5/13.
 */

public class RegisterFrameTest {
    @Before
    public void setup()throws Exception{

    }

    //正常情况下的注册编码
    @Test
    public void TestEncodeRegisterFrameSuccess(){
        //初始化帧
        BuildFrame registerFrame = new BuildFrame(BuildFrame.Register);
        //准备帧需要的实体
        ILoginUser registerUser = new LoginUserModel();
        registerUser.setUsername("abc");
        registerUser.setPassword("123");
        //构建帧
        Protocol.Frame frame = registerFrame.GetRegisterFrame(registerUser);
        //打印
        System.out.println(frame);
        //assert
        assertEquals("abc",frame.getSrc().getUserName());
        assertEquals("123",frame.getSrc().getUserPwd());
    }
}
