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
import com.cst.im.dataBase.Constant;
import com.cst.im.model.LoginUserModel;
import com.cst.im.model.UserModel;
import com.cst.im.presenter.IUserSettingPresenter;
import com.cst.im.presenter.Status;
import com.cst.im.presenter.UserSettingPresenterCompl;

public class ChangeDetailsActivity extends AppCompatActivity {
    IUserSettingPresenter userSettingPresenter = new UserSettingPresenterCompl(this);
    Intent returnIntent;
    UserModel tempuserModel;
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
                returnIntent = new Intent();
                tempuserModel = UserModel.localUser;
                if(index == UserInfoActivity.indexUserAge)//年龄
                {
                    int age = UserModel.localUser.getAge();
                    try{
                        age = Integer.parseInt(editText.getText().toString());
                    }catch (Exception e){
                        Toast.makeText(ChangeDetailsActivity.this,"年龄格式不正确",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(age <= 0 || age >=200)
                        return;
                    returnIntent.putExtra("return_age",age);
                    tempuserModel.setAge(age);
                }
                if(index == UserInfoActivity.indexUserRealName) { // 真实姓名
                    String name = editText.getText().toString();
                    returnIntent.putExtra("return_real_name",name);
                    tempuserModel.setUserRealName(name);
                }
                if(index == UserInfoActivity.indexUserPhone){ //手机号
                    if(LoginUserModel.checkTypeOfUsername(editText.getText().toString()) != Status.Login.USERNAME_PHONE){
                        Toast.makeText(ChangeDetailsActivity.this,"不是合法的手机号",Toast.LENGTH_LONG).show();
                        return;
                    }
                    returnIntent.putExtra("return_phone",editText.getText().toString());
                    Log.d("RETURN_ADDRESS",editText.getText().toString());
                    tempuserModel.setUserPhone(editText.getText().toString());
                }

                if(index == UserInfoActivity.indexUserEmail){
                    if(LoginUserModel.checkTypeOfUsername(editText.getText().toString()) != Status.Login.USERNAME_EMAIL){
                        Toast.makeText(ChangeDetailsActivity.this,"不是合法的邮箱",Toast.LENGTH_LONG).show();
                        return;
                    }
                    returnIntent.putExtra("return_email",editText.getText().toString());
                    tempuserModel.setUserEmail(editText.getText().toString());

                }

                if(index == UserInfoActivity.indexUserAddress){
                    returnIntent.putExtra("return_address",editText.getText().toString());
                    tempuserModel.setUserAddress(editText.getText().toString());

                }
                if(index == UserInfoActivity.indexUserSign){
                    returnIntent.putExtra("return_sign",editText.getText().toString());
                    tempuserModel.setUserSign(editText.getText().toString());
                }
                userSettingPresenter.pushRemoteUserInfo(tempuserModel);
            }
        });
    }

    public void onChangeSuccess(){//修改成功
        setResult(RESULT_OK,returnIntent);
        UserModel.localUser = tempuserModel;
        Toast.makeText(ChangeDetailsActivity.this,"修改成功",Toast.LENGTH_LONG).show();
        finish();
    }

    public void onChangeFail(){
        setResult(RESULT_CANCELED,returnIntent);
        Toast.makeText(ChangeDetailsActivity.this,"修改失败，请稍后再试",Toast.LENGTH_LONG).show();
        finish();
    }
    public void onNetWorkError(){
        Toast.makeText(this,"网络错误,无法修改数据",Toast.LENGTH_LONG).show();
    }
}
