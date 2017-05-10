package com.cst.im.tools;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by polluxlee on 2017/5/10.
 */

public class RecordUtils {

    private static final String TAG = "Record";

    // 录音对象
    public static MediaRecorder mediaRecorder;
    // 音频播放对象
    public static MediaPlayer player;

    // 系统的音频文件
    private static File soundFile;

    private enum RecordStatus {
        START_RECORD, //开始录音
        STOP_RECORD,  //停止录音
        PLAY_AUDIO,  //播放录音
        STOP_AUDIO   //停止播放
    }

    private static RecordStatus flag;

    public static boolean initRecord() {

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.e(TAG, "检测不到内存");
            return false;
        }
        // 创建保存录音的音频文件
        soundFile = new File(Environment.getExternalStorageDirectory()
                + "/mysound.amr");
        // 创建录制音频的对象
        mediaRecorder = new MediaRecorder();
        // 设置录音的声音来源，一般来自于麦克风
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置录制的声音输出格式（必须在设置声音编码格式之前设置）
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // 设置声音编码格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // 设置声音的输出路径
        mediaRecorder.setOutputFile(soundFile.getAbsolutePath());

        player = new MediaPlayer();

        return true;
    }

    /**
     * 获取音频文件绝对路径
     */
    public static String getAudioPath() {
        /** 录音文件存在且已经停止录音 */
        if (soundFile.exists()
                && soundFile != null
                && flag == RecordStatus.STOP_RECORD) {
            return soundFile.getAbsolutePath();
        }
        return "";
    }

    /**
     * 开始录音
     */
    public static void startRecord() {
        // 准备录音
        try {
            mediaRecorder.prepare();
            // 开始录音
            mediaRecorder.start();
            flag = RecordStatus.START_RECORD;
            Log.d(TAG,"开始录音");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录音
     */
    public static boolean stopRecord() {
        if (soundFile.exists() && soundFile != null) {
            // 停止录音
            mediaRecorder.stop();
            flag = RecordStatus.STOP_RECORD;
            Log.d(TAG,"停止录音");
            return true;
        }
        return false;
    }

    /**
     * 释放录音对象
     */
    public static void releaseRecorder() {
        if (flag == RecordStatus.STOP_RECORD) {
            // 释放资源
            mediaRecorder.release();
            mediaRecorder = null;
            Log.d(TAG,"释放录音对象成功");
        }
    }

    /**
     * 播放录音
     */
    public static void playAudio(String path) {
        try {
            player.setDataSource(path);
            player.prepare();
            player.start();
            Log.d(TAG,"播放录音成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止播放
     */
    public static void stopAudio(){
        try{
            player.stop();
            Log.d(TAG,"停止播放成功");
        } catch (Exception e){
            //仅当 player 没有初始化时才报异常
            e.printStackTrace();
        }
    }
}
