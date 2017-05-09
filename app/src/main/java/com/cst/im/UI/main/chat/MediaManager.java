package com.cst.im.UI.main.chat;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by wzb on 2017/5/9.
 */

public class MediaManager {
    private static MediaPlayer mPlayer;

    private static boolean isPause;

    public static  void playSound(String filePathString,
                                  MediaPlayer.OnCompletionListener onCompletionListener) {
        // TODO Auto-generated method stub
        if (mPlayer==null) {
            mPlayer=new MediaPlayer();
            //保险起见，设置报错监听
            mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // TODO Auto-generated method stub
                    mPlayer.reset();
                    return false;
                }
            });
        }else {
            mPlayer.reset();//就恢复
        }

        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnCompletionListener(onCompletionListener);
            mPlayer.setDataSource(filePathString);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //停止函数
    public static void pause(){
        if (mPlayer!=null&&mPlayer.isPlaying()) {
            mPlayer.pause();
            isPause=true;
        }
    }

    //继续
    public static void resume()
    {
        if (mPlayer!=null&&isPause) {
            mPlayer.start();
            isPause=false;
        }
    }


    public  static void release()
    {
        if (mPlayer!=null) {
            mPlayer.release();
            mPlayer=null;
        }
    }
}
