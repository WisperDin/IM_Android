package com.cst.im.UI.main.chat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cst.im.R;
import com.cst.im.UI.main.chat.file.CallbackBundle;
import com.cst.im.UI.main.chat.file.OpenFileDialog;
import com.cst.im.dataBase.DBManager;
import com.cst.im.model.IMsg;
import com.cst.im.model.MsgModel;
import com.cst.im.presenter.ChatPresenter;
import com.cst.im.presenter.IChatPresenter;
import com.cst.im.view.IChatView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


public class ChatActivity extends SwipeBackActivity implements View.OnClickListener ,IChatView {
    private SQLiteOpenHelper helper;//从数据库获取历史消息
    private Button mBtnBack;// 返回btn
    private Button mBtnFile;//发送文件按钮
    private EditText mEditTextContent;//输入消息的栏
    private Button mBtnSend;//发送按钮
    private ListView mListView;//消息列表
    private ChatMsgViewAdapter mAdapter;// 消息视图的Adapter
    private TextView opposite_name;     //显示聊天对象名字
    //抽象出聊天的业务逻辑
    private IChatPresenter chatPresenter;

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



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        //by jijinping
        //获取接收者的名称
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据
        String acceptName=bundle.getString("Accept");//getString()返回指定key的值
        Toast.makeText(this, acceptName, Toast.LENGTH_LONG).show();


        //数据库的创建及调用
        helper = DBManager.getIntance(this);
        helper.getWritableDatabase();

        //InitData();//本地数据库测试

        //从数据库获取聊天数据
        List<IMsg> msg_list =  DBManager.QueryMsg("lzy");


        initView();// 初始化view

        //初始化数据（MVP）
        chatPresenter=new ChatPresenter(this , msg_list);
        mAdapter = new ChatMsgViewAdapter(this , msg_list);

        mListView.setAdapter(mAdapter);
        //消息列表选择到最后一行
        mListView.setSelection(mAdapter.getCount() - 1);

        /**
         * 监听EditText的回车事件
         * 发送消息
         */

        mEditTextContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //发送消息
                    chatPresenter.SendMsg(mEditTextContent.getText().toString());
                }
                return false;
            }
        });

    }
    //接收到消息就会执行
    @Override
    public void onRecvMsg(String msg,String date){
        mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
        mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
    }


    @Override
    public void onSendMsg(){
        mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
        mEditTextContent.setText("");// 清空编辑框数据
        mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
    }

    /**
     * 初始化view
     */
    public void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mBtnBack = (Button) findViewById(R.id.btn_back);
        mBtnSend = (Button)findViewById(R.id.btn_send);
        mBtnSend = (Button)findViewById(R.id.btn_send);
        mBtnFile=(Button) findViewById(R.id.btn_file);
        mBtnBack.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);

        mBtnSend.setOnClickListener(this);
        mBtnFile.setOnClickListener(this);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
        opposite_name = (TextView)findViewById(R.id.opposite_name);
        opposite_name.setText("聊天对象ID");

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

    static private int openfileDialogId = 0;
    //创建文件对话框
    @Override
    protected Dialog onCreateDialog(int id) {
        if(id==openfileDialogId){
            Map<String, Integer> images = new HashMap<String, Integer>();
            // 下面几句设置各文件类型的图标， 需要你先把图标添加到资源文件夹
            images.put(OpenFileDialog.sRoot, R.drawable.filedialog_root);   // 根目录图标
            images.put(OpenFileDialog.sParent, R.drawable.filedialog_folder_up);    //返回上一层的图标
            images.put(OpenFileDialog.sFolder, R.drawable.filedialog_folder);   //文件夹图标
            images.put("wav", R.drawable.filedialog_wavfile);   //wav文件图标
            images.put("txt", R.drawable.filedialog_wavfile);   //wav文件图标
            images.put(OpenFileDialog.sEmpty, R.drawable.filedialog_root);
            Dialog dialog = OpenFileDialog.createDialog(id, this, "打开文件", new CallbackBundle() {
                        @Override
                        public void callback(Bundle bundle) {
                            File file = new File(bundle.getString("path"));
                            //测试为1发到1
                            chatPresenter.SendFile(file,1,new int[]{1});
                            String filepath = bundle.getString("path");
                            setTitle(filepath); // 把文件路径显示在标题上
                        }
                    },
                    "",//.wav;
                    images);
            return dialog;
        }
        return null;
    }

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
      private void GetFile(){
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
        private void GetVideoFromCam(){
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
                finish();// 结束,实际开发中，可以返回主界面
                break;

            case R.id.btn_send://发送聊天信息
                Log.d("Send","Send____________________________________________________");
                chatPresenter.SendMsg(mEditTextContent.getText().toString());
                break;
            case R.id.btn_file://发送文件
                Log.d("Viewing","File----");
                //GetFile();
                //GetPhotoFromCamera();
                //showDialog(openfileDialogId);
                GetPhotoFromCamera();
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
            Toast.makeText(this,uri.getPath(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == PHOTO_REQUEST_GALLERY) {//从相册选择的图片
            Uri uri = data.getData();
            Toast.makeText(this,uri.getPath(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == PHOTO_REQUEST_CAREMA) {// 从相机返回的图片
            Toast.makeText(this,Uri.fromFile(tempPhotoFile).getPath(), Toast.LENGTH_SHORT).show();
            return;
        }
       if (requestCode == VIDEO_REQUEST_CAREMA) {// 获取视频
            Toast.makeText(this,Uri.fromFile(tempVideoFile).getPath(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void InitData(){
        MsgModel lzy_1 = new MsgModel("lzy" ,"wzb", "2012-09-22 18:00:02" , "有大吗" , true);
        DBManager.InsertMsg(lzy_1);

        MsgModel wzb_1 = new MsgModel("lzy" ,"wzb" , "2012-09-22 18:10:22" , "有！你呢？" , false);
        DBManager.InsertMsg(wzb_1);

        MsgModel lzy_2 = new MsgModel("lzy" ,"wzb" , "2012-09-22 18:11:24" , "我也有" , true);
        DBManager.InsertMsg(lzy_2);

        MsgModel wzb_2 = new MsgModel("lzy" ,"wzb" , "2012-09-22 18:20:23" , "那上吧" , false);
        DBManager.InsertMsg(wzb_2);

        MsgModel wzb_3 = new MsgModel("lz" ,"wzb" , "2015-09-22 18:10:22" , "傻逼" , false);
        DBManager.InsertMsg(wzb_3);


    }


}