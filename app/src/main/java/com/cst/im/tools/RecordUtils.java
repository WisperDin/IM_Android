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

    private static MediaRecorder mediaRecorder;
    // 系统的音频文件
    private static File soundFile;

    private enum RecordStatus {
        START_RECORD,
        STOP_RECORD
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
        return null;
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
            // 停止录制
            mediaRecorder.stop();
            flag = RecordStatus.STOP_RECORD;
            Log.d(TAG,"停止录音");
            return true;
        }
        return false;
    }

    /**
     * 播放本地录音
     */
    public static void playLocalAudio() {
        if (stopRecord()) {
            // 释放资源
            mediaRecorder.release();
            mediaRecorder = null;
            Log.d(TAG,"播放本地录音成功");
        }
    }

    /**
     * 播放接收到的录音
     */
    public static void playReceiveAudio(String path) {
        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(path);
            player.prepare();
            player.start();
            Log.d(TAG,"播放接收录音成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
