package com.cst.im.UI.main.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cst.im.R;

import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends Fragment {
    private final int flagUserInfo = 1;
    private final int flagSettting = 2;
    private List<Settings> settingsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        initSettings();

        UserSettingAdapter adapter = new UserSettingAdapter(SettingFragment.this.getActivity(),
                R.layout.settings_layout,settingsList);

        ListView listView = (ListView)view.findViewById(R.id.lv_setting);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Settings settings = settingsList.get(position);
                switch (settings.getIndex()){
                    case flagUserInfo:{

                        Toast.makeText(SettingFragment.this.getActivity(),settings.getName(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SettingFragment.this.getActivity(),UserInfoActivity.class);
                        startActivity(intent);
                    }break;
                    case flagSettting:{
                        Toast.makeText(SettingFragment.this.getActivity(),settings.getName(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SettingFragment.this.getActivity(),SettingDetailsActivity.class);
                        startActivity(intent);
                    }break;
                }

            }
        });

        return view;
    }



    private void initSettings(){
        Settings user = new Settings("用户信息",R.drawable.profile_icon,1);
        settingsList.add(user);
        Settings setting = new Settings("设置", R.drawable.setting_icon,2);
        settingsList.add(setting);
    }



}