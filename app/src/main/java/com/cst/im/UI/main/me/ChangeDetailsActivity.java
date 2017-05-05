package com.cst.im.UI.main.me;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cst.im.R;

public class ChangeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_detail_layout);

        final EditText editText = (EditText)findViewById(R.id.detail_et);
        Button button = (Button)findViewById(R.id.save_bt);

        final Intent intent = getIntent();
        String value = intent.getStringExtra("value");
        final int indexofvalue = intent.getIntExtra("index",0);
        Log.d("THIS IS VALUE",value);
        editText.setText(value);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                if(indexofvalue == 1) {
                    String name = editText.getText().toString();
                    intent1.putExtra("return_name",name);
                    Log.d("RETURN_NAME", editText.getText().toString());
                    setResult(RESULT_OK, intent1);
                    Toast.makeText(ChangeDetailsActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                    finish();
                }
                if(indexofvalue ==2){
                    intent1.putExtra("return_address",editText.getText().toString());
                    Log.d("RETURN_ADDRESS",editText.getText().toString());
                    setResult(RESULT_OK,intent1);
                    Toast.makeText(ChangeDetailsActivity.this,"ADDRESS",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
