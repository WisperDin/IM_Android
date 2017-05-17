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

import com.baidu.mapapi.model.LatLng;
import com.cst.im.FileAccess.FileSweet;
import com.cst.im.R;
import com.cst.im.dataBase.ChatConst;
import com.cst.im.model.FileMsgModel;
import com.cst.im.model.IBaseMsg;
import com.cst.im.model.IFileMsg;
import com.cst.im.model.ILocationMsg;
import com.cst.im.model.IPhotoMsg;
import com.cst.im.model.ISoundMsg;
import com.cst.im.model.ITextMsg;
import com.cst.im.model.LocationMsgModel;
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
    public static final int TO_USER_LOCATION = 8;
    public static final int FROM_USER_LOCATION = 9;
    private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
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
        return 10;
    }


    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        IBaseMsg msg = coll.get(i);
        switch (getItemViewType(i)) {
            case FROM_USER_MSG:
                FromUserMsgViewHolder holder;
                if (view == null) {
                    holder = new FromUserMsgViewHolder();
                    view = mLayoutInflater.inflate(R.layout.activity_chat_left, null);
                    holder.headicon = (ImageView) view
                            .findViewById(R.id.iv_userhead);
                    holder.chat_time = (TextView) view.findViewById(R.id.tv_sendtime);
                    holder.content = (TextView) view.findViewById(R.id.tv_chatcontent);
                    view.setTag(holder);
                } else {
                    holder = (FromUserMsgViewHolder) view.getTag();
                }
                fromMsgUserLayout(holder, msg, i);
                break;
            case FROM_USER_IMG:
                FromUserImageViewHolder holder1;
                if (view == null) {
                    holder1 = new FromUserImageViewHolder();
                    view = mLayoutInflater.inflate(R.layout.layout_imagefrom_list_item, null);
                    holder1.headicon = (ImageView) view
                            .findViewById(R.id.tb_other_user_icon);
                    holder1.chat_time = (TextView) view.findViewById(R.id.chat_time);
                    holder1.image_Msg = (BubbleImageView) view
                            .findViewById(R.id.image_message);
                    view.setTag(holder1);
                } else {
                    holder1 = (FromUserImageViewHolder) view.getTag();
                }
                fromImgUserLayout(holder1, msg, i);
                break;
            case FROM_USER_VOICE:
                FromUserVoiceViewHolder holder2;
                if (view == null) {
                    holder2 = new FromUserVoiceViewHolder();
                    view = mLayoutInflater.inflate(R.layout.layout_voicefrom_list_item, null);
                    holder2.headicon = (ImageView) view
                            .findViewById(R.id.tb_other_user_icon);
                    holder2.chat_time = (TextView) view.findViewById(R.id.chat_time);
                    holder2.voice_group = (LinearLayout) view
                            .findViewById(R.id.voice_group);
                    holder2.voice_time = (TextView) view
                            .findViewById(R.id.voice_time);
                    holder2.receiver_voice_unread = (View) view
                            .findViewById(R.id.receiver_voice_unread);
                    holder2.voice_image = (FrameLayout) view
                            .findViewById(R.id.voice_receiver_image);
                    holder2.voice_anim = (View) view
                            .findViewById(R.id.id_receiver_recorder_anim);
                    view.setTag(holder2);
                } else {
                    holder2 = (FromUserVoiceViewHolder) view.getTag();
                }
                fromVoiceUserLayout(holder2, msg, i);
                break;

            case FROM_USE_FILE:
                FromUserFileViewHolder holder6;
                if (view == null) {
                    holder6 = new FromUserFileViewHolder();
                    view = mLayoutInflater.inflate(R.layout.layout_received_file, null);
                    holder6.head_iv = (ImageView) view
                            .findViewById(R.id.iv_userhead);
                    holder6.tv_file_name = (TextView) view
                            .findViewById(R.id.tv_file_name);
                    holder6.tv_file_size = (TextView) view
                            .findViewById(R.id.tv_file_size);
                    holder6.pb = (ProgressBar) view
                            .findViewById(R.id.pb_sending);
                    holder6.staus_iv = (ImageView) view
                            .findViewById(R.id.msg_status);
                    holder6.tv_file_download_state = (TextView) view
                            .findViewById(R.id.tv_file_state);
                    holder6.ll_container = (LinearLayout) view
                            .findViewById(R.id.ll_file_container);
                    // 这里是进度值
                    holder6.tv = (TextView) view
                            .findViewById(R.id.percentage);
                    holder6.tv_userId = (TextView) view
                            .findViewById(R.id.tv_userid);
                    view.setTag(holder6);
                } else {
                    holder6 = (FromUserFileViewHolder) view.getTag();
                }
                fromFileUserLayout(holder6, msg, i);
                break;
            case TO_USE_FILE:
                FromUserFileViewHolder holder7;
                if (view == null) {
                    holder7 = new FromUserFileViewHolder();
                    view = mLayoutInflater.inflate(R.layout.layout_sent_file, null);
                    holder7.head_iv = (ImageView) view
                            .findViewById(R.id.iv_userhead);
                    holder7.tv_file_name = (TextView) view
                            .findViewById(R.id.tv_file_name);
                    holder7.tv_file_size = (TextView) view
                            .findViewById(R.id.tv_file_size);
                    holder7.pb = (ProgressBar) view
                            .findViewById(R.id.pb_sending);
                    holder7.staus_iv = (ImageView) view
                            .findViewById(R.id.msg_status);
                    holder7.tv_file_download_state = (TextView) view
                            .findViewById(R.id.tv_file_state);
                    holder7.ll_container = (LinearLayout) view
                            .findViewById(R.id.ll_file_container);
                    // 这里是进度值
                    holder7.tv = (TextView) view
                            .findViewById(R.id.percentage);
                    holder7.tv_userId = (TextView) view
                            .findViewById(R.id.tv_userid);
                    view.setTag(holder7);
                } else {
                    holder7 = (FromUserFileViewHolder) view.getTag();
                }
                toFileUserLayout(holder7, msg, i);
                break;
            case TO_USER_MSG:
                ToUserMsgViewHolder holder3;
                if (view == null) {
                    holder3 = new ToUserMsgViewHolder();
                    view = mLayoutInflater.inflate(R.layout.activity_chat_right, null);
                    holder3.headicon = (ImageView) view
                            .findViewById(R.id.iv_userhead);
                    holder3.chat_time = (TextView) view
                            .findViewById(R.id.tv_sendtime);
                    holder3.content = (TextView) view
                            .findViewById(R.id.tv_chatcontent);
                    holder3.sendFailImg = (ImageView) view
                            .findViewById(R.id.mysend_fail_img);
                    view.setTag(holder3);
                } else {
                    holder3 = (ToUserMsgViewHolder) view.getTag();
                }
                toMsgUserLayout(holder3, msg, i);
                break;
            case TO_USER_IMG:
                ToUserImgViewHolder holder4;
                if (view == null) {
                    holder4 = new ToUserImgViewHolder();
                    view = mLayoutInflater.inflate(R.layout.layout_imageto_list_item, null);
                    holder4.headicon = (ImageView) view
                            .findViewById(R.id.tb_my_user_icon);
                    holder4.chat_time = (TextView) view
                            .findViewById(R.id.mychat_time);
                    holder4.sendFailImg = (ImageView) view
                            .findViewById(R.id.mysend_fail_img);
                    holder4.image_group = (LinearLayout) view
                            .findViewById(R.id.image_group);
                    holder4.image_Msg = (BubbleImageView) view
                            .findViewById(R.id.image_message);
                    view.setTag(holder4);
                } else {
                    holder4 = (ToUserImgViewHolder) view.getTag();
                }
                toImgUserLayout(holder4, msg, i);
                break;
            case TO_USER_VOICE:
                ToUserVoiceViewHolder holder5;
                if (view == null) {
                    holder5 = new ToUserVoiceViewHolder();
                    view = mLayoutInflater.inflate(R.layout.layout_voiceto_list_item, null);
                    holder5.headicon = (ImageView) view
                            .findViewById(R.id.tb_my_user_icon);
                    holder5.chat_time = (TextView) view
                            .findViewById(R.id.mychat_time);
                    holder5.voice_group = (LinearLayout) view
                            .findViewById(R.id.voice_group);
                    holder5.voice_time = (TextView) view
                            .findViewById(R.id.voice_time);
                    holder5.voice_image = (FrameLayout) view
                            .findViewById(R.id.voice_image);
                    holder5.voice_anim = (View) view
                            .findViewById(R.id.id_recorder_anim);
                    holder5.sendFailImg = (ImageView) view
                            .findViewById(R.id.mysend_fail_img);
                    view.setTag(holder5);
                } else {
                    holder5 = (ToUserVoiceViewHolder) view.getTag();
                }
                toVoiceUserLayout(holder5, msg, i);
                break;
            case TO_USER_LOCATION:
                FromUserFileViewHolder holder8;
                if (view == null) {
                    holder8 = new FromUserFileViewHolder();
                    view = mLayoutInflater.inflate(R.layout.row_sent_location, null);
                    holder8.head_iv = (ImageView) view
                            .findViewById(R.id.iv_userhead);
                    holder8.tv = (TextView) view
                            .findViewById(R.id.tv_location);
                    holder8.pb = (ProgressBar) view
                            .findViewById(R.id.pb_sending);
                    holder8.staus_iv = (ImageView) view
                            .findViewById(R.id.msg_status);
                    holder8.tv_userId = (TextView) view
                            .findViewById(R.id.tv_userid);
                    view.setTag(holder8);
                } else {
                    holder8 = (FromUserFileViewHolder) view.getTag();
                }
                handleLocationMessage(holder8, msg,i ,view);
                break;
            case FROM_USER_LOCATION:
                FromUserFileViewHolder holder9;
                if (view == null) {
                    holder9 = new FromUserFileViewHolder();
                    view = mLayoutInflater.inflate(R.layout.row_received_location, null);
                    holder9.head_iv = (ImageView) view
                            .findViewById(R.id.iv_userhead);
                    holder9.tv = (TextView) view
                            .findViewById(R.id.tv_location);
                    holder9.pb = (ProgressBar) view
                            .findViewById(R.id.pb_sending);
                    holder9.staus_iv = (ImageView) view
                            .findViewById(R.id.msg_status);
                    holder9.tv_userId = (TextView) view
                            .findViewById(R.id.tv_userid);
                    view.setTag(holder9);
                } else {
                    holder9 = (FromUserFileViewHolder) view.getTag();
                }
                handleLocationMessage(holder9, msg,i ,view);
                break;
        }

        return view;
    }

    public class FromUserMsgViewHolder {
        public ImageView headicon;
        public TextView chat_time;
        public TextView content;
    }

    public class FromUserImageViewHolder {
        public ImageView headicon;
        public TextView chat_time;
        public BubbleImageView image_Msg;
    }

    public class FromUserVoiceViewHolder {
        public ImageView headicon;
        public TextView chat_time;
        public LinearLayout voice_group;
        public TextView voice_time;
        public FrameLayout voice_image;
        public View receiver_voice_unread;
        public View voice_anim;
    }

    public class FromUserFileViewHolder {
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

    public class ToUserMsgViewHolder {
        public ImageView headicon;
        public TextView chat_time;
        public TextView content;
        public ImageView sendFailImg;
    }

    public class ToUserImgViewHolder {
        public ImageView headicon;
        public TextView chat_time;
        public LinearLayout image_group;
        public BubbleImageView image_Msg;
        public ImageView sendFailImg;
    }

    public class ToUserVoiceViewHolder {
        public ImageView headicon;
        public TextView chat_time;
        public LinearLayout voice_group;
        public TextView voice_time;
        public FrameLayout voice_image;
        public View receiver_voice_unread;
        public View voice_anim;
        public ImageView sendFailImg;
    }

    private void fromMsgUserLayout(final FromUserMsgViewHolder holder, final IBaseMsg msg, final int position) {
        holder.headicon.setBackgroundResource(R.mipmap.ic_default);
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
        holder.content.setVisibility(View.VISIBLE);
        SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context, ((ITextMsg)msg).getText());
        holder.content.setText(spannableString);
    }

    private void fromImgUserLayout(final FromUserImageViewHolder holder, final IBaseMsg msg, final int position) {
        holder.headicon.setBackgroundResource(R.mipmap.ic_default);
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
        if (isPicRefresh) {
            holder.image_Msg.setImageBitmap(null);
            IPhotoMsg photoUrl = ((IPhotoMsg)msg);
            final String imageSrc = photoUrl.getPhotoUrl() == null ? "" :
                    photoUrl.getPhotoLocal();
            final String imageUrlSrc = photoUrl.getPhotoUrl() == null ? "" :
                    photoUrl.getPhotoUrl();
            final String imageIconUrl = photoUrl.getPhotoUrl() == null ? ""
                    : photoUrl.getPhotoUrl();
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

    private void fromVoiceUserLayout(final FromUserVoiceViewHolder holder, final IBaseMsg msg, final int position) {
        //final ISoundMsg soundUrl = ((ISoundMsg)msg);
        holder.headicon.setBackgroundResource(R.mipmap.ic_default);
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
                        soundMsgUrl.setSoundUrl(file.toURL().toString()); ;
                }catch (MalformedURLException mue){
                    Log.w("to url","failed");
                    return;
                }
                if(soundMsgUrl.getSoundUrl()==null){
                    Log.w("url","null");
                    return;
                }
                //处理是否已读
                if (voiceIsRead != null) {
                    voiceIsRead.voiceOnClick(position);
                }
                MediaManager.playSound(soundMsgUrl.getSoundUrl(), new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        voicePlayPosition = -1;
                        holder.voice_anim.setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
                    }
                });

            }

        });
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

    private void fromFileUserLayout(final FromUserFileViewHolder holder, final IBaseMsg msg, final int position){

        holder.head_iv.setBackgroundResource(R.mipmap.ic_default);
        final IFileMsg fileMsg = ((FileMsgModel)msg);
//        final String filePath = fileMsg.getFileUrl();
        holder.tv_file_name.setText(fileMsg.getFileName());
        holder.tv_file_size.setText(fileMsg.getFileSize());
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


        //holder.pb.setVisibility(View.INVISIBLE);
        //holder.pb.setVisibility(View.VISIBLE);
        holder.pb.setVisibility(View.INVISIBLE);

    }
    private void toFileUserLayout(final FromUserFileViewHolder holder, final IBaseMsg msg, final int position){
        holder.head_iv.setBackgroundResource(R.mipmap.ic_default);
        /* time */
//        final NormalFileMessageBody fileMessageBody = (NormalFileMessageBody) message
//                .getBody();
        final IFileMsg fileMsg = ((FileMsgModel)msg);
//        final String filePath = fileMsg.getFileUrl();
        holder.tv_file_name.setText(fileMsg.getFileName());
        holder.tv_file_size.setText(fileMsg.getFileSize());
        holder.ll_container.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
/*                if(fileMsg.getFileUrl()==null){
                    Log.e("toFileUserLayout","fileURL null");
                    return;
                }*/
                File file = new File(fileMsg.getFile().getAbsolutePath());
                if (file != null && file.exists()) {
                    // 文件存在，直接打开
                    DealFileTypeUtils.openFile(file, (Activity) context);
                } else {
/*                    // 下载
                    context.startActivity(new Intent(context,
                            ShowNormalFileActivity.class).putExtra("msgbody",
                            (Serializable) fileMsg));*/
                }
            }
        });

//        holder.pb.setVisibility(View.INVISIBLE);
//        holder.pb.setVisibility(View.VISIBLE);
//        holder.tv.setVisibility(View.VISIBLE);
        //holder.tv.setText(message.progress + "%");
        holder.pb.setVisibility(View.INVISIBLE);
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

    private void toMsgUserLayout(final ToUserMsgViewHolder holder, final IBaseMsg msg, final int position) {
        holder.headicon.setBackgroundResource(R.mipmap.ic_default);
        holder.headicon.setImageDrawable(context.getResources()
                .getDrawable(R.mipmap.ic_default));
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
        holder.content.setVisibility(View.VISIBLE);
        SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context, ((ITextMsg)msg).getText());
        holder.content.setText(spannableString);
    }

    private void toImgUserLayout(final ToUserImgViewHolder holder, final IBaseMsg msg, final int position) {
        IPhotoMsg photoMsgUrl = ((PhotoMsgModel)msg);
        holder.headicon.setBackgroundResource(R.mipmap.ic_default);
        switch (msg.getSendState()) {
            case ChatConst.SENDING:
                an = AnimationUtils.loadAnimation(context,
                        R.anim.update_loading_progressbar_anim);
                LinearInterpolator lin = new LinearInterpolator();
                an.setInterpolator(lin);
                an.setRepeatCount(-1);
                holder.sendFailImg
                        .setBackgroundResource(R.mipmap.xsearch_loading);
                holder.sendFailImg.startAnimation(an);
                an.startNow();
                holder.sendFailImg.setVisibility(View.VISIBLE);
                break;

            case ChatConst.COMPLETED:
                holder.sendFailImg.clearAnimation();
                holder.sendFailImg.setVisibility(View.GONE);
                break;

            case ChatConst.SENDERROR:
                holder.sendFailImg.clearAnimation();
                holder.sendFailImg
                        .setBackgroundResource(R.mipmap.msg_state_fail_resend_pressed);
                holder.sendFailImg.setVisibility(View.VISIBLE);
                holder.sendFailImg.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Auto-generated method stub
                        if (sendErrorListener != null) {
                            sendErrorListener.onClick(position);
                        }
                    }

                });
                break;
            default:
                break;
        }
        holder.headicon.setImageDrawable(context.getResources()
                .getDrawable(R.mipmap.ic_default));

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

        if (isPicRefresh) {
            holder.image_Msg.setImageBitmap(null);
            holder.image_group.setVisibility(View.VISIBLE);
            IPhotoMsg photoUrl = ((IPhotoMsg)msg);
            final String imageSrc = photoUrl.getPhotoUrl() == null ? "" :
                    photoUrl.getPhotoLocal();
            final String imageUrlSrc = photoUrl.getPhotoUrl() == null ? "" :
                    photoUrl.getPhotoUrl();
            final String imageIconUrl = photoUrl.getPhotoUrl() == null ? ""
                    : photoUrl.getPhotoUrl();
            File file = new File(imageSrc);
            final boolean hasLocal = !imageSrc.equals("")
                    && FileSaveUtil.isFileExists(file);
            int res;
            res = R.drawable.chat_to_bg_normal;
            //判断本地是否存在此图片
            if (hasLocal) {
                holder.image_Msg.setLocalImageBitmap(ImageCheckoutUtil.getLoacalBitmap(imageSrc),
                        res);
            } else {
                holder.image_Msg.load(imageUrlSrc, res, R.mipmap.cygs_cs);
            }
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
    /**
     * 处理位置消息
     *
     * @param msg
     * @param holder
     * @param position
     * @param convertView
     */
    private void handleLocationMessage(final FromUserFileViewHolder holder, final IBaseMsg msg, final int position ,View convertView) {
        TextView locationView = ((TextView) convertView
                .findViewById(R.id.tv_location));
        ILocationMsg localMsg = (LocationMsgModel)msg;
        LocationMessageBody locBody = localMsg.getLocalBody();
        locationView.setText(locBody.getAddress());
        LatLng loc = new LatLng(locBody.getLatitude(), locBody.getLongitude());
        locationView.setOnClickListener(new MapClickListener(loc, locBody
                .getAddress()));

//        if (message.direct == EMMessage.Direct.RECEIVE) {
//            return;
//        }
        // deal with send message
//        switch (message.status) {
//            case SUCCESS:
                holder.pb.setVisibility(View.GONE);
                holder.staus_iv.setVisibility(View.GONE);
//                break;
//            case FAIL:
//                holder.pb.setVisibility(View.GONE);
//                holder.staus_iv.setVisibility(View.VISIBLE);
//                break;
//            case INPROGRESS:
//                holder.pb.setVisibility(View.VISIBLE);
//                break;
//            default:
                //sendMsgInBackground(message, holder);
 //       }
    }

    private void toVoiceUserLayout(final ToUserVoiceViewHolder holder, final IBaseMsg msg, final int position) {
        holder.headicon.setBackgroundResource(R.mipmap.ic_default);
        switch (msg.getSendState()) {
            case ChatConst.SENDING:
                an = AnimationUtils.loadAnimation(context,
                        R.anim.update_loading_progressbar_anim);
                LinearInterpolator lin = new LinearInterpolator();
                an.setInterpolator(lin);
                an.setRepeatCount(-1);
                holder.sendFailImg
                        .setBackgroundResource(R.mipmap.xsearch_loading);
                holder.sendFailImg.startAnimation(an);
                an.startNow();
                holder.sendFailImg.setVisibility(View.VISIBLE);
                break;

            case ChatConst.COMPLETED:
                holder.sendFailImg.clearAnimation();
                holder.sendFailImg.setVisibility(View.GONE);
                break;

            case ChatConst.SENDERROR:
                holder.sendFailImg.clearAnimation();
                holder.sendFailImg
                        .setBackgroundResource(R.mipmap.msg_state_fail_resend_pressed);
                holder.sendFailImg.setVisibility(View.VISIBLE);
                holder.sendFailImg.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Auto-generated method stub
                        if (sendErrorListener != null) {
                            sendErrorListener.onClick(position);
                        }
                    }

                });
                break;
            default:
                break;
        }
        holder.headicon.setImageDrawable(context.getResources()
                .getDrawable(R.mipmap.ic_default));

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
                String voicePath = soundMsgUrl.getSoundUrl() == null ? ""
                        : soundMsgUrl.getSoundUrl();
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

    /*
     * 点击地图消息listener
     */
    class MapClickListener implements View.OnClickListener {

        LatLng location;
        String address;

        public MapClickListener(LatLng loc, String address) {
            location = loc;
            this.address = address;

        }

        @Override
        public void onClick(View v) {
            Intent intent;
            intent = new Intent(context, BaiduMapActivity.class);
            intent.putExtra("latitude", location.latitude);
            intent.putExtra("longitude", location.longitude);
            intent.putExtra("address", address);
            context.startActivity(intent);
        }

    }
}
