package com.cst.im.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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
import com.cst.im.UI.main.chat.ChatActivity;
import com.cst.im.presenter.ILoginPresenter;
import com.cst.im.presenter.LoginPresenterCompl;
import com.cst.im.view.ILoginView;

public class LoginActivity extends AppCompatActivity implements ILoginView,View.OnClickListener,View.OnFocusChangeListener{
    //显示动画
    final TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

    private EditText editUser;
    private EditText editPwd;
    private Button btnLogin;
    private ImageView topPicture ;
    private ImageView showPassword;

    private Button btnLoginByMessage;
    private ImageView ivLoginByQQ;
    private ImageView ivLoginByWechat;
    private ImageView ivLoginByWeibo;
    private TextView tvForgetPassword;
    private LinearLayout activityMain ;
    private TextInputLayout tilUsername;
    private TextInputLayout tilPassword;
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
        editUser = (EditText) findViewById(R.id.username);
        editPwd = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.login_button);
        btnLoginByMessage = (Button) findViewById(R.id.login_message);
        ivLoginByQQ = (ImageView) findViewById(R.id.qq_login);
        ivLoginByWechat = (ImageView) findViewById(R.id.wechat_login);
        ivLoginByWeibo = (ImageView) findViewById(R.id.weibo_login);
        activityMain = (LinearLayout) findViewById(R.id.activity_main);
        tilUsername = (TextInputLayout) findViewById(R.id.usernameWrapper);
        tilPassword = (TextInputLayout) findViewById(R.id.passwordWrapper);


        mShowAction.setDuration(500);//动画运行时间

        btnLogin.setOnClickListener(this); // 登录按钮
        editPwd.setOnFocusChangeListener(this); // 输入密码时图标消失

    }
    //处理登录事件的UI提示
    @Override
    public void onLoginResult(Boolean result, int code){
        if (result){
            //页面跳转
            Intent it = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(it);
            LoginActivity.this.finish();
            Toast.makeText(this,R.string.success_login, Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this,"Login Fail, code = " + code,Toast.LENGTH_SHORT).show();
    }

    //网络错误提示
    @Override
    public void onNetworkError() {

    }
    //输入时的提醒
    @Override
    public void onEditTip() {

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login_button){
            loginPresenter.doLogin(editUser.getText().toString(),editPwd.getText().toString());
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            topPicture.setVisibility(View.GONE);
        }
        else{
            topPicture.startAnimation(mShowAction);
            topPicture.setVisibility(View.VISIBLE);
        }
    }


}
