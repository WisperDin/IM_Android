package com.cst.im.UI.main.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cst.im.R;
import com.cst.im.UI.main.msg.MsgFragment;
import com.cst.im.dataBase.DBManager;
import com.cst.im.model.IBaseMsg;
import com.cst.im.model.IPhotoMsg;
import com.cst.im.model.ISoundMsg;
import com.cst.im.model.ITextMsg;
import com.cst.im.model.SoundMsgModel;
import com.cst.im.model.UserModel;
import com.cst.im.presenter.ChatPresenter;
import com.cst.im.presenter.IChatPresenter;
import com.cst.im.tools.RecordUtils;
import com.cst.im.tools.UriUtils;
import com.cst.im.view.IChatView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

import static com.cst.im.UI.main.chat.ChatMsgViewAdapter.returnTime;


public class ListViewChatActivity extends SwipeBackActivity implements View.OnClickListener, IChatView {
    private SQLiteOpenHelper helper;//从数据库获取历史消息
    private Button mBtnBack;// 返回btn
    private TextView mSendBtn;//发送按钮
    private ListView mListView;//消息列表
    public ChatMsgViewAdapter mAdapter;// 消息视图的Adapter
    public ArrayList<String> imageList = new ArrayList<String>();//adapter图片数据
    public HashMap<Integer, Integer> imagePosition = new HashMap<Integer, Integer>();//图片下标位置
    private SendMessageHandler sendMessageHandler;
    private TextView opposite_name;     //显示聊天对象名字
    public List<IBaseMsg> msg_List = new ArrayList<IBaseMsg>();
    //抽象出聊天的业务逻辑
    private IChatPresenter chatPresenter;


    /**
     * edit bar & plus button panel
     */
    private EditText mSendEdt; // 输入框
    private KPSwitchPanelLinearLayout mPanelRoot; // 面板
    private ImageView mPlusIv; // 加号按钮
    private ImageView mPictureBtn; //发送按钮
    private ImageView mFileBtn; // 发送文件
    private ImageView mVoiceKeyboard;  // 语音按钮后面的键盘按钮
    private ImageView mEmojiKeyboard; // Emoji 后面的键盘按钮
    private ImageView mVoiceBtn; // 语音按钮
    private Button mVoicePressBtn; // 按住说话按钮

    private static final int IMAGE_SIZE = 100 * 1024;// 300kb
    public static final int SEND_OK = 0x1110;
    public static final int REFRESH = 0x0011;
    public static final int RECERIVE_OK = 0x1111;
    public static final int PULL_TO_REFRESH_DOWN = 0x0111;

    //打开文件
    private static final int FILE_REQUEST = 0;
    //打开照相机获取图片
    private static final int PHOTO_REQUEST_CAREMA = 1;
    // 从相册中选择图片
    private static final int PHOTO_REQUEST_GALLERY = 2;
    //拍摄视频
    private static final int VIDEO_REQUEST_CAREMA = 3;

    //临时文件
    private File tempPhotoFile;
    private File tempVideoFile;

    //目的用户信息
    private ArrayList<UserModel> dstUsers = new ArrayList<UserModel>();
    //目的用户信息，数组形态
    private UserModel[] dst;

    private ISoundMsg soundMsg;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //新页面接收数据
        Bundle bundle;
        bundle = this.getIntent().getExtras();
        UserModel dstUser = new UserModel(bundle.getString("dstName"), "", bundle.getInt("dstId"));
        dstUsers.add(dstUser);
        dst = new UserModel[dstUsers.size()];
        dstUsers.toArray(dst);
        Toast.makeText(this, dstUser.getName() + " " + dstUser.getId(), Toast.LENGTH_LONG).show();


        //数据库的创建及调用
        helper = DBManager.getIntance(this);
        helper.getWritableDatabase();

        //InitData();//本地数据库测试

        //从数据库获取聊天数据
        final List<IBaseMsg> msg_list = DBManager.QueryMsg(dstUser.getId());


        initView(bundle.getString("dstName"), msg_list);// 初始化view

        //初始化数据（MVP）
        chatPresenter = new ChatPresenter(this, msg_list);

        // 监控键盘与面板高度
        doAttach();
        // 监听加号按钮以及输入框内容变化
        setListener();

        // 初始化声音实体
        soundMsg = new SoundMsgModel();

    }

    // 监控键盘与面板高度
    private void doAttach() {
        /**
         * 这个Util主要是监控键盘的状态: 显示与否 以及 键盘的高度
         */
        KeyboardUtil.attach(this, mPanelRoot);

        /**
         * 这个Util主要是协助处理一些面板与键盘相关的事件。
         */
        KPSwitchConflictUtil.attach(mPanelRoot, mPlusIv, mSendEdt);
    }

    // 监听加号按钮以及输入框内容变化
    private void setListener() {
        // 监听加号加号按钮
        mPlusIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoicePressBtn.getVisibility() == View.VISIBLE) {
                    mVoicePressBtn.setVisibility(View.GONE);
                    mSendEdt.setVisibility(View.VISIBLE);
                }
                if (mPanelRoot.getVisibility() == View.VISIBLE) {
                    showKeyboard();
                } else {
                    hideKeyboard();
                    mPanelRoot.setVisibility(View.VISIBLE);
                }
            }
        });

        mSendEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mSendBtn.setVisibility(View.VISIBLE);
                } else {
                    mSendBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 触摸空白处隐藏面板
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    hideKeyboard();
                    mPanelRoot.setVisibility(View.GONE);
                }

                return false;
            }
        });

        mVoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示按住说话的按钮
                mSendEdt.setVisibility(View.GONE);
                mVoicePressBtn.setVisibility(View.VISIBLE);

                // 语音按钮设置不可见，并显示键盘按钮，同时显示软键盘
                v.setVisibility(View.GONE);
                mVoiceKeyboard.setVisibility(View.VISIBLE);
                hideKeyboard();
            }
        });

        mVoiceKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置按住说话不可见，设置键盘按钮不可见，语音按钮可见
                mVoicePressBtn.setVisibility(View.GONE);
                mSendEdt.setVisibility(View.VISIBLE);
                mVoiceKeyboard.setVisibility(View.GONE);
                mVoiceBtn.setVisibility(View.VISIBLE);
                showKeyboard();
            }
        });



        /** 按住说话录音，松开停止录音 */
        mVoicePressBtn.setOnTouchListener(new View.OnTouchListener() {
            long startVoiceT,endVoiceT;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    startVoiceT =  System.currentTimeMillis();
                    mVoicePressBtn.setText("松开 结束");
                    // 初始化录音对象
                    if (RecordUtils.initRecord()) {
                        //录音之前停止播放
                        if(RecordUtils.player.isPlaying()){
                            RecordUtils.stopAudio();
                        }
                        //开始录音
                        RecordUtils.startRecord();
                    }

                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    endVoiceT = System.currentTimeMillis();
                    mVoicePressBtn.setText("按住 说话");

                    /*if((endVoiceT - startVoiceT)/1000 < 3){
                        Log.d("Record","startTime : " + String.valueOf(startVoiceT));
                        Log.d("Record","endTime : " + String.valueOf(endVoiceT));
                        Toast.makeText(ListViewChatActivity.this,"录音时长过短",Toast.LENGTH_SHORT).show();
                        return false;
                    }*/
                    // 停止录音
                    RecordUtils.stopRecord();
                    // 释放录音对象
                    RecordUtils.releaseRecorder();

                    /* TODO:准备发送 */
                    try {
/*                        String filePath = RecordUtils.getAudioPath();

                        // 设置录音的时长
                        RecordUtils.player.setDataSource(filePath);
                        int duration = RecordUtils.player.getDuration();
                        Log.d("Record","duration : " + String.valueOf(duration));
                        soundMsg.setUserVoiceTime(duration);*/

                        // 设置 URL
/*                        File file = new File(filePath);
                        URL url;
                        url = file.toURL();
                        Log.d("Record","URL : " + url.toString());
                        soundMsg.setSoundUrl(url.toString());*/

                        // 设置时间戳
/*                        soundMsg.setMsgDate(Tools.getDate());
                        Log.d("Record","Time : " + Tools.getDate());
                        IBaseMsg.MsgType msgType = null;*/

                        //msgType = IBaseMsg.MsgType.SOUNDS;
                        chatPresenter.SendFile(dst, new File(RecordUtils.getAudioPath()) ,IBaseMsg.MsgType.SOUNDS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // 播放录音
                    //RecordUtils.playAudio(RecordUtils.getAudioPath());

                }
                return false;
            }
        });

        mBtnBack.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);
        mFileBtn.setOnClickListener(this);
        mPictureBtn.setOnClickListener(this);
    }

    // 弹出键盘
    private void showKeyboard() {
        mSendEdt.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) mSendEdt.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(mSendEdt, 0);
    }

    //隐藏键盘
    private void hideKeyboard() {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mSendEdt.clearFocus();
        imm.hideSoftInputFromWindow(mSendEdt.getWindowToken(), 0);
    }

    //接收到消息就会执行
    @Override
    public void onRecvMsg() {
        mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
        mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
    }


    @Override
    public void onSendMsg() {
    }

    /**
     * 初始化view
     */
    public void initView(String dst_Name, List<IBaseMsg> msg_list) {
        sendMessageHandler = new SendMessageHandler(this);
        mListView = (ListView) findViewById(R.id.content_list);
        mAdapter = new ChatMsgViewAdapter(this, msg_list);
        mListView.setAdapter(mAdapter);
        //消息列表选择到最后一行
        mListView.setSelection(mAdapter.getCount() - 1);
        mAdapter.isPicRefresh = true;
        mBtnBack = (Button) findViewById(R.id.btn_back);
        opposite_name = (TextView) findViewById(R.id.opposite_name);
        opposite_name.setText(dst_Name);


        /**
         * 初始化输入框
         */
        mSendEdt = (EditText) findViewById(R.id.send_edt);
        mPanelRoot = (KPSwitchPanelLinearLayout) findViewById(R.id.panel_root);
        mPlusIv = (ImageView) findViewById(R.id.plus_iv);
        mSendBtn = (TextView) findViewById(R.id.btn_send);
        mPictureBtn = (ImageView) findViewById(R.id.chat_picture);
        mFileBtn = (ImageView) findViewById(R.id.chat_file);
        mVoiceBtn = (ImageView) findViewById(R.id.voice_btn);
        mVoiceKeyboard = (ImageView) findViewById(R.id.voice_keyboard);
        mVoicePressBtn = (Button) findViewById(R.id.voice_press_btn);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //TODO EMOJI
/*        if (keyCode == KeyEvent.KEYCODE_BACK
                *//*&& ((FaceRelativeLayout) findViewById(R.id.FaceRelativeLayout))
                .hideFaceView()*//*) {
            return true;
        }*/
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && ((FaceRelativeLayout) findViewById(R.id.FaceRelativeLayout))
//                .hideFaceView()) {
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


    /*
     * 从相册获取
      */
    private void GetImgFromGallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        try {
            // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
            startActivityForResult(Intent.createChooser(intent, "选择图片"), PHOTO_REQUEST_GALLERY);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "抱歉,不存在图库", Toast.LENGTH_SHORT).show();
        }
    }

    //选择文件
    private void GetFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择文件"), FILE_REQUEST);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "抱歉,不存在文件管理器", Toast.LENGTH_SHORT).show();
        }
    }

    //获取视频文件从摄像头
    private void GetVideoFromCam() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.addCategory("android.intent.category.DEFAULT");
        tempPhotoFile = new File(this.getCacheDir(), "testVideoFileFromCam.avi");
        // 从文件中创建uri
        Uri uri = Uri.fromFile(tempPhotoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, VIDEO_REQUEST_CAREMA);


    }

    /*
    * 判断sdcard是否被挂载
    */
 /*     private boolean hasSdcard() {
          if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
              return true;
          } else{
              return false;
          }
      }*/
    private void GetPhotoFromCamera() {
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
           /* tempFile = new File(Environment.getExternalStorageDirectory(),
                    "test");*/
        tempPhotoFile = new File("/", "testPhotoFileFromCam.jpg");
        // 从文件中创建uri
        Uri uri = Uri.fromFile(tempPhotoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回按钮点击事件
                MsgFragment.myAdapter.notifyDataSetChanged();
                finish();// 结束,实际开发中，可以返回主界面
                break;

            case R.id.btn_send://发送聊天信息
                Log.d("Send", "Send____________________________________________________");
                sendMessage();
                //chatPresenter.SendMsg(dst, mSendEdt.getText().toString());
                break;
            case R.id.chat_file://发送文件
                Log.d("Viewing", "File----");
                GetFile();
                break;
            case R.id.chat_picture://发送图片
                Log.d("Viewing", "Photo----");
                GetImgFromGallery();

                break;
        }
    }

    /**
     * 发送文字
     */
    protected void sendMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatPresenter.SendMsg(dst, mSendEdt.getText().toString());
                sendMessageHandler.sendEmptyMessage(SEND_OK);
            }
        }).start();

    }


    /**
     * 发送图片
     */
    int i = 0;

    @Override
    public void onSendImg(final IPhotoMsg msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //判断发送状态
//                if (i == 0) {
//                    msg_List.add(getTbub(userName, ChatListViewAdapter.TO_USER_IMG, null, null, null, filePath, null, null,
//                            0f, ChatConst.SENDING));
//                } else if (i == 1) {
//                    tblist.add(getTbub(userName, ChatListViewAdapter.TO_USER_IMG, null, null, null, filePath, null, null,
//                            0f, ChatConst.SENDERROR));
//                } else if (i == 2) {
//                    tblist.add(getTbub(userName, ChatListViewAdapter.TO_USER_IMG, null, null, null, filePath, null, null,
//                            0f, ChatConst.COMPLETED));
//                    i = -1;
//                }
                msg.getPhotoUrl();
                msg.setType(ChatMsgViewAdapter.TO_USER_IMG);
                msg_List.add(msg);
                imageList.add(msg_List.get(msg_List.size() - 1).getPhotoLocal());
                imagePosition.put(msg_List.size() - 1, imageList.size() - 1);
                sendMessageHandler.sendEmptyMessage(SEND_OK);
                ListViewChatActivity.this.filePath = filePath;
                i++;
            }
        }).start();
    }

    /**
     * 发送语音
     */
//    float seconds = 0.0f;
//    String voiceFilePath = "";
    @Override
    public void onSendVoice(final ISoundMsg soundMsg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                msg_List.add(soundMsg);
                sendMessageHandler.sendEmptyMessage(SEND_OK);
                ListViewChatActivity.this.seconds = soundMsg.getUserVoiceTime();
                voiceFilePath = soundMsg.getSoundUrl();
                soundMsg.setType(ChatMsgViewAdapter.TO_USER_VOICE);
            }
        }).start();
    }


    /**
     * 接收文字
     */
    public void receriveMsgText(final IPhotoMsg msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ITextMsg txtmsg = ((ITextMsg) msg);
                String time = returnTime();
                txtmsg.setMsgDate(time);
                txtmsg.setType(ChatMsgViewAdapter.FROM_USER_MSG);
                sendMessageHandler.sendEmptyMessage(RECERIVE_OK);
            }
        }).start();
    }

    /**
     * 接收图片
     */

    String filePath = "";
    @Override
    public void onReceriveImageText(final IPhotoMsg msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                IPhotoMsg photoMsg = msg;
                String time = returnTime();
                photoMsg.setMsgDate(time);
                photoMsg.setPhotoLocal(photoMsg.getPhotoUrl());
                photoMsg.setType(ChatMsgViewAdapter.FROM_USER_IMG);
                msg_List.add(msg);
                imageList.add(msg_List.get(msg_List.size() - 1).getPhotoLocal());
                imagePosition.put(msg_List.size() - 1, imageList.size() - 1);
                sendMessageHandler.sendEmptyMessage(RECERIVE_OK);
                // mChatDbManager.insert(tbub);
            }
        }).start();
    }




//    /**
//     * 接收语音
//     */
    float seconds = 0.0f;
    String voiceFilePath = "";

    @Override
    public void onReceriveSoundText(final ISoundMsg soundMsg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                seconds = soundMsg.getUserVoiceTime();
                //soundMsg.setUserName(userName);
                String time = returnTime();
                soundMsg.setMsgDate(time);
                soundMsg.setUserVoiceTime(seconds);
                soundMsg.setSoundUrl(filePath);
                mAdapter.unReadPosition.add(msg_List.size() + "");
                soundMsg.setType(ChatMsgViewAdapter.FROM_USER_VOICE);
                msg_List.add(soundMsg);
                sendMessageHandler.sendEmptyMessage(RECERIVE_OK);
                //mChatDbManager.insert(tbub);
            }
        }).start();
    }

    /**
     * 为了模拟接收延迟
     */
//    private Handler receriveHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    receriveMsgText(content);
//                    break;
//                case 1:
//                    receriveImageText(filePath);
//                    break;
//                case 2:
//                    //receriveVoiceText(seconds, voiceFilePath);
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == FILE_REQUEST || requestCode == PHOTO_REQUEST_GALLERY) {//一般文件 //从相册选择的图片
            Uri uri = data.getData();
            String absolutePath = UriUtils.getPath(this, uri);
            //发送文件
            IBaseMsg.MsgType msgType = null;
            switch (requestCode) {
                case FILE_REQUEST:
                    msgType = IBaseMsg.MsgType.FILE;
                    break;
                case PHOTO_REQUEST_GALLERY:
                case PHOTO_REQUEST_CAREMA:
                    msgType = IBaseMsg.MsgType.PHOTO;
                    break;
            }
            if (msgType == null) {
                Log.w("msgType", "null");
                return;
            }
            chatPresenter.SendFile(dst, new File(absolutePath), msgType);
            Toast.makeText(this, absolutePath, Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == PHOTO_REQUEST_CAREMA) {// 从相机返回的图片
            Toast.makeText(this, Uri.fromFile(tempPhotoFile).getPath(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == VIDEO_REQUEST_CAREMA) {// 获取视频
            Toast.makeText(this, Uri.fromFile(tempVideoFile).getPath(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    static class SendMessageHandler extends Handler {
        WeakReference<ListViewChatActivity> mActivity;

        SendMessageHandler(ListViewChatActivity activity) {
            mActivity = new WeakReference<ListViewChatActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            ListViewChatActivity theActivity = mActivity.get();
            if (theActivity != null) {
                switch (msg.what) {
                    case REFRESH:
                        theActivity.mAdapter.isPicRefresh = true;
                        theActivity.mAdapter.notifyDataSetChanged();
                        theActivity.mListView.setSelection(theActivity.mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
                        //theActivity.myList.setSelection(theActivity.tblist
                        //        .size() - 1);
                        break;
                    case SEND_OK:
                        theActivity.mSendEdt.setText("");// 清空编辑框数据
                        theActivity.mAdapter.isPicRefresh = true;
                        theActivity.mAdapter.notifyDataSetChanged();
                        theActivity.mListView.setSelection(theActivity.mListView.getCount() - 1);
//                        theActivity.myList.setSelection(theActivity.tblist
//                                .size() - 1);
                        break;
                    case RECERIVE_OK:
                        theActivity.mAdapter.isPicRefresh = true;
                        theActivity.mAdapter.notifyDataSetChanged();
                        theActivity.mListView.setSelection(theActivity.mListView.getCount() - 1);
                        //theActivity.myList.setSelection(theActivity.tblist
                        //       .size() - 1);
                        break;
                    case PULL_TO_REFRESH_DOWN:
                        //theActivity.pullList.refreshComplete();
                        theActivity.mAdapter.notifyDataSetChanged();
                        theActivity.mListView.setSelection(theActivity.mListView.getCount() - 1);
                        // theActivity.myList.setSelection(theActivity.position - 1);
                        //theActivity.isDown = false;
                        break;
                    default:
                        break;
                }
            }
        }

    }


//    public void InitData() {
//        MsgModel lzy_1 = new MsgModel("lzy", "wzb", "2012-09-22 18:00:02", "有大吗", true);
//        DBManager.InsertMsg(lzy_1);
//
//        MsgModel wzb_1 = new MsgModel("lzy", "wzb", "2012-09-22 18:10:22", "有！你呢？", false);
//        DBManager.InsertMsg(wzb_1);
//
//        MsgModel lzy_2 = new MsgModel("lzy", "wzb", "2012-09-22 18:11:24", "我也有", true);
//        DBManager.InsertMsg(lzy_2);
//
//
//        MsgModel wzb_2 = new MsgModel("lzy", "wzb", "2012-09-22 18:20:23", "那上吧", false);
//        DBManager.InsertMsg(wzb_2);
//
//        MsgModel wzb_3 = new MsgModel("lz", "wzb", "2015-09-22 18:10:22", "傻逼", false);
//        DBManager.InsertMsg(wzb_3);
//    }


}