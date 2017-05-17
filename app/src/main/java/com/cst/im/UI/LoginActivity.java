package com.cst.im.UI;

import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cst.im.NetWork.ComService;
import com.cst.im.R;
import com.cst.im.UI.main.MainActivity;
import com.cst.im.presenter.ILoginPresenter;
import com.cst.im.presenter.LoginPresenterCompl;
import com.cst.im.presenter.Status;
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

    private ImageView ivLoginByQQ;
    private ImageView ivLoginByWechat;
    private ImageView ivLoginByWeibo;
    private TextView tvForgetPassword;


    private boolean isAccountValid;
    private boolean isPasswordValid;


    // 注册按钮
    TextView btnRegister;

    //登录业务逻辑
    ILoginPresenter loginPresenter;
    ComService comService;
    ServiceConnection serviceConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.cst.im.R.layout.activity_login);

        isAccountValid = false;
        isPasswordValid = false;

        initView();

        //init login presenter
        loginPresenter = new LoginPresenterCompl(this);
        mShowAction.setDuration(500);//动画运行时间

        btnLogin.setOnClickListener(this); // 登录按钮
        btnRegister.setOnClickListener(this); // 注册按钮
        editPwd.setOnFocusChangeListener(this); // 输入密码时图标消失
        onEditTip();

//网络接口测试部分
////////////////////////////////////////////////////////////////////
        /*
        serviceConn =new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                comService = ((ComService.MyBind)service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(new Intent(this, ComService.class), serviceConn, BIND_AUTO_CREATE);
        */

//////////////////////////////////////////////////////////////////////////////

    }

    // 初始化控件
    private void initView(){
        //find Control
        topPicture = (ImageView) findViewById(R.id.top_picture);
        editUser = (EditText) findViewById(R.id.username);
        editPwd = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.login_button);
//        ivLoginByQQ = (ImageView) findViewById(R.id.qq_login);
//        ivLoginByWechat = (ImageView) findViewById(R.id.wechat_login);
//        ivLoginByWeibo = (ImageView) findViewById(R.id.weibo_login);
        btnRegister = (TextView) findViewById(R.id.register_action);
    }

    //处理登录事件的UI提示
    @Override
    public void onLoginResult(final int rslCode,final int id){

        switch (rslCode) {
            case Status.Login.LOGINSUCCESS:
                loginPresenter.saveLoginInf();
                //页面跳转
                //TODO

                Intent it = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(it);
                LoginActivity.this.finish();
                Toast.makeText(this,"登录成功", Toast.LENGTH_SHORT).show();
                break;
            case Status.Login.LOGINFAILED:
                Toast.makeText(this,"登录失败",Toast.LENGTH_SHORT).show();
                break;
            case Status.Login.LOGINNOTEXIST:
                Toast.makeText(this,"该用户未注册",Toast.LENGTH_SHORT).show();
                break;
            case Status.Login.LOGINWRONGPWD:
                Toast.makeText(this,"密码错误",Toast.LENGTH_SHORT).show();
                break;
        }

    }

    //网络错误提示
    @Override
    public void onNetworkError() {
        Toast.makeText(this,"网络错误，无法连接服务器",Toast.LENGTH_LONG).show();
    }
    //输入时的提醒
    @Override
    public void onEditTip() {
        editUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Drawable drawableCorrect = getResources().getDrawable(R.drawable.correct);
                drawableCorrect.setBounds(0,0,60,60);
                Drawable drawableWarn = getResources().getDrawable(R.drawable.warning);
                drawableWarn.setBounds(0,0,56,56);

                switch (loginPresenter.judgeUsername(editUser.getText().toString())){
                    case Status.Login.USERNAME_INVALID:
                        editUser.setError("不合法",drawableWarn);
                        isAccountValid = false;
                        break;
                    case Status.Login.USERNAME_PHONE:
                        editUser.setError("合法手机号",drawableCorrect);
                        isAccountValid = true;
                        break;
                    case Status.Login.USERNAME_EMAIL:
                        editUser.setError("合法邮箱",drawableCorrect);
                        isAccountValid = true;
                        break;
                    case Status.Login.USERNAME_ACCOUNT:
                        editUser.setError("合法用户名",drawableCorrect);
                        isAccountValid = true;
                        break;
                    default:
                        editUser.setError("不合法",drawableWarn);
                        isAccountValid = false;
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Drawable drawableCorrect = getResources().getDrawable(R.drawable.correct);
                drawableCorrect.setBounds(0,0,60,60);
                Drawable drawableWarn = getResources().getDrawable(R.drawable.warning);
                drawableWarn.setBounds(0,0,56,56);


                if(loginPresenter.judgePassword(editPwd.getText().toString())){
                    editPwd.setError("合法",drawableCorrect);
                    isPasswordValid = true;
                }
                else {
                    editPwd.setError("不合法",drawableWarn);
                    isPasswordValid = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.login_button:

                if(!isAccountValid){
                    Toast.makeText(LoginActivity.this,"用户名不合法",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(!isPasswordValid){
                    Toast.makeText(LoginActivity.this,"密码不合法",Toast.LENGTH_SHORT).show();
                    break;
                }

                String user = editUser.getText().toString();
                String pwd = editPwd.getText().toString();
                //TODO: 暂时版本
                loginPresenter.doLogin(user,pwd);
                /*if(loginPresenter.canLogin(user,pwd)) //判断是否符合登录条件
                    loginPresenter.doLogin(editUser.getText().toString(),editPwd.getText().toString());
                else
                    Toast.makeText(this,"用户名或密码不规范", Toast.LENGTH_LONG).show();*/
                break;
            case R.id.register_action:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
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
