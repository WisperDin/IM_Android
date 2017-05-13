package com.cst.im.DeEncode;

import com.cst.im.NetWork.proto.BuildFrame;
import com.cst.im.model.IBaseMsg;
import com.cst.im.model.ITextMsg;
import com.cst.im.model.TextMsgModel;
import com.cst.im.presenter.Tools;

import org.junit.Before;
import org.junit.Test;

import protocol.Protocol;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ASUS on 2017/5/1.
 */

public class TextMsgFrameTest {


    @Before
    public void setup()throws Exception{

    }
    @Test
    public void encodeChatMsgFrameSuccessTest()throws Exception{
        //初始化帧
        BuildFrame chatFrame = new BuildFrame(BuildFrame.TextMsg);
        //准备帧需要的实体
        ITextMsg txtMsg = new TextMsgModel();
        txtMsg.setSrc_ID(1);
        txtMsg.setMsgDate(Tools.getDate());
        txtMsg.setText("testMsg");
        txtMsg.sendOrRecv(false);
        txtMsg.setDst_ID(new int[]{1});
        txtMsg.setMsgType(IBaseMsg.MsgType.TEXT);
        //构建帧
        Protocol.Frame frame = chatFrame.GetChatMsgFrame(txtMsg);
        //打印
        System.out.println(frame);

        //assert
        assertEquals(1,frame.getSrc().getUserID());
        assertEquals(1,frame.getDst().getDst(0).getUserID());
        assertEquals("testMsg",frame.getMsg().getMsg());
    }
/*
    //错误样例
    @Test
    public void encodeChatMsgFrameFailedTest()throws Exception{
        //初始化帧
        BuildFrame chatFrame = new BuildFrame(BuildFrame.ChatMsg);
        //准备帧需要的实体
        IMsg entity = new MsgModel();
        //entity.setRight_name("lzy");
        entity.setDate(Tools.getDate());
        entity.setMessage("testMsg");
        entity.setMsgType(false);

        entity.setLeft_name("abc");
        //构建帧

        Protocol.Frame frame = chatFrame.GetChatMsgFrame(entity);

        System.out.println(frame.getProtoSign());
        System.out.println(frame.getMsgLength());
        System.out.println(frame.getMsgType());
        System.out.println(frame.getSenderTime());
        System.out.println(frame.getSrc().getUserName());
        System.out.println(frame.getDst().getDst(0).getUserName());
        System.out.println(frame.getMsg());
    }
    @Test
    public void byteArrayToShortTest(){
        //模拟出大端方式的int16字节数组情况
        //模拟1
        byte[] test = new byte[]{(byte)(0x01),(byte)(0x00)};
        System.out.println(test[0]);
        System.out.println(test[1]);
        short s = DeEnCode.byteArrayToShort(test[1],test[0]);
        System.out.println(s);
        assertEquals(1,s);

    }

    @Test
    public void encoeFileMsgTest(){
        File file = new File("D:/testforAndroid.txt");
        IFileMsg fileMsg = new FileMsgModel();
        fileMsg.setFile(file);
        fileMsg.setSrc_ID(1);
        fileMsg.setDst_ID(new int[]{2});
        //编码（proto头+文件）
        byte[] fileMsgHead = DeEnCode.encodeFileMsgFrameHead(fileMsg);
        byte[] fileData = DeEnCode.encodeFileToByte(file);
        byte[] fileDataMsg = DeEnCode.encodeFileMsgFrame(fileMsg);

        for (int i=0;i<fileMsgHead.length;i++)
        {
            System.out.print((int)(fileMsgHead[i]));
            System.out.print(" ");
        }
        System.out.print("\n");
        for (int i=0;i<fileData.length;i++)
        {
            System.out.print((int)(fileData[i]));
            System.out.print(" ");
        }
        System.out.print("\n");
        for (int i=0;i<fileDataMsg.length;i++)
        {
            System.out.print((int)(fileDataMsg[i]));
            System.out.print(" ");
        }
        //验证两个帧是否正确的连在一起
        int j=0,k=0;
        for (int i=0;i<fileDataMsg.length;i++)
        {
            if(i<fileMsgHead.length)
            {
                assertEquals(fileDataMsg[i],fileMsgHead[j]);
                j++;
            }
            else
            {
                assertEquals(fileDataMsg[i],fileData[k]);
                k++;
            }
        }*/
       // System.out.println(fileData);
       // System.out.println(fileDataMsg);

    //}
/*    @Test
    public void TestGetFileInfoFrame(){
        //初始化帧
        BuildFrame fileHeadFrame = new BuildFrame(BuildFrame.FileSend);
        //准备帧需要的实体
        IFileMsg fileMsg = new FileMsgModel();
        fileMsg.setSrc_ID(1);
        fileMsg.setDst_ID(new int[]{2});
        //构建帧
        Protocol.Frame frame = fileHeadFrame.GetFileInfoFrame(fileMsg);

        System.out.println(frame.getProtoSign());
        System.out.println(frame.getMsgLength());
        System.out.println(frame.getMsgType());
        System.out.println(frame.getSenderTime());
        System.out.println(frame.getSrc().getUserID());
        System.out.println(frame.getDst().getDst(0).getUserID());
    }*/
}
