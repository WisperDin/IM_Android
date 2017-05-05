package com.cst.im.UI.main.me;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cst.im.R;

import java.util.List;

/**
 * Created by HuangYiXin on 2017/5/2.
 */

public class SettingDetailsAdapter extends ArrayAdapter<SettingDetails> {

    private int resourceId;

    public SettingDetailsAdapter(Context context, int textViewResourceId,
                                 List<SettingDetails>objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SettingDetails settingDetails = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView name = (TextView)view.findViewById(R.id.setting_name);
        TextView value = (TextView)view.findViewById(R.id.setting_value);
        name.setText(settingDetails.getName());
        value.setText(settingDetails.getValue());
        return view;
    }
}
