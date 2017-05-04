package com.cst.im.UI.main.chat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ChatActivity extends Activity implements View.OnClickListener ,IChatView {
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


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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
        mBtnFile=(Button) findViewById(R.id.btn_file);
        mBtnBack.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        mBtnFile.setOnClickListener(this);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
        opposite_name = (TextView)findViewById(R.id.opposite_name);
        opposite_name.setText("聊天对象ID");

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

                            chatPresenter.SendFile(file,1,new int[]{2});
                            //String filepath = bundle.getString("path");
                            //setTitle(filepath); // 把文件路径显示在标题上
                        }
                    },
                    "",//.wav;
                    images);
            return dialog;
        }
        return null;
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
                showDialog(openfileDialogId);
                break;
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