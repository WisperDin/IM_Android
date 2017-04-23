package com.cst.im.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cst.im.UI.main.chat.ChatActivity;
import com.cst.im.presenter.ILoginPresenter;
import com.cst.im.presenter.LoginPresenterCompl;
import com.cst.im.view.ILoginView;

public class LoginActivity extends AppCompatActivity implements ILoginView {
    private EditText editUser;
    private EditText editPwd;
    private Button btnLogin;

    //登录业务逻辑
    ILoginPresenter loginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.cst.im.R.layout.activity_login);

        //init login presenter
        loginPresenter = new LoginPresenterCompl(this);

        //find Control
        editUser=(EditText) findViewById(com.cst.im.R.id.login_account);
        editPwd=(EditText) findViewById(com.cst.im.R.id.login_password);
        btnLogin=(Button) findViewById(com.cst.im.R.id.login_button);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.doLogin(editUser.getText().toString(),editPwd.getText().toString());
            }
        });
    }
    //处理登录事件
    @Override
    public void onLoginResult(Boolean result, int code){
        if (result){
            //页面跳转
            Intent it = new Intent(LoginActivity.this, ChatActivity.class);
            startActivity(it);
            Toast.makeText(this,"Login Success", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this,"Login Fail, code = " + code,Toast.LENGTH_SHORT).show();
    }
}
