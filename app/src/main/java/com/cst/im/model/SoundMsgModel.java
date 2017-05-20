package com.cst.im.model;

/**
 * Created by wzb on 2017/5/8.
 */

public class SoundMsgModel extends FileMsgModel implements ISoundMsg {
    private float UserVoiceTime;

    public float getUserVoiceTime() {
        return this.UserVoiceTime;
    }

    public void setUserVoiceTime(float UserVoiceTime) {
        this.UserVoiceTime = UserVoiceTime;
    }
}
