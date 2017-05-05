package com.cst.im.UI;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cst.im.R;
import com.cst.im.UI.main.MainActivity;
import com.cst.im.dataBase.DBManager;
import com.cst.im.dataBase.DatabaseHelper;
import com.cst.im.model.ILoginUser;
import com.cst.im.model.LoginUserModel;
import com.cst.im.presenter.ILoginPresenter;

public class SplashActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    ILoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        databaseHelper = DBManager.getIntance(this);
        databaseHelper.getWritableDatabase();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ILoginUser loginUser = DBManager.queryLoginUser();


                if(loginUser.getId().equals("")){
                    Log.d("Splash","need Login");
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Log.d("Splash","don't need Login");
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1500);
    }
}
