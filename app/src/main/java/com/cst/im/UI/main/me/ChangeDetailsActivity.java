package com.cst.im.UI.main.me;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cst.im.R;
import com.cst.im.model.LoginUserModel;
import com.cst.im.presenter.Status;

public class ChangeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_detail_layout);

        final EditText editText = (EditText)findViewById(R.id.detail_et);
        Button button = (Button)findViewById(R.id.save_bt);
        TextInputLayout til = (TextInputLayout)findViewById(R.id.detailWrapper);
        final Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        String value = intent.getStringExtra("value");
        final int index = intent.getIntExtra("index",0);
        Log.d("THIS IS KEY",key);
        Log.d("THIS IS VALUE",value);
        til.setHint(key);
        editText.setText(value)
        ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                if(index == UserInfoActivity.indexUserRealName) { // 真实姓名
                    String name = editText.getText().toString();
                    returnIntent.putExtra("return_real_name",name);
                    Log.d("RETURN_REAL_NAME", editText.getText().toString());
                    setResult(RESULT_OK, returnIntent);
                    Toast.makeText(ChangeDetailsActivity.this, "修改姓名成功", Toast.LENGTH_LONG).show();
                    finish();
                }
                if(index == UserInfoActivity.indexUserPhone){ //手机号
                    if(LoginUserModel.checkTypeOfUsername(editText.getText().toString()) != Status.Login.USERNAME_PHONE){
                        Toast.makeText(ChangeDetailsActivity.this,"不是合法的手机号",Toast.LENGTH_LONG).show();
                        return;
                    }
                    returnIntent.putExtra("return_phone",editText.getText().toString());
                    Log.d("RETURN_ADDRESS",editText.getText().toString());
                    setResult(RESULT_OK,returnIntent);
                    Toast.makeText(ChangeDetailsActivity.this,"修改手机号成功",Toast.LENGTH_SHORT).show();
                    finish();
                }

                if(index == UserInfoActivity.indexUserEmail){
                    if(LoginUserModel.checkTypeOfUsername(editText.getText().toString()) != Status.Login.USERNAME_EMAIL){
                        Toast.makeText(ChangeDetailsActivity.this,"不是合法的邮箱",Toast.LENGTH_LONG).show();
                        return;
                    }
                    returnIntent.putExtra("return_email",editText.getText().toString());
                    setResult(RESULT_OK,returnIntent);
                    Toast.makeText(ChangeDetailsActivity.this,"修改邮箱成功",Toast.LENGTH_LONG).show();
                    finish();
                }

                if(index == UserInfoActivity.indexUserAddress){
                    returnIntent.putExtra("return_address",editText.getText().toString());
                    setResult(RESULT_OK,returnIntent);
                    Toast.makeText(ChangeDetailsActivity.this,"修改地址成功",Toast.LENGTH_LONG).show();
                    finish();
                }
                if(index == UserInfoActivity.indexUserSign){
                    returnIntent.putExtra("return_sign",editText.getText().toString());
                    setResult(RESULT_OK,returnIntent);
                    Toast.makeText(ChangeDetailsActivity.this,"修改个性签名成功",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
}
