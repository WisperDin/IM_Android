package com.cst.im.model;

/**
 * Created by wzb on 2017/5/8.
 */

public class SoundMsgModel extends FileMsgModel implements ISoundMsg {
    String soundUrl;
    private float UserVoiceTime;
    @Override
    public String getSoundUrl() {
        return soundUrl;
    }

    @Override
    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }
    public float getUserVoiceTime() {
        return this.UserVoiceTime;
    }

    public void setUserVoiceTime(float UserVoiceTime) {
        this.UserVoiceTime = UserVoiceTime;
    }
}
