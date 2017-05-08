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

        switch(result){
            case Status.Register.REGISTER_SUCCESS:
                // 注册成功 1s 后返回登录界面
                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        onBackPressed();
                    }
                }, 1000);
                break;
            case Status.Register.REGISTER_FAIL:
                Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                break;
            case Status.Register.REGISTER_ALREADY:
                Toast.makeText(RegisterActivity.this,"已注册",Toast.LENGTH_SHORT).show();
                break;
        }

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
                Drawable drawableCorrect = getResources().getDrawable(R.drawable.correct);
                drawableCorrect.setBounds(0,0,60,60);
                Drawable drawableWarn = getResources().getDrawable(R.drawable.warning);
                drawableWarn.setBounds(0,0,56,56);
                switch (registerPresenter.judgeUsername(username.getText().toString())){
                    case Status.Login.USERNAME_INVALID:
                        username.setError("不合法",drawableWarn);
                        break;
                    case Status.Login.USERNAME_PHONE:
                        username.setError("合法手机号",drawableCorrect);
                        break;
                    case Status.Login.USERNAME_EMAIL:
                        username.setError("合法邮箱",drawableCorrect);
                        break;
                    case Status.Login.USERNAME_ACCOUNT:
                        username.setError("合法用户名",drawableCorrect);
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

                Drawable drawableCorrect = getResources().getDrawable(R.drawable.correct);
                drawableCorrect.setBounds(0,0,60,60);
                Drawable drawableWarn = getResources().getDrawable(R.drawable.warning);
                drawableWarn.setBounds(0,0,56,56);
                if(registerPresenter.judgePassword(password.getText().toString())){
                    password.setError("合法",drawableCorrect);
                }
                else {
                    password.setError("不合法",drawableWarn);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
