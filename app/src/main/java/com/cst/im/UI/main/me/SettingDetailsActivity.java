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
                    case 1: {
                        Intent usernameintent = new Intent(SettingDetailsActivity.this, ChangeDetailsActivity.class);
                        usernameintent.putExtra("value", settingDetails.getValue());
                        usernameintent.putExtra("index", 1);
                        startActivityForResult(usernameintent, USER_NAME_REQUEST);
                        Toast.makeText(SettingDetailsActivity.this, settingDetails.getName(), Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    }break;
                    case 2: {
                        Intent addressintent = new Intent(SettingDetailsActivity.this, ChangeDetailsActivity.class);
                        addressintent.putExtra("value", settingDetails.getValue());
                        addressintent.putExtra("index", 2);
                        startActivityForResult(addressintent, ADDRESS_REQUEST);
                        Toast.makeText(SettingDetailsActivity.this, settingDetails.getName(), Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
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
        TextView username = (TextView) findViewById(R.id.setting_value);
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
        SettingDetails nickname = new SettingDetails("用户名","用户1",1);
        settingDetailList.add(nickname);
        SettingDetails address = new SettingDetails("地址","",2);
        settingDetailList.add(address);
    }
}
