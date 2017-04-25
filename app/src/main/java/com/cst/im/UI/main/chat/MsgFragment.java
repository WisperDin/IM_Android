package com.cst.im.UI.main.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cst.im.R;

/**
 * Created by PolluxLee on 2017/4/25.
 */

public class MsgFragment extends Fragment {

    public static MsgFragment newInstance() {
        MsgFragment msgFragment = new MsgFragment();
        return msgFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        return view;
    }
}
