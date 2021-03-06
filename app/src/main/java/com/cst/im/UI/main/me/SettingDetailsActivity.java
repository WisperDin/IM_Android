package com.cst.im.UI.main.me;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cst.im.R;
import com.cst.im.UI.main.MainActivity;
import com.cst.im.dataBase.DBManager;
import com.cst.im.model.UserModel;

import java.util.ArrayList;
import java.util.List;


public class SettingDetailsActivity extends AppCompatActivity {

    static final int USER_NAME_REQUEST = 1;
    static final int ADDRESS_REQUEST = 2;

    private List<SettingDetails> settingDetailList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_detail_parent_layout);

        initSettingDetail();

        final SettingDetailsAdapter adapter = new SettingDetailsAdapter(SettingDetailsActivity.this,
                R.layout.setting_detail_layout,settingDetailList);
        final ListView listView = (ListView)findViewById(R.id.setting_parent_lv);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                SettingDetails settingDetails = settingDetailList.get(position);
                switch (settingDetails.getIndex()){

                    case 3:{ // 退出到程序，并清空本地的登录信息
                        DBManager.deleteLoginUser(UserModel.localUser.getId());
                        Toast.makeText(SettingDetailsActivity.this,"退出成功，请重新登录",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SettingDetailsActivity.this, MainActivity.class);//利用跳转到MainActivity把栈里的Activity都删除
                        startActivity(intent);
                    }break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        SettingDetailsAdapter adapter = new SettingDetailsAdapter(SettingDetailsActivity.this,
                R.layout.setting_detail_layout,settingDetailList);

        ListView listView = (ListView)findViewById(R.id.setting_parent_lv);
        TextView username = (TextView) findViewById(R.id.setting_name);
        Log.d("USERNAME",username.getText().toString());
        TextView address = (TextView) findViewById(R.id.setting_value);
        Log.d("THIS IS THE FUNCTION","ON_ACTIVITY_RESUTL");
        Log.d("RESULT_CODE",String.valueOf(resultCode));
        Log.d("REQUESTCODE",String.valueOf(requestCode));
        switch (requestCode) {
            case USER_NAME_REQUEST: {
                if (resultCode == RESULT_OK) {
                    SettingDetails lv = settingDetailList.get(0);

                    String value = data.getStringExtra("return_name");
                    Log.d("VALUE", value);
                    lv.setValue(value);
                   // username.setText("DEBUG"+"");
                    listView.getChildAt(0);

                }
            }
            break;
            case ADDRESS_REQUEST: {
                if (resultCode == RESULT_OK) {
                    SettingDetails lv = settingDetailList.get(1);
                    String value = data.getStringExtra("return_address");
                    Log.d("VALUE", value);
                    lv.setValue(value);
                    //address.setText(value);

                }
            }
            break;
        }
    }
    private void initSettingDetail(){
        settingDetailList.add(new SettingDetails("消息提醒","",1));
        settingDetailList.add(new SettingDetails("关于","",2));
        settingDetailList.add(new SettingDetails("退出登录","",3));
    }
}
