package com.cst.im.UI.main.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.util.DisplayMetrics;
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
import android.widget.TextView;

import com.cst.im.R;
import com.cst.im.dataBase.ChatConst;
import com.cst.im.model.IBaseMsg;
import com.cst.im.model.IPhotoMsg;
import com.cst.im.model.ISoundMsg;
import com.cst.im.model.ITextMsg;
import com.cst.im.model.PhotoMsgModel;

import java.io.File;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wzb on 2017/5/9.
 */

public class ChatMsgViewAdapter extends BaseAdapter {
    private Context context;
    //private List<ChatMessageBean> userList = new ArrayList<ChatMessageBean>();
    private ArrayList<String> imageList = new ArrayList<String>();
    private HashMap<Integer,Integer> imagePosition = new HashMap<Integer,Integer>();
    public static final int FROM_USER_MSG = 0;//接收消息类型
    public static final int TO_USER_MSG = 1;//发送消息类型
    public static final int FROM_USER_IMG = 2;//接收消息类型
    public static final int TO_USER_IMG = 3;//发送消息类型
    public static final int FROM_USER_VOICE = 4;//接收消息类型
    public static final int TO_USER_VOICE = 5;//发送消息类型
    private int mMinItemWith;// 设置对话框的最大宽度和最小宽度
    private List<IBaseMsg> coll;// 消息对象数组
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

    public void setImagePosition(HashMap<Integer,Integer> imagePosition) {
        this.imagePosition = imagePosition;
    }

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
        return 6;
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
                fromMsgUserLayout((FromUserMsgViewHolder) holder, msg, i);
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
                fromVoiceUserLayout((FromUserVoiceViewHolder) holder2, msg, i);
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
                toMsgUserLayout((ToUserMsgViewHolder) holder3, msg, i);
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
                toVoiceUserLayout((ToUserVoiceViewHolder) holder5, msg, i);
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
        holder.headicon.setBackgroundResource(R.mipmap.tongbao_hiv);
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
        holder.headicon.setBackgroundResource(R.mipmap.tongbao_hiv);
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
        final ISoundMsg soundUrl = ((ISoundMsg)msg);
        holder.headicon.setBackgroundResource(R.mipmap.tongbao_hiv);
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
//                String voicePath = tbub.getUserVoicePath() == null ? "" : tbub
//                        .getUserVoicePath();
//                File file = new File(voicePath);
//                if (!(!voicePath.equals("") && FileSaveUtil
//                        .isFileExists(file))) {
//                    voicePath = tbub.getUserVoiceUrl() == null ? ""
//                            : tbub.getUserVoiceUrl();
//                }
//                voicePath = soundUrl.getSoundUrl();
                if (voiceIsRead != null) {
                    voiceIsRead.voiceOnClick(position);
                }
                MediaManager.playSound(soundUrl.getSoundUrl(),
                        new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                voicePlayPosition = -1;
                                holder.voice_anim
                                        .setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
                            }
                        });
            }

        });
        float voiceTime = soundUrl.getUserVoiceTime();
        BigDecimal b = new BigDecimal(voiceTime);
        float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        holder.voice_time.setText(f1 + "\"");
        ViewGroup.LayoutParams lParams = holder.voice_image
                .getLayoutParams();
        lParams.width = (int) (mMinItemWith + mMaxItemWith / 60f
                * soundUrl.getUserVoiceTime());
        holder.voice_image.setLayoutParams(lParams);
    }

    private void toMsgUserLayout(final ToUserMsgViewHolder holder, final IBaseMsg msg, final int position) {
        holder.headicon.setBackgroundResource(R.mipmap.grzx_tx_s);
        holder.headicon.setImageDrawable(context.getResources()
                .getDrawable(R.mipmap.grzx_tx_s));
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
        holder.headicon.setBackgroundResource(R.mipmap.grzx_tx_s);
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
                .getDrawable(R.mipmap.grzx_tx_s));

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

    private void toVoiceUserLayout(final ToUserVoiceViewHolder holder, final IBaseMsg msg, final int position) {
        holder.headicon.setBackgroundResource(R.mipmap.grzx_tx_s);
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
                .getDrawable(R.mipmap.grzx_tx_s));

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
}
