package com.cst.im.UI.main.friend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cst.im.R;
import com.cst.im.UI.main.chat.ChatActivity;
import com.cst.im.UI.main.msg.MsgFragment;
import com.cst.im.model.IBaseMsg;
import com.cst.im.model.MsgModelBase;
import com.cst.im.presenter.ChatListPresenter;

public class friendinformationActivity extends AppCompatActivity {

    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendinformation);
        //新页面接收数据
        bundle = this.getIntent().getExtras();
        //接收name值
        final String dstName = bundle.getString("dstName");
        final int dstId= bundle.getInt("dstId");
        TextView tvname=(TextView) findViewById(R.id.tv_friendinfo_name);
        TextView tvid=(TextView) findViewById(R.id.tv_friendinfo_id);
        Button bt_msg=(Button)findViewById(R.id.bt_msg);
        tvname.setText(dstName);
        tvid.setText( String.valueOf(dstId));
        //final IBaseMsg dst_User = new MsgModelBase(dstId , dstName);
        //向某个好友发消息
        bt_msg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent =new Intent(friendinformationActivity.this,ChatActivity.class);
                intent.putExtras(bundle);
                //通知消息列表添加消息
                MsgFragment.chatListPresenter.AddChatMsg(dstId ,dstName , "");
                startActivity(intent);
            }
        });
    }
}
