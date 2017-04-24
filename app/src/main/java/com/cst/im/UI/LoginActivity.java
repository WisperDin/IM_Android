package com.cst.im.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cst.im.R;
import com.cst.im.UI.main.MainActivity;
import com.cst.im.presenter.ILoginPresenter;
import com.cst.im.presenter.LoginPresenterCompl;
import com.cst.im.view.ILoginView;

public class LoginActivity extends AppCompatActivity implements ILoginView {
    private EditText editUser;
    private EditText editPwd;
    private Button btnLogin;


    private ImageView topPicture ;
    private EditText userName;
    private EditText passWord;
    private ImageView showPassword;
    private Button loginButton;
    private Button loginByMessage;
    private ImageView loginByQQ;
    private ImageView loginByWechat;
    private ImageView loginByWeibo;
    private TextView forgetPassword;
    private LinearLayout activityMain ;
    //登录业务逻辑
    ILoginPresenter loginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.cst.im.R.layout.activity_login);

        //init login presenter
        loginPresenter = new LoginPresenterCompl(this);

        //find Control
        topPicture = (ImageView) findViewById(R.id.top_picture);
        userName = (EditText) findViewById(R.id.username);
        passWord = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login_button);
        loginByMessage = (Button) findViewById(R.id.login_message);
        loginByQQ = (ImageView) findViewById(R.id.qq_login);
        loginByWechat = (ImageView) findViewById(R.id.wechat_login);
        loginByWeibo = (ImageView) findViewById(R.id.weibo_login);
        activityMain = (LinearLayout) findViewById(R.id.activity_main);
        //set btn listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.doLogin(userName.getText().toString(),passWord.getText().toString());
            }
        });

        HandleLoginAnimator();
    }
    //处理登录事件的UI提示
    @Override
    public void onLoginResult(Boolean result, int code){
        if (result){
            //页面跳转
            Intent it = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(it);
            LoginActivity.this.finish();
            Toast.makeText(this,"Login Success", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this,"Login Fail, code = " + code,Toast.LENGTH_SHORT).show();
    }
    //处理登录动画
    private void HandleLoginAnimator(){
        //显示动画
        final TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);

        //隐藏动画
        final TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        mHiddenAction.setDuration(1000);
        passWord.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
//                    topPicture.startAnimation(mHiddenAction);
                    topPicture.setVisibility(View.GONE);
                }
                else{
                    topPicture.startAnimation(mShowAction);
                    topPicture.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
