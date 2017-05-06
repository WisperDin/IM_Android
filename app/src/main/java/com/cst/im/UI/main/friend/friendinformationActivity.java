package com.cst.im.UI.main.friend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cst.im.R;
import com.cst.im.UI.main.chat.ChatActivity;

public class friendinformationActivity extends AppCompatActivity {

    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendinformation);
        //新页面接收数据
        bundle = this.getIntent().getExtras();
        //接收name值
        String name = bundle.getString("dstName");
        int id= bundle.getInt("dstId");
        TextView tvname=(TextView) findViewById(R.id.tv_friendinfo_name);
        TextView tvid=(TextView) findViewById(R.id.tv_friendinfo_id);
        Button bt_msg=(Button)findViewById(R.id.bt_msg);
        tvname.setText(name);
        tvid.setText( String.valueOf(id));
        bt_msg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent =new Intent(friendinformationActivity.this,ChatActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
