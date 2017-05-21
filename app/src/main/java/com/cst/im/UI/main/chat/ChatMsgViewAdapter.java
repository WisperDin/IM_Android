package com.cst.im.UI.main.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cst.im.FileAccess.FileSweet;
import com.cst.im.NetWork.ComService;
import com.cst.im.R;
import com.cst.im.dataBase.ChatConst;
import com.cst.im.model.FileMsgModel;
import com.cst.im.model.IBaseMsg;
import com.cst.im.model.IFileMsg;
import com.cst.im.model.IPhotoMsg;
import com.cst.im.model.ISoundMsg;
import com.cst.im.model.ITextMsg;
import com.cst.im.model.PhotoMsgModel;
import com.cst.im.tools.FileUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;


/**
 * Created by wzb on 2017/5/9.
 */

public class ChatMsgViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> imageList = new ArrayList<String>();
    //key是position，value是在imageList中的位置
    private HashMap<Integer,Integer> imagePosition = new HashMap<Integer,Integer>();
    public static final int FROM_USER_MSG = 0;//接收消息类型
    public static final int TO_USER_MSG = 1;//发送消息类型
    public static final int FROM_USER_IMG = 2;//接收消息类型
    public static final int TO_USER_IMG = 3;//发送消息类型
    public static final int FROM_USER_VOICE = 4;//接收消息类型
    public static final int TO_USER_VOICE = 5;//发送消息类型
    public static final int TO_USE_FILE = 6;//发送消息类型
    public static final int FROM_USE_FILE = 7;//接收消息类型
    private int mMinItemWith;// 设置对话框的最大宽度和最小宽度
    private List<IBaseMsg> coll;// 消息对象数组
    private Map<String, Timer> timers = new Hashtable<String, Timer>();
    private int mMaxItemWith;
    public MyHandler handler;
    private Animation an;
    private SendErrorListener sendErrorListener;
    private VoiceIsRead voiceIsRead;
    public List<String> unReadPosition = new ArrayList<String>();
    private int voicePlayPosition = -1;
    private LayoutInflater mLayoutInflater;
    private boolean isGif = true;
    public boolean isPicRefresh = true;

    public interface SendErrorListener {
        public void onClick(int position);
    }

    public void setSendErrorListener(SendErrorListener sendErrorListener) {
        this.sendErrorListener = sendErrorListener;
    }

    public interface VoiceIsRead {
        public void voiceOnClick(int position);
    }

    public void setVoiceIsReadListener(VoiceIsRead voiceIsRead) {
        this.voiceIsRead = voiceIsRead;
    }
    public ChatMsgViewAdapter(Context context, List<IBaseMsg> coll) {
        this.context = context;
        this.coll = coll;
        mLayoutInflater = LayoutInflater.from(context);
        // 获取系统宽度
        WindowManager wManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wManager.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWith = (int) (outMetrics.widthPixels * 0.5f);
        mMinItemWith = (int) (outMetrics.widthPixels * 0.15f);
        handler = new MyHandler(this);
    }

    public static class MyHandler extends Handler {
        private final WeakReference<ChatMsgViewAdapter> mTbAdapter;

        public MyHandler(ChatMsgViewAdapter tbAdapter) {
            mTbAdapter = new WeakReference<ChatMsgViewAdapter>(tbAdapter);
        }

        @Override
        public void handleMessage(Message msg) {
            ChatMsgViewAdapter tbAdapter = mTbAdapter.get();

            if (tbAdapter != null) {
            }
        }
    }

    public void setIsGif(boolean isGif) {
        this.isGif = isGif;
    }

    //public void setUserList(List<ChatMessageBean> userList) {
    //   this.userList = userList;
    //}

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }
    public ArrayList<String> getImageList() {
        return  imageList;
    }

    public void setImagePosition(HashMap<Integer,Integer> imagePosition) {this.imagePosition = imagePosition;}
    public HashMap<Integer,Integer> getImagePosition() {return  imagePosition;}

    @Override
    public int getCount() {
        return coll.size();
    }

    @Override
    public Object getItem(int position) {
        return coll.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return coll.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 8;
    }


    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        IBaseMsg msg = coll.get(i);
        switch (getItemViewType(i)) {
            case FROM_USER_MSG:
                UserMsgViewHolder holder;
                if (view == null) {
                    holder = new UserMsgViewHolder();
                    view = mLayoutInflater.inflate(R.layout.activity_chat_left, null);
                    initTextMsgView(holder , view , FROM_USER_MSG);
                    view.setTag(holder);
                } else {
                    holder = (UserMsgViewHolder) view.getTag();
                }
                fromMsgUserLayout(holder, msg, i);
                break;
            case FROM_USER_IMG:
                UserImgViewHolder holder1;
                if (view == null) {
                    holder1 = new UserImgViewHolder();
                    view = mLayoutInflater.inflate(R.layout.layout_imagefrom_list_item, null);
                    initImgMsgView(holder1,view,FROM_USER_IMG);
                    view.setTag(holder1);
                } else {
                    holder1 = (UserImgViewHolder) view.getTag();
                }
                fromImgUserLayout(holder1, msg, i);
                break;
            case FROM_USER_VOICE:
                UserVoiceViewHolder holder2;
                if (view == null) {
                    holder2 = new UserVoiceViewHolder();
                    view = mLayoutInflater.inflate(R.layout.layout_voicefrom_list_item, null);
                    initVoiceMsgView(holder2,view,FROM_USER_VOICE);
                    view.setTag(holder2);
                } else {
                    holder2 = (UserVoiceViewHolder) view.getTag();
                }
                fromVoiceUserLayout(holder2, msg, i);
                break;

            case FROM_USE_FILE:
                UserFileViewHolder holder6;
                if (view == null) {
                    holder6 = new UserFileViewHolder();
                    view = mLayoutInflater.inflate(R.layout.layout_received_file, null);
                    initFileMsgView(holder6,view,FROM_USE_FILE);
                    view.setTag(holder6);
                } else {
                    holder6 = (UserFileViewHolder) view.getTag();
                }
                fromFileUserLayout(holder6, msg, i);
                break;
            case TO_USE_FILE:
                UserFileViewHolder holder7;
                if (view == null) {
                    holder7 = new UserFileViewHolder();
                    view = mLayoutInflater.inflate(R.layout.layout_sent_file, null);
                    initFileMsgView(holder7,view,TO_USE_FILE);
                    view.setTag(holder7);
                } else {
                    holder7 = (UserFileViewHolder) view.getTag();
                }
                toFileUserLayout(holder7, msg, i);
                break;
            case TO_USER_MSG:
                UserMsgViewHolder holder3;
                if (view == null) {
                    holder3 = new UserMsgViewHolder();
                    view = mLayoutInflater.inflate(R.layout.activity_chat_right, null);
                    initTextMsgView(holder3 , view ,TO_USER_MSG);
                    view.setTag(holder3);
                } else {
                    holder3 = (UserMsgViewHolder) view.getTag();
                }
                toMsgUserLayout(holder3, msg, i);
                break;
            case TO_USER_IMG:
                UserImgViewHolder holder4;
                if (view == null) {
                    holder4 = new UserImgViewHolder();
                    view = mLayoutInflater.inflate(R.layout.layout_imageto_list_item, null);
                    initImgMsgView(holder4,view,TO_USER_IMG);
                    view.setTag(holder4);
                } else {
                    holder4 = (UserImgViewHolder) view.getTag();
                }
                toImgUserLayout(holder4, msg, i);
                break;
            case TO_USER_VOICE:
                UserVoiceViewHolder holder5;
                if (view == null) {
                    holder5 = new UserVoiceViewHolder();
                    view = mLayoutInflater.inflate(R.layout.layout_voiceto_list_item, null);
                    initVoiceMsgView(holder5,view,TO_USER_VOICE);
                    view.setTag(holder5);
                } else {
                    holder5 = (UserVoiceViewHolder) view.getTag();
                }
                toVoiceUserLayout(holder5, msg, i);
                break;
        }
        return view;
    }


    public class UserFileViewHolder {
        ImageView iv;
        TextView tv;
        ProgressBar pb;
        ImageView staus_iv;
        ImageView head_iv;
        TextView tv_userId;
        ImageView playBtn;
        TextView timeLength;
        TextView size;
        LinearLayout container_status_btn;
        LinearLayout ll_container;
        ImageView iv_read_status;
        // 显示已读回执状态
        TextView tv_ack;
        // 显示送达回执状态
        TextView tv_delivered;
        TextView tv_file_name;
        TextView tv_file_size;
        TextView tv_file_download_state;
    }

    public class UserMsgViewHolder {
        public ImageView headicon;
        public TextView chat_time;
        public TextView content;
        public ImageView sendFailImg;
    }

    public class UserImgViewHolder {
        public ImageView headicon;
        public TextView chat_time;
        public LinearLayout image_group;
        public BubbleImageView image_Msg;
        public ImageView sendFailImg;
    }

    public class UserVoiceViewHolder {
        public ImageView headicon;
        public TextView chat_time;
        public LinearLayout voice_group;
        public TextView voice_time;
        public FrameLayout voice_image;
        public View receiver_voice_unread;
        public View voice_anim;
        public ImageView sendFailImg;
    }

    private void fromMsgUserLayout(final UserMsgViewHolder holder, final IBaseMsg msg, final int position) {
        holder.headicon.setBackgroundResource(R.mipmap.ic_default);
        /* time */
        initTextMsgLayout(holder,msg,position);
    }

    private void fromImgUserLayout(final UserImgViewHolder holder, final IBaseMsg msg, final int position) {
        holder.headicon.setBackgroundResource(R.mipmap.ic_default);
        /* time */
        initImgMsgLayout(holder,msg,position,FROM_USER_IMG);
    }

    private void fromVoiceUserLayout(final UserVoiceViewHolder holder, final IBaseMsg msg, final int position) {
        //final ISoundMsg soundUrl = ((ISoundMsg)msg);
        holder.headicon.setBackgroundResource(R.mipmap.ic_default);
        initVoiceMsgLayout(holder,msg,position);
        //播放语音动画效果
        AnimationDrawable drawable;
        holder.voice_anim.setId(position);
        if (position == voicePlayPosition) {
            holder.voice_anim
                    .setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
            holder.voice_anim
                    .setBackgroundResource(R.drawable.voice_play_receiver);
            drawable = (AnimationDrawable) holder.voice_anim
                    .getBackground();
            drawable.start();
        } else {
            holder.voice_anim
                    .setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
        }
        final ISoundMsg soundMsgUrl = ((ISoundMsg)msg);
        holder.voice_group.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (holder.receiver_voice_unread != null)
                    holder.receiver_voice_unread.setVisibility(View.GONE);
                holder.voice_anim
                        .setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
                stopPlayVoice();
                voicePlayPosition = holder.voice_anim.getId();
                AnimationDrawable drawable;
                holder.voice_anim
                        .setBackgroundResource(R.drawable.voice_play_receiver);
                drawable = (AnimationDrawable) holder.voice_anim
                        .getBackground();
                drawable.start();
                File file = new File(com.cst.im.tools.FileUtils.getFilePath(FileSweet.FILE_TYPE_SOUND),soundMsgUrl.getFileName());
                if(!file.exists()){
                    Log.e("fromVoiceUserLayout","file not exists");
                    return;
                }
                try{
                        soundMsgUrl.setFileUrl(file.toURL().toString()); ;
                }catch (MalformedURLException mue){
                    Log.w("to url","failed");
                    return;
                }
                if(soundMsgUrl.getFileUrl()==null){
                    Log.w("url","null");
                    return;
                }
                //处理是否已读
                if (voiceIsRead != null) {
                    voiceIsRead.voiceOnClick(position);
                }
                MediaManager.playSound(soundMsgUrl.getFileUrl(), new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        voicePlayPosition = -1;
                        holder.voice_anim.setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
                    }
                });

            }

        });
        dealVoiceTime(holder,soundMsgUrl);
    }

    private void fromFileUserLayout(final UserFileViewHolder holder, final IBaseMsg msg, final int position){
        holder.head_iv.setBackgroundResource(R.mipmap.ic_default);
        initFileMsgLayout(holder,msg,position);
        //holder.pb.setVisibility(View.INVISIBLE);
        //holder.pb.setVisibility(View.VISIBLE);
        //holder.pb.setVisibility(View.INVISIBLE);
    }
    private void toFileUserLayout(final UserFileViewHolder holder, final IBaseMsg msg, final int position){
        holder.head_iv.setBackgroundResource(R.mipmap.ic_default);
        initFileMsgLayout(holder,msg,position);
//        holder.pb.setVisibility(View.INVISIBLE);
//        holder.pb.setVisibility(View.VISIBLE);
//        holder.tv.setVisibility(View.VISIBLE);
//        holder.tv.setText(message.progress + "%");
//        holder.pb.setVisibility(View.INVISIBLE);
    }
//        holder.tv.setVisibility(View.INVISIBLE);}
//        holder.tv.setVisibility(View.INVISIBLE);
//        holder.staus_iv.setVisibility(View.INVISIBLE);
//        final Timer timer = new Timer();
//        timers.put(fileMsg.getFileUrl(), timer);
//        timer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//                activity.runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        holder.pb.setVisibility(View.VISIBLE);
//                        holder.tv.setVisibility(View.VISIBLE);
//                        //holder.tv.setText(message.progress + "%");
//                        holder.pb.setVisibility(View.INVISIBLE);
//                        holder.tv.setVisibility(View.INVISIBLE);
//                        timer.cancel();
//
//                    }
//                });
//
//            }
//        }, 0, 500);
//    }

    private void toMsgUserLayout(final UserMsgViewHolder holder, final IBaseMsg msg, final int position) {
        holder.headicon.setBackgroundResource(R.mipmap.ic_default);
        holder.headicon.setImageDrawable(context.getResources()
                .getDrawable(R.mipmap.ic_default));
        /* time */
        initTextMsgLayout(holder,msg,position);
    }

    private void toImgUserLayout(final UserImgViewHolder holder, final IBaseMsg msg, final int position) {
        holder.headicon.setBackgroundResource(R.mipmap.ic_default);
        //findViewById(R.id.image_group)含发送状态图标
//        switch (msg.getSendState()) {
//            case ChatConst.SENDING:
//                an = AnimationUtils.loadAnimation(context,
//                        R.anim.update_loading_progressbar_anim);
//                LinearInterpolator lin = new LinearInterpolator();
//                an.setInterpolator(lin);
//                an.setRepeatCount(-1);
//                holder.sendFailImg
//                        .setBackgroundResource(R.mipmap.xsearch_loading);
//                holder.sendFailImg.startAnimation(an);
//                an.startNow();
//                holder.sendFailImg.setVisibility(View.VISIBLE);
//                break;
//
//            case ChatConst.COMPLETED:
//                holder.sendFailImg.clearAnimation();
//                holder.sendFailImg.setVisibility(View.GONE);
//                break;
//
//            case ChatConst.SENDERROR:
//                holder.sendFailImg.clearAnimation();
//                holder.sendFailImg
//                        .setBackgroundResource(R.mipmap.msg_state_fail_resend_pressed);
//                holder.sendFailImg.setVisibility(View.VISIBLE);
//                holder.sendFailImg.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//                        // TODO Auto-generated method stub
//                        if (sendErrorListener != null) {
//                            sendErrorListener.onClick(position);
//                        }
//                    }
//
//                });
//                break;
//            default:
//                break;
//        }
        holder.headicon.setImageDrawable(context.getResources()
                .getDrawable(R.mipmap.ic_default));
        initImgMsgLayout(holder,msg,position ,TO_USER_IMG);
        /* time */
    }

    private void toVoiceUserLayout(final UserVoiceViewHolder holder, final IBaseMsg msg, final int position) {
        holder.headicon.setBackgroundResource(R.mipmap.ic_default);
//        switch (msg.getSendState()) {
//            case ChatConst.SENDING:
//                an = AnimationUtils.loadAnimation(context,
//                        R.anim.update_loading_progressbar_anim);
//                LinearInterpolator lin = new LinearInterpolator();
//                an.setInterpolator(lin);
//                an.setRepeatCount(-1);
//                holder.sendFailImg
//                        .setBackgroundResource(R.mipmap.xsearch_loading);
//                holder.sendFailImg.startAnimation(an);
//                an.startNow();
//                holder.sendFailImg.setVisibility(View.VISIBLE);
//                break;
//
//            case ChatConst.COMPLETED:
//                holder.sendFailImg.clearAnimation();
//                holder.sendFailImg.setVisibility(View.GONE);
//                break;
//
//            case ChatConst.SENDERROR:
//                holder.sendFailImg.clearAnimation();
//                holder.sendFailImg
//                        .setBackgroundResource(R.mipmap.msg_state_fail_resend_pressed);
//                holder.sendFailImg.setVisibility(View.VISIBLE);
//                holder.sendFailImg.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//                        // TODO Auto-generated method stub
//                        if (sendErrorListener != null) {
//                            sendErrorListener.onClick(position);
//                        }
//                    }
//
//                });
//                break;
//            default:
//                break;
//        }
        holder.headicon.setImageDrawable(context.getResources()
                .getDrawable(R.mipmap.ic_default));
        initVoiceMsgLayout(holder,msg,position);
        AnimationDrawable drawable;
        holder.voice_anim.setId(position);
        if (position == voicePlayPosition) {
            holder.voice_anim.setBackgroundResource(R.mipmap.adj);
            holder.voice_anim
                    .setBackgroundResource(R.drawable.voice_play_send);
            drawable = (AnimationDrawable) holder.voice_anim
                    .getBackground();
            drawable.start();
        } else {
            holder.voice_anim.setBackgroundResource(R.mipmap.adj);
        }
        final ISoundMsg soundMsgUrl = ((ISoundMsg)msg);
        holder.voice_group.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (holder.receiver_voice_unread != null)
                    holder.receiver_voice_unread.setVisibility(View.GONE);
                holder.voice_anim.setBackgroundResource(R.mipmap.adj);
                stopPlayVoice();
                voicePlayPosition = holder.voice_anim.getId();
                AnimationDrawable drawable;
                holder.voice_anim
                        .setBackgroundResource(R.drawable.voice_play_send);
                drawable = (AnimationDrawable) holder.voice_anim
                        .getBackground();
                drawable.start();
                String voicePath = soundMsgUrl.getFileUrl() == null ? ""
                        : soundMsgUrl.getFileUrl();
                if (voiceIsRead != null) {
                    voiceIsRead.voiceOnClick(position);
                }
                MediaManager.playSound(voicePath,
                        new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                voicePlayPosition = -1;
                                holder.voice_anim
                                        .setBackgroundResource(R.mipmap.adj);
                            }
                        });
            }

        });
        dealVoiceTime(holder,soundMsgUrl);
    }

    @SuppressLint("SimpleDateFormat")
    public String getTime(String time, String before) {
        String show_time = null;
        if (before != null) {
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date now = df.parse(time);
                java.util.Date date = df.parse(before);
                long l = now.getTime() - date.getTime();
                long day = l / (24 * 60 * 60 * 1000);
                long hour = (l / (60 * 60 * 1000) - day * 24);
                long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
                if (min >= 1) {
                    show_time = time.substring(11);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            show_time = time.substring(11);
        }
        String getDay = getDay(time);
        if (show_time != null && getDay != null)
            show_time = getDay + " " + show_time;
        return show_time;
    }

    @SuppressLint("SimpleDateFormat")
    public static String returnTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    @SuppressLint("SimpleDateFormat")
    public String getDay(String time) {
        String showDay = null;
        String nowTime = returnTime();
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date now = df.parse(nowTime);
            java.util.Date date = df.parse(time);
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            if (day >= 365) {
                showDay = time.substring(0, 10);
            } else if (day >= 1 && day < 365) {
                showDay = time.substring(5, 10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return showDay;
    }

    public void stopPlayVoice() {
        if (voicePlayPosition != -1) {
            View voicePlay = (View) ((Activity) context)
                    .findViewById(voicePlayPosition);
            if (voicePlay != null) {
                if (getItemViewType(voicePlayPosition) == FROM_USER_VOICE) {
                    voicePlay
                            .setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
                } else {
                    voicePlay.setBackgroundResource(R.mipmap.adj);
                }
            }
            MediaManager.pause();
            voicePlayPosition = -1;
        }
    }

    //设置文本聊天布局基本控件，缓存于holder
    public void initTextMsgView(UserMsgViewHolder holder , View view , int i){
        if(i == TO_USER_MSG){
            //源用户头像设置
            //发送状态设置
            holder.sendFailImg = (ImageView) view
                    .findViewById(R.id.mysend_fail_img);
        }
        //目的用户头像设置
        holder.headicon = (ImageView) view
                .findViewById(R.id.iv_userhead);
        holder.chat_time = (TextView) view.findViewById(R.id.tv_sendtime);
        holder.content = (TextView) view.findViewById(R.id.tv_chatcontent);
    }
    public void initTextMsgLayout(UserMsgViewHolder holder , IBaseMsg msg , int position){
        if (position != 0) {
            String showTime = getTime(msg.getMsgDate(), coll.get(position - 1).getMsgDate());
            if (showTime != null) {
                holder.chat_time.setVisibility(View.VISIBLE);
                holder.chat_time.setText(showTime);
            } else {
                holder.chat_time.setVisibility(View.GONE);
            }
        } else {
            String showTime = getTime(msg.getMsgDate(), null);
            holder.chat_time.setVisibility(View.VISIBLE);
            holder.chat_time.setText(showTime);
        }
        holder.content.setVisibility(View.VISIBLE);
        SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context, ((ITextMsg)msg).getText());
        holder.content.setText(spannableString);
    }
    //设置图片聊天布局基本控件，缓存于holder
    public void initImgMsgView(UserImgViewHolder holder , View view , int i){
        if(i == FROM_USER_IMG){
            holder.headicon = (ImageView) view
                    .findViewById(R.id.tb_other_user_icon);
            holder.chat_time = (TextView) view.findViewById(R.id.chat_time);
        }
        if(i == TO_USER_IMG){
            holder.headicon = (ImageView) view
                    .findViewById(R.id.tb_my_user_icon);
            holder.chat_time = (TextView) view
                    .findViewById(R.id.mychat_time);
            holder.sendFailImg = (ImageView) view
                    .findViewById(R.id.mysend_fail_img);
            holder.image_group = (LinearLayout) view
                    .findViewById(R.id.image_group);
        }
        holder.image_Msg = (BubbleImageView) view
                .findViewById(R.id.image_message);
    }
    public void initImgMsgLayout(UserImgViewHolder holder , IBaseMsg msg , final int position , int i){
        if (position != 0) {
            String showTime = getTime(msg.getMsgDate(), coll.get(position - 1).getMsgDate());
            if (showTime != null) {
                holder.chat_time.setVisibility(View.VISIBLE);
                holder.chat_time.setText(showTime);
            } else {
                holder.chat_time.setVisibility(View.GONE);
            }
        } else {
            String showTime = getTime(msg.getMsgDate(), null);
            holder.chat_time.setVisibility(View.VISIBLE);
            holder.chat_time.setText(showTime);
        }
        if (isPicRefresh) {
            if(i == TO_USER_IMG){
                holder.image_group.setVisibility(View.VISIBLE);
            }
            IPhotoMsg photoUrl = ((IPhotoMsg)msg);
            final String imageSrc = photoUrl.getFileUrl() == null ? "" :
                    photoUrl.getPhotoLocal();
            final String imageUrlSrc = photoUrl.getFileUrl() == null ? "" :
                    photoUrl.getFileUrl();
            final String imageIconUrl = photoUrl.getFileUrl() == null ? ""
                    : photoUrl.getFileUrl();
            File file = new File(imageSrc);
            final boolean hasLocal = !imageSrc.equals("")
                    && FileSaveUtil.isFileExists(file);
            int res;
            res = R.drawable.chatfrom_bg_focused;
            if (hasLocal) {
                holder.image_Msg.setLocalImageBitmap(ImageCheckoutUtil.getLoacalBitmap(imageSrc),
                        res);
            } else {
                holder.image_Msg.load(imageIconUrl, res, R.mipmap.cygs_cs);
            }
            holder.image_Msg.load(imageIconUrl, res, R.mipmap.cygs_cs);
            holder.image_Msg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // TODO Auto-generated method stub
                    stopPlayVoice();
                    Intent intent = new Intent(context, ImageViewActivity.class);
                    intent.putStringArrayListExtra("images", imageList);
                    intent.putExtra("clickedIndex", imagePosition.get(position));
                    context.startActivity(intent);
                }

            });
        }
    }
    //设置语音聊天布局基本控件，缓存于holder
    public void initVoiceMsgView(UserVoiceViewHolder holder , View view , int i){
        if(i == TO_USER_VOICE){
            holder.headicon = (ImageView) view
                    .findViewById(R.id.tb_my_user_icon);
            holder.chat_time = (TextView) view
                    .findViewById(R.id.mychat_time);
            holder.voice_image = (FrameLayout) view
                    .findViewById(R.id.voice_image);
            holder.voice_anim = (View) view
                    .findViewById(R.id.id_recorder_anim);
            //holder.sendFailImg = (ImageView) view
             //       .findViewById(R.id.mysend_fail_img);
        }
        if(i == FROM_USER_VOICE){
            holder.headicon = (ImageView) view
                    .findViewById(R.id.tb_other_user_icon);
            holder.chat_time = (TextView) view.findViewById(R.id.chat_time);
            holder.receiver_voice_unread = (View) view
                    .findViewById(R.id.receiver_voice_unread);
            holder.voice_image = (FrameLayout) view
                    .findViewById(R.id.voice_receiver_image);
            holder.voice_anim = (View) view
                    .findViewById(R.id.id_receiver_recorder_anim);
        }
        holder.voice_group = (LinearLayout) view
                .findViewById(R.id.voice_group);
        holder.voice_time = (TextView) view
                .findViewById(R.id.voice_time);
    }
    public void initVoiceMsgLayout(UserVoiceViewHolder holder , IBaseMsg msg , int position){
        /* time */
        if (position != 0) {
            String showTime = getTime(msg.getMsgDate(), coll.get(position - 1).getMsgDate());
            if (showTime != null) {
                holder.chat_time.setVisibility(View.VISIBLE);
                holder.chat_time.setText(showTime);
            } else {
                holder.chat_time.setVisibility(View.GONE);
            }
        } else {
            String showTime = getTime(msg.getMsgDate(), null);
            holder.chat_time.setVisibility(View.VISIBLE);
            holder.chat_time.setText(showTime);
        }

        holder.voice_group.setVisibility(View.VISIBLE);
        if (holder.receiver_voice_unread != null)
            holder.receiver_voice_unread.setVisibility(View.GONE);
        if (holder.receiver_voice_unread != null && unReadPosition != null) {
            for (String unRead : unReadPosition) {
                if (unRead.equals(position + "")) {
                    holder.receiver_voice_unread
                            .setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
    }
    public void dealVoiceTime(UserVoiceViewHolder holder ,ISoundMsg soundMsgUrl){
        float voiceTime = soundMsgUrl.getUserVoiceTime();
        BigDecimal b = new BigDecimal(voiceTime);
        float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        holder.voice_time.setText(f1 + "\"");
        ViewGroup.LayoutParams lParams = holder.voice_image
                .getLayoutParams();
        lParams.width = (int) (mMinItemWith + mMaxItemWith / 10f
                * soundMsgUrl.getUserVoiceTime());
        holder.voice_image.setLayoutParams(lParams);
    }
    //设置文件聊天布局基本控件，缓存于holder
    public void initFileMsgView(UserFileViewHolder holder , View view , int i){
        holder.head_iv = (ImageView) view
                .findViewById(R.id.iv_userhead);
        holder.tv_file_name = (TextView) view
                .findViewById(R.id.tv_file_name);
        holder.tv_file_size = (TextView) view
                .findViewById(R.id.tv_file_size);
//        holder.pb = (ProgressBar) view
//                .findViewById(R.id.pb_sending);
        holder.tv = (TextView)view.findViewById(R.id.timestamp);
        holder.staus_iv = (ImageView) view
                .findViewById(R.id.msg_status);
        holder.tv_file_download_state = (TextView) view
                .findViewById(R.id.tv_file_state);
        holder.ll_container = (LinearLayout) view
                .findViewById(R.id.ll_file_container);
        // 这里是进度值
//        holder.tv = (TextView) view
//                .findViewById(R.id.percentage);
//        holder.tv_userId = (TextView) view
//                .findViewById(R.id.tv_userid);
    }
    public void initFileMsgLayout(UserFileViewHolder holder , IBaseMsg msg ,int position ){
        final IFileMsg fileMsg = ((FileMsgModel)msg);
        holder.tv_file_name.setText(fileMsg.getFileName());
        holder.tv_file_size.setText(fileMsg.getFileSize());
        String showTime = getTime(msg.getMsgDate(), coll.get(position - 1).getMsgDate());
        if (showTime != null) {
            holder.tv.setVisibility(View.VISIBLE);
            holder.tv.setText(showTime);
        } else {
            holder.tv.setVisibility(View.GONE);
        }
        holder.ll_container.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //TODO 这里是直接打开下载目录的文件路径（其实暗含了默认接受下载的逻辑，若需要做选择性接收，这里需要改）

                File file = new File(FileUtils.getFilePath(FileSweet.FILE_TYPE_FILE),FileUtils.getFileNameNoEx(fileMsg.getFileName()));
                String fileMIME = DealFileTypeUtils.getMIMEType(fileMsg.getFileName());
                if (file != null && file.exists()) {
                    // 文件存在，直接打开
                    //再传一个后缀
                    DealFileTypeUtils.openFile(Uri.fromFile(file),fileMIME,(Activity) context);
                } else {
/*                    // 下载
                    context.startActivity(new Intent(context,
                            ShowNormalFileActivity.class).putExtra("msgbody",
                            (Serializable) fileMsg));*/
                }
            }
        });
    }
}
