package com.cst.im.NetWork;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cst.im.NetWork.proto.BuildFrame;
import com.cst.im.model.FileMsgModel;
import com.cst.im.model.FriendModel;
import com.cst.im.model.IBaseMsg;
import com.cst.im.model.IFriend;
import com.cst.im.model.PhotoMsgModel;
import com.cst.im.model.SoundMsgModel;
import com.cst.im.model.TextMsgModel;
import com.cst.im.model.UserModel;
import com.cst.im.presenter.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import protocol.Protocol.Action;
import protocol.Protocol.Frame;
import protocol.Protocol.User;

/**
 * Created by cjwddz on 2017/4/23.
 */

public class ComService extends TcpService {
    public interface MsgHandler{
        //void handleEvent(Frame frame);
        void handleFbEvent(int rslCode,int id);//参数为反馈的状态码与状态信息
    }
    public interface ChatMsgHandler{
        void handleChatMsgEvent(IBaseMsg msgRecv);//参数为接收到的消息
    }
    //TODO 其实可以直接用上面的
    public interface ChatListHandler{
        void handleChatListEvent(IBaseMsg msgRecv);//参数为接收到的消息
    }


/*    public interface FileMsgHandler{
        void handleFileMsgEvent(IBaseMsg msgRecv);//参数为接收到的文件简要信息
    }*/

    public interface AddFriendHandler {
        void handleAddFriendEvent(IFriend f1);
    }

    public interface FriendListHandler {
        void handleFriendLisEvent(IFriend fl);
    }

    public interface IsFriendHandler {
        void handleIsFriendEvent(IFriend fl);
    }
    public interface UserInfoHandler{
        void handlePullUserInfoEvent(int rslCode,UserModel userModel); // 加载远程用户数据结果
        void handlePushUserInfoEvent(int rslCode); // 上传本地用户数据结果
    }


    static CopyOnWriteArrayList<MsgHandler> msgListeners;
    static MsgHandler registerEvent;
    static MsgHandler loginFbEvent;
    static ChatMsgHandler chatMsgEvent;
    static FriendListHandler FriendListEvent;
    static ChatListHandler chatListEvent;
    static IsFriendHandler isfriendEvent;
    static AddFriendHandler addfriendEvent;
    static UserInfoHandler userInfoHandler;

    //static FileMsgHandler fileMsgHandler;

    public static void setRegisterCallback(MsgHandler registerCallback){registerEvent =registerCallback;}
    public static void setLoginCallback(MsgHandler loginCallback){
        loginFbEvent=loginCallback;
    }
    public static void setChatMsgCallback(ChatMsgHandler chatMsgCallback){chatMsgEvent=chatMsgCallback;}
    //public static void setFileMsgCallback(FileMsgHandler fileMsgCallback){fileMsgHandler=fileMsgCallback;}
    public static void setChatListCallback(ChatListHandler chatListCallback) {chatListEvent=chatListCallback;}
    public static void setFriendListCallback(FriendListHandler FriendListCallback){FriendListEvent=FriendListCallback;}
    public static void setIsfriendCallback(IsFriendHandler IsFriendCallback){isfriendEvent=IsFriendCallback;}
    public static void setaddfriendCallback(AddFriendHandler addFriendCallback){addfriendEvent=addFriendCallback;}
    public static void setUserInfoHandler(UserInfoHandler userInfoHandlerCallback){userInfoHandler = userInfoHandlerCallback;}
    @Override
    public void OnTcpStop() {
        // TODO: 2017/4/26 tcp连接断开处理
    }
    @Override
    public void OnMessageCome(Frame frame) {
        if(frame==null){
            Log.e("OnMessageCome","frame null");
            return;
        }
        //根据消息类型分发客户端收到的消息
        switch (frame.getMsgType()){
            case BuildFrame.FeedBack://反馈信息
            {
                Log.d("OnMessage","feedback");
                Action action =  frame.getFbAction();//若空则返回默认类型
                User user = frame.getSrc();
                //选择反馈信息的类型
                switch (action.getActionType()){
                    case BuildFrame.Login://登录反馈信息
                        Log.d("OnMessageCome","登录反馈");
                        if(loginFbEvent!=null)//执行登录反馈事件
                            loginFbEvent.handleFbEvent(action.getRslCode(),user.getUserID());
                        break;
                    case BuildFrame.Register://注册反馈信息
                        Log.d("OnMessageCome","注册反馈");
                        if(registerEvent!=null)//执行登录反馈事件
                            registerEvent.handleFbEvent(action.getRslCode(),0);
                        break;
                    default:
                        Log.e("OnMessageCome","FeedBack 反馈类型不正确");
                }
                break;
            }
            case BuildFrame.TextMsg://聊天消息
            case BuildFrame.FileInfo://文件简要消息
            {
                //检查目的用户数量是否>0,srcid不等于0
                if (frame.getDst().getDstCount()<=0||frame.getSrc().getUserID()==0||frame.getSrc().getUserName()==""){
                    Log.e(" bad value", "ComService,OnMessageCome ChatMsg");
                    return;
                }
                System.out.println("chatMsg");
                IBaseMsg baseMsg = null;
                //实例化对象
                if(frame.getMsgType()==BuildFrame.TextMsg){
                    if(frame.getMsg().getMsg()==""){//空消息不被允许
                        Log.e(" bad value", "ComService,OnMessageCome TextMsg");
                        return;
                    }
                    baseMsg = new TextMsgModel();
                    ((TextMsgModel)baseMsg).setText(frame.getMsg().getMsg());
                    baseMsg.setSrc_Name(frame.getSrc().getUserName());
                    baseMsg.setMsgType(IBaseMsg.MsgType.TEXT);
                }else if(frame.getMsgType()==BuildFrame.FileInfo){
                    //关于文件信息参数的检查
                    if(frame.getFileInfo().getFileName()==""||frame.getFileInfo().getFileType()==0||
                            frame.getFileInfo().getFileParam()==""||frame.getFileInfo().getFileFeature()==""){
                        Log.e(" bad value", "ComService,OnMessageCome FileInfo fileParam");
                        return;
                    }
                    switch (frame.getFileInfo().getFileType()){
                        case 2://图片
                            baseMsg = new PhotoMsgModel();
                            baseMsg.setMsgType(IBaseMsg.MsgType.PHOTO);
                            ((PhotoMsgModel) baseMsg).setFileSize(frame.getFileInfo().getFileParam());
                            break;
                        case 3://文件
                            baseMsg = new FileMsgModel();
                            baseMsg.setMsgType(IBaseMsg.MsgType.FILE);
                            ((FileMsgModel) baseMsg).setFileSize(frame.getFileInfo().getFileParam());
                            break;
                        case 4://声音
                            baseMsg = new SoundMsgModel();
                            baseMsg.setMsgType(IBaseMsg.MsgType.SOUNDS);
                            ((SoundMsgModel) baseMsg).setUserVoiceTime(Float.parseFloat(frame.getFileInfo().getFileParam()));
                            break;
                    }
                    ((FileMsgModel) baseMsg).setFileName(frame.getFileInfo().getFileName());
                    ((FileMsgModel) baseMsg).setFileFeature(frame.getFileInfo().getFileFeature());

                }
                if(baseMsg==null){
                    Log.e(" bad value", "ComService,baseMsg null");
                    return;
                }
                //初始化一些公有的东西
                int dst[] = new int[frame.getDst().getDstCount()];
                for(int i = 0 ; i < frame.getDst().getDstCount() ; i++){
                    if(frame.getDst().getDst(i).getUserID()==0){
                        Log.e(" bad value", "ComService,chatMsg dstID equal 0");
                        return;
                    }
                    dst[i] = frame.getDst().getDst(i).getUserID();
                }
                baseMsg.setSrc_Name(frame.getSrc().getUserName());
                baseMsg.setSrc_ID(frame.getSrc().getUserID());
                baseMsg.setDst_ID(dst);
                baseMsg.setMsgDate(Tools.getDate());
                if(chatMsgEvent!=null)//执行收到聊天消息反馈事件
                    chatMsgEvent.handleChatMsgEvent(baseMsg);
                if(chatListEvent!=null)
                    chatListEvent.handleChatListEvent(baseMsg);
                break;
            }

            case BuildFrame.GetFriend://好友列表信息
            {
                if(frame.getDst().getDstCount()<=0) {
                    Log.w("onMessageCome","GetFriend null");
                    return;
                }

                Log.d("OnMessage", "feedbackofFriendlist");
                ArrayList<String> list = new ArrayList<String>();
                HashMap<String ,Integer> NameAndID = new HashMap<String , Integer>();
                for (int i = 0; i < frame.getDst().getDstCount(); i++) {
                    if(frame.getDst().getDst(i).getUserName()==""||frame.getDst().getDst(i).getUserID()==0){
                        Log.w("onMessageCome","GetFriend bad value");
                        return;
                    }

                    list.add(frame.getDst().getDst(i).getUserName());
                    NameAndID.put(frame.getDst().getDst(i).getUserName(),frame.getDst().getDst(i).getUserID());
                }
                IFriend myfriend = new FriendModel(list,NameAndID);
                FriendModel.InitFriendModel(list,NameAndID);
                FriendListEvent.handleFriendLisEvent(myfriend);
                break;
            }


            case BuildFrame.IsFriend://判断是否为好友
            {
                IFriend IsFriend;
                if(frame.getDst().getDstCount()<=0){
                    Log.w("onMessageCome","IsFriend null");
                    IsFriend=new FriendModel();
                }else{
                    IsFriend=new FriendModel(frame.getDst().getDst(0).getUserName());
                    if(frame.getDst().getDst(0).getUserID()!=0){
                        IsFriend.SetRealtCode(1);
                    }
                }
                Log.d("OnMessage", "feedbackofIsFriend");
                isfriendEvent.handleIsFriendEvent(IsFriend);
                break;
            }

            case BuildFrame.AddFriend://添加好友
            {
                IFriend addFriend;
                if(frame.getDst().getDstCount()<=0){
                    Log.w("onMessageCome","AddFriend null");
                    addFriend=new FriendModel();
                }else{
                    addFriend=new FriendModel(frame.getDst().getDst(0).getUserID(),frame.getDst().getDst(0).getUserName());
                }
                Log.d("OnMessage", "feedbackofAddFriend");
                addFriend.SetRealtCode(frame.getFbAction().getRslCode());
                addfriendEvent.handleAddFriendEvent(addFriend);
                break;
            }
            /*case BuildFrame.FileInfo://文件简要消息
            {
                System.out.println("fileInfo");
                //检查是否空
                if (frame.getSrc()==null&&frame.getDst()==null||frame.getDst().getDstCount()<=0){
                    Log.e(" bad value", "ConService,OnMessageCome FileInfo");
                    System.out.println("ConService,OnMessageCome FileInfo bad value");
                    return;
                }
                    int dst[] = new int[frame.getDst().getDstCount()+1];
                    for(int i = 0 ; i < frame.getDst().getDstCount() ; i++){
                        dst[i] = frame.getDst().getDst(i).getUserID();
                    }
                IFileMsg fileMsg = new FileMsgModel();
                fileMsg.setSrc_ID(frame.getSrc().getUserID());
                fileMsg.setDst_ID(dst);
                fileMsg.setMsgDate(Tools.getDate());
                if(fileMsgHandler!=null)//执行收到文件消息反馈事件
                    fileMsgHandler.handleFileMsgEvent(fileMsg);

                break;
            }*/
            case BuildFrame.PullUserInfo:{ // 获取远程用户信息
                Log.d("Service","pullUserInfo");
                User user = frame.getSrc();
                if(user.getUserID() != UserModel.localUser.getId()){
                    Log.w("pull from wrong id",String.format("origin id = %s,local id = %s",user.getUserID(),UserModel.localUser.getId()));
                    userInfoHandler.handlePullUserInfoEvent(BuildFrame.PullUserInfoFail,null);
                    return;
                }
                if(frame.getFbAction().getRslCode() == BuildFrame.PullUserInfoFail){//获取远程用户失败
                    userInfoHandler.handlePullUserInfoEvent(BuildFrame.PullUserInfoFail,null);
                    return;
                }
                UserModel localUser = new UserModel();
                localUser.setAge(user.getAge());
                localUser.setUserRealName(user.getRealName());
                localUser.setUserSign(user.getSign());
                localUser.setUserSex(user.getSex());
                localUser.setUserAddress(user.getAddress());
                localUser.setUserEmail(user.getEmail());
                localUser.setUserPhone(user.getPhone());
                userInfoHandler.handlePullUserInfoEvent(BuildFrame.PullUserInfoSuccess,localUser);
            }break;

            case BuildFrame.PushUserInfo:{ //上传用户数据
                if(frame.getFbAction().getRslCode() == BuildFrame.PushUserInfoFail){//获取远程用户失败
                    userInfoHandler.handlePushUserInfoEvent(BuildFrame.PushUserInfoFail);
                    return;
                }
                User user = frame.getSrc();
                UserModel localUser = new UserModel();
                localUser.setAge(user.getAge());
                localUser.setUserRealName(user.getRealName());
                localUser.setUserSign(user.getSign());
                localUser.setUserSex(user.getSex());
                localUser.setUserAddress(user.getAddress());
                localUser.setUserEmail(user.getEmail());
                localUser.setUserPhone(user.getPhone());
                userInfoHandler.handlePushUserInfoEvent(BuildFrame.PushUserInfoSuccess);

            }break;
            default:
                Log.w("OnMessageCome","msgType异常");
                break;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBind();
    }
    public class MyBind extends Binder {
        public ComService getService(){
            return ComService.this;
        }
    }
}
