package com.cst.im.UI;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cst.im.R;
import com.cst.im.UI.main.MainActivity;
import com.cst.im.presenter.IRegisterPresenter;
import com.cst.im.presenter.RegisterPresenterCompl;
import com.cst.im.presenter.Status;
import com.cst.im.view.IRegisterView;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class RegisterActivity extends SwipeBackActivity implements IRegisterView, View.OnClickListener{

    private Button registerButton;
    private EditText username;
    private EditText password;

    private IRegisterPresenter registerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();

        registerPresenter = new RegisterPresenterCompl(this);

        registerButton.setOnClickListener(this);

        onEditTip();
    }

    private void initView(){
        registerButton = (Button) findViewById(R.id.register_button);
        username = (EditText) findViewById(R.id.register_username);
        password = (EditText) findViewById(R.id.register_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_button:
                registerPresenter.doRegister(username.getText().toString(),password.getText().toString());
        }
    }

    @Override
    public void onRegisterResult( int result) {
        if(result == Status.Register.REGISTER_SUCCESS){

            // 注册成功 1s 后返回登录界面
            Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    onBackPressed();
                }
            }, 1000);

        }else if(result == Status.Register.REGISTER_FAIL){

            Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public void onEditTip() {
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Drawable drawable = getResources().getDrawable(R.drawable.login_warning);
                drawable.setBounds(0,0,56,56);
                switch (registerPresenter.judgeUsername(username.getText().toString())){
                    case Status.Login.USERNAME_INVALID:
                        username.setError("不合法",drawable);
                        break;
                    case Status.Login.USERNAME_PHONE:
                        username.setError("手机号",drawable);
                        break;
                    case Status.Login.USERNAME_EMAIL:
                        username.setError("邮箱",drawable);
                        break;
                    case Status.Login.USERNAME_ACCOUNT:
                        username.setError("用户名",drawable);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Drawable drawable = getResources().getDrawable(R.drawable.login_warning);
                drawable.setBounds(0,0,56,56);
                if(registerPresenter.judgePassword(password.getText().toString())){
                    password.setError("√",drawable);
                }
                else {
                    password.setError("x",drawable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
