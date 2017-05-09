package com.cst.im.UI.main.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cst.im.R;
import com.cst.im.model.IBaseMsg;
import com.cst.im.model.IPhotoMsg;
import com.cst.im.model.ISoundMsg;
import com.cst.im.model.ITextMsg;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 消息ListView的Adapter
 *
 * @author way
 */
public class ChatMsgViewAdapter extends BaseAdapter {

    public static interface IMsgViewType {
        int IMVT_COM_MSG = 0;// 收到对方的消息
        int IMVT_TO_MSG = 1;// 自己发送出去的消息
    }

    private static final int ITEMCOUNT = 8;// 消息类型的总数
    private List<IBaseMsg> coll;// 消息对象数组
    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<String> imageList = new ArrayList<String>();
    private HashMap<Integer,Integer> imagePosition = new HashMap<Integer,Integer>();
    private int mMinItemWith;// 设置对话框的最大宽度和最小宽度
    private int mMaxItemWith;
    private VoiceIsRead voiceIsRead;
    public List<String> unReadPosition = new ArrayList<String>();
    private int voicePlayPosition = -1;
    public static final int FROM_USER_VOICE = 4;//接受语音信息



    //判断语音是否已读
    public interface VoiceIsRead {
        public void voiceOnClick(int position);
    }
    //监听语音按钮
    public void setVoiceIsReadListener(VoiceIsRead voiceIsRead) {
        this.voiceIsRead = voiceIsRead;
    }


    public ChatMsgViewAdapter(Context context, List<IBaseMsg> coll) {
        this.coll = coll;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }



    public int getCount() {
        return coll.size();
    }

    public Object getItem(int position) {
        return coll.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }
    public void setImagePosition(HashMap<Integer,Integer> imagePosition) {
        this.imagePosition = imagePosition;
    }

    /**
     * 得到Item的类型，是对方发过来的消息，还是自己发送出去的
     */
    public int getItemViewType(int position) {
        IBaseMsg entity = coll.get(position);

        if (entity.sendOrRecv()) {//收到的消息
            return IMsgViewType.IMVT_COM_MSG;
        } else {//自己发送的消息
            return IMsgViewType.IMVT_TO_MSG;
        }
    }

    /**
     * Item类型的总数
     */
    public int getViewTypeCount() {
        return ITEMCOUNT;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        IBaseMsg msg = coll.get(position);
        boolean isComMsg = msg.sendOrRecv();
        convertView = judgeMsgType(msg , convertView ,isComMsg , position);
        return convertView;
    }

    static class ViewHolder {
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;
        public BubbleImageView image_Msg;
        public LinearLayout image_group;

        public LinearLayout voice_group;
        public TextView voice_time;
        public FrameLayout voice_image;
        public View receiver_voice_unread;
        public View voice_anim;
        public boolean isComMsg = true;
    }

    //将数据加载到listView
    public View judgeMsgType(IBaseMsg msg, View convertView, boolean isComMsg  , final int position){
        if(msg.getMsgType() == null){
            msg.setMsgType(IBaseMsg.MsgType.TEXT);
        }
        ViewHolder viewHolder;
        switch(msg.getMsgType()){
            case TEXT:
                /**获取文本布局*/
                if (convertView == null) {
                    //获取文本信息布局
                    convertView = judgeSendOrRecv(msg , isComMsg);
                    viewHolder = new ViewHolder();
                    viewHolder.tvSendTime = (TextView) convertView
                            .findViewById(R.id.tv_sendtime);
                    viewHolder.tvContent = (TextView) convertView
                            .findViewById(R.id.tv_chatcontent);
                    viewHolder.isComMsg = isComMsg;
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.tvSendTime.setText(msg.getMsgDate());
                SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context, ((ITextMsg)msg).getText());
                viewHolder.tvContent.setText(spannableString);
                return convertView;
            case PHOTO:
                /**获取照片信息布局**/
                //获取图片URL
                IPhotoMsg photoMsg = ((IPhotoMsg)msg);
                //获取加载图片的气泡
                int res_p;
                res_p = getImageChatBubble(isComMsg);
                final String imageIconUrl = photoMsg.getPhotoUrl() == null ? ""
                        : photoMsg.getPhotoUrl();
                if (convertView == null) {
                    convertView = judgeSendOrRecv(msg , isComMsg);
                    viewHolder = new ViewHolder();
                    viewHolder.tvSendTime = (TextView) convertView
                            .findViewById(R.id.tv_sendtime);
                    viewHolder.isComMsg = isComMsg;
                    viewHolder.image_Msg = (BubbleImageView) convertView
                            .findViewById(R.id.image_message);
                    viewHolder.image_group = (LinearLayout) convertView
                            .findViewById(R.id.image_group);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.tvSendTime.setText(msg.getMsgDate());
                viewHolder.image_Msg.load(imageIconUrl, res_p, R.mipmap.cygs_cs);
                viewHolder.image_Msg.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(context, ImageViewActivity.class);
                        intent.putStringArrayListExtra("images", imageList);
                        intent.putExtra("clickedIndex", imagePosition.get(position));
                        context.startActivity(intent);
                    }

                });
                return convertView;
            case SOUNDS:
                //获取声音URL
                //ISoundMsg soundMsg = ((ISoundMsg)msg);
                if (convertView == null) {
                    convertView = judgeSendOrRecv(msg , isComMsg);
                    viewHolder = new ViewHolder();
                    viewHolder.tvSendTime = (TextView) convertView
                            .findViewById(R.id.tv_sendtime);
                    viewHolder.isComMsg = isComMsg;
                    getSoundStatus(viewHolder , isComMsg , position , msg);
                    viewHolder.voice_group.setVisibility(View.VISIBLE);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                viewHolder.tvSendTime.setText(msg.getMsgDate());
                //viewHolder.image_Msg.load(imageIconUrl, res, R.mipmap.cygs_cs);


        }
        Log.d("ChatMsgAdapter" , "_______________聊天数据、布局加载失败__________________");
        return null;
    }

    //初始化收到信息的Item
    public View changeView_From(IBaseMsg msg){
        switch(msg.getMsgType()){
            case TEXT:
                View convertView_tl = mInflater.inflate(R.layout.activity_chat_left, null);
                return convertView_tl;
            case SOUNDS:
                View convertView_sl = mInflater.inflate(R.layout.layout_voicefrom_list_item, null);
                return convertView_sl;
            case PHOTO:
                View convertView_pl = mInflater.inflate(R.layout.layout_imagefrom_list_item, null);
                return convertView_pl;
            case FILE:
                break;
        }
        Log.d("初始化聊天界面Item","________初始化聊天界面Item失败_________________");
        return null;
    }
    //初始化发送信息的Item
    public View changeView_To(IBaseMsg msg){
        switch(msg.getMsgType()){
            case TEXT:
                View convertView_tr = mInflater.inflate(R.layout.activity_chat_right, null);
                return convertView_tr;
            case SOUNDS:
                View convertView_sr = mInflater.inflate(R.layout.layout_voiceto_list_item, null);
                return convertView_sr;
            case PHOTO:
                View convertView_pr = mInflater.inflate(R.layout.layout_imageto_list_item, null);
                return convertView_pr;
            case FILE:
                break;
        }
        Log.d("初始化聊天界面Item","________初始化聊天界面Item失败_________________");
        return null;
    }

    public View judgeSendOrRecv(IBaseMsg msg, boolean isComMsg){
        View convertView;
        if (isComMsg) {
            //加载收到数据的Item
            convertView = changeView_From(msg);
        } else {
            //加载发送数据的Item
            convertView = changeView_To(msg);
        }
        return convertView;
    }
    public ChatMsgViewAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        // 获取系统宽度
        WindowManager wManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wManager.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWith = (int) (outMetrics.widthPixels * 0.5f);
        mMinItemWith = (int) (outMetrics.widthPixels * 0.15f);
        //handler = new MyHandler(this);
    }
    //获取图片气泡
    public int getImageChatBubble(boolean isComing){
        int res;
        if(isComing){
            res = R.drawable.chat_from_bg_normal;
            return res;
        } else {
            res = R.drawable.chat_to_bg_normal;
            return res;
        }
    }

    //获取语音信息阅读状态
    public void getSoundStatus(final ViewHolder viewHolder , boolean isComming , final int position , IBaseMsg msg){
        final ISoundMsg soundMsgUrl = ((ISoundMsg)msg);
        if(isComming){
            /**接受语音信息处理*/
            if (viewHolder.receiver_voice_unread != null)
                viewHolder.receiver_voice_unread.setVisibility(View.GONE);
            if (viewHolder.receiver_voice_unread != null && unReadPosition != null) {
                for (String unRead : unReadPosition) {
                    if (unRead.equals(position + "")) {
                        viewHolder.receiver_voice_unread
                                .setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
            AnimationDrawable drawable;
            viewHolder.voice_anim.setId(position);
            if (position == voicePlayPosition) {
                viewHolder.voice_anim
                        .setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
                viewHolder.voice_anim
                        .setBackgroundResource(R.drawable.voice_play_receiver);
                drawable = (AnimationDrawable) viewHolder.voice_anim
                        .getBackground();
                drawable.start();
            } else {
                viewHolder.voice_anim
                        .setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
            }
            viewHolder.voice_group.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (viewHolder.receiver_voice_unread != null)
                        viewHolder.receiver_voice_unread.setVisibility(View.GONE);
                    viewHolder.voice_anim
                            .setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
                    stopPlayVoice();
                    voicePlayPosition = viewHolder.voice_anim.getId();
                    AnimationDrawable drawable;
                    viewHolder.voice_anim
                            .setBackgroundResource(R.drawable.voice_play_receiver);
                    drawable = (AnimationDrawable) viewHolder.voice_anim
                            .getBackground();
                    drawable.start();
                    String voicePath = soundMsgUrl.getSoundUrl() == null ? "" : soundMsgUrl.getSoundUrl();
//                    File file = new File(voicePath);
//                    if (!(!voicePath.equals("") && FileSaveUtil
//                            .isFileExists(file))) {
//                        voicePath = tbub.getUserVoiceUrl() == null ? ""
//                                : tbub.getUserVoiceUrl();
//                    }
                    if (voiceIsRead != null) {
                        voiceIsRead.voiceOnClick(position);
                    }
                    MediaManager.playSound(voicePath,
                            new MediaPlayer.OnCompletionListener() {

                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    voicePlayPosition = -1;
                                    viewHolder.voice_anim
                                            .setBackgroundResource(R.mipmap.receiver_voice_node_playing003);
                                }
                            });
                }

            });
            float voiceTime = soundMsgUrl.getUserVoiceTime();
            BigDecimal b = new BigDecimal(voiceTime);
            float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            viewHolder.voice_time.setText(f1 + "\"");
            ViewGroup.LayoutParams lParams = viewHolder.voice_image
                    .getLayoutParams();
            lParams.width = (int) (mMinItemWith + mMaxItemWith / 60f
                    * soundMsgUrl.getUserVoiceTime());
            viewHolder.voice_image.setLayoutParams(lParams);
        }
        else{
            /**发送语音信息出来*/

        }
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