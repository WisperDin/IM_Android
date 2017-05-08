package com.cst.im.ComServe;

import com.cst.im.NetWork.ComService;

import org.junit.After;
import org.junit.Before;

//import com.cst.im.model.IMsg;
//import com.cst.im.model.MsgModel;

/**
 * Created by ASUS on 2017/5/2.
 */

public class ConserveTest {

    //static Protocol.Frame frame;
    static ComService comService;

    @Before
    public void setUP()throws Exception{
        comService=new ComService();
    }

    @After
    public void tear()throws Exception{
        //frame=null;
    }
    /*//成功样例
    @Test
    public void OnMessageComeSuccessTest(){
        //初始化帧
        BuildFrame chatFrame = new BuildFrame(BuildFrame.ChatMsg);
        //准备帧需要的实体
        IMsg entity = new MsgModel();
        entity.setRight_name("lzy");//发送源
        entity.setDate(Tools.getDate());//日期
        entity.setMessage("testMsg");//发送信息
        entity.setMsgType(true);//消息类别

        entity.setLeft_name("abc");//目的
        //构建帧
        Protocol.Frame frame = chatFrame.GetChatMsgFrame(entity);

        //调用消息分发函数
        comService.OnMessageCome(frame);
    }
    //失败样例（查看日志提示bad value）
    @Test
    public void OnMessageComeNullPointerTest(){
        //初始化帧
        BuildFrame chatFrame = new BuildFrame(BuildFrame.ChatMsg);
        Protocol.Frame.Builder frameOrigin = chatFrame.GetOriginFrameBuilder();
        //准备帧需要的实体
        IMsg entity = new MsgModel();
        //entity.setRight_name("lzy");//发送源
        entity.setDate(Tools.getDate());//日期
        entity.setMessage("testMsg");//发送信息
        entity.setMsgType(true);//消息类别

        entity.setLeft_name("abc");//目的
        //构建帧

        //（不设置发送源）
        //发送源
        //Protocol.User.Builder src = Protocol.User.newBuilder();
        //src.setUserName(entity.getRight_name());
        //接收者
        Protocol.User.Builder dst = Protocol.User.newBuilder();
        dst.setUserName(entity.getLeft_name());

        Protocol.DstUser.Builder dstGroup = Protocol.DstUser.newBuilder();
        dstGroup.addDst(dst);
        //要发送的信息
        Protocol.Msg.Builder msg = Protocol.Msg.newBuilder();
        msg.setMsg(entity.getMessage());
        //frameOrigin.setSrc(src.build());
        frameOrigin.setDst(dstGroup.build());
        frameOrigin.setMsg(msg);


        //调用消息分发函数
        comService.OnMessageCome(frameOrigin.build());

    }*/
}
