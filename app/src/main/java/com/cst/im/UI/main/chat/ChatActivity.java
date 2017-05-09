package com.cst.im.UI.main.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
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
import com.cst.im.model.UserModel;
import com.cst.im.presenter.ChatPresenter;
import com.cst.im.presenter.IChatPresenter;
import com.cst.im.view.IChatView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


public class ChatActivity extends SwipeBackActivity implements View.OnClickListener, IChatView {
    private SQLiteOpenHelper helper;//从数据库获取历史消息
    private Button mBtnBack;// 返回btn
    private TextView mSendBtn;//发送按钮
    private ListView mListView;//消息列表
    private ChatMsgViewAdapter mAdapter;// 消息视图的Adapter
    private TextView opposite_name;     //显示聊天对象名字
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
        List<IBaseMsg> msg_list = DBManager.QueryMsg(dstUser.getId());


        initView(bundle.getString("dstName"));// 初始化view

        //初始化数据（MVP）
        chatPresenter = new ChatPresenter(this, msg_list);
        mAdapter = new ChatMsgViewAdapter(this, msg_list);

        mListView.setAdapter(mAdapter);
        //消息列表选择到最后一行
        mListView.setSelection(mAdapter.getCount() - 1);

        // 监控键盘与面板高度
        doAttach();
        // 监听加号按钮以及输入框内容变化
        setListener();

    }

    // 监控键盘与面板高度
    private void doAttach(){
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

        mBtnBack.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);
        mFileBtn.setOnClickListener(this);
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
        mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
        mSendEdt.setText("");// 清空编辑框数据
        mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
    }

    /**
     * 初始化view
     */
    public void initView(String dst_Name) {
        mListView = (ListView) findViewById(R.id.content_list);
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

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && ((FaceRelativeLayout) findViewById(R.id.FaceRelativeLayout))
                .hideFaceView()) {
            return true;
        }
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
                chatPresenter.SendMsg(dst, mSendEdt.getText().toString());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == FILE_REQUEST) {//一般文件
            Uri uri = data.getData();
            //发送文件
            //TODO 要注意这个路径是否uri路径
            chatPresenter.SendFile(dst,new File(uri.getPath()));
            Toast.makeText(this, uri.getPath(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == PHOTO_REQUEST_GALLERY) {//从相册选择的图片
            Uri uri = data.getData();
            //解析路径
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String path = actualimagecursor.getString(actual_image_column_index);// 获取选择文件
            //发送图片文件
            chatPresenter.SendFile(dst,new File(path));
            Toast.makeText(this, uri.getPath(), Toast.LENGTH_SHORT).show();
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