package com.cst.im.model;

/**
 * Created by wzb on 2017/5/8.
 */

public interface ISoundMsg extends IBaseMsg {
    //语音
    String getSoundUrl();
    void setSoundUrl(String soundUrl);
    float getUserVoiceTime();
    void setUserVoiceTime(float UserVoiceTime);

}
