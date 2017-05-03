package com.cst.im.NetWork;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cst.im.NetWork.proto.DeEnCode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import protocol.Protocol.Frame;

/**
 * Created by cjwddz on 2017/4/23.
 */

public abstract class TcpService extends Service {
    public static TcpClient client;
    @Override
    public void onCreate() {
        super.onCreate();
        //这里是设置服务器的ip地址和端口
        client = new TcpClient("192.168.1.128",6666);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public class TcpClient {
        private String serverIp;
        private int port;
        private Socket socket;
        public boolean running=false;
        private long lastSendTime;
        public TcpClient(String serverIp, int port) {
            this.serverIp=serverIp;
            this.port=port;
        }
        public void start() throws IOException {
            if(running)
                return;
            running=true;
            socket = new Socket(serverIp,port);
            lastSendTime= System.currentTimeMillis();

            // 获取Socket的OutputStream对象用于发送数据。
            //OutputStream outputStream = socket.getOutputStream();
            //UserModel userToLogin = new UserModel("lzy","123");
            //outputStream.write(DeEnCode.encodeLoginFrame(userToLogin));
            // 发送读取的数据到服务端
            //outputStream.flush();
            //new Thread(new KeepHeartThread()).start();
            new Thread(new ReceiveThread()).start();
        }
        //发送消息
        public void SendData(byte[] data) throws IOException {
            if(socket==null)
                return;
            OutputStream out=socket.getOutputStream();
            out.write(data);
            out.flush();
        }

        public void stop(){
            if(running){
                running=false;
                OnTcpStop();
            }

        }
        public void destory() throws IOException {
            if(socket!=null)
                socket.close();
        }

       class ReceiveThread implements Runnable {
            private ExecutorService msgPool = Executors.newCachedThreadPool();
            byte[] buffer=new byte[1024];
            //StringBuffer stringBuffer;
            //int sum=0;
            public void run() {
                while(client.running){
                    try {

                        InputStream in = socket.getInputStream();
                        if(in.available()>0){
                            //读字节流
                            int count= in.read(buffer,0,1024);
                            System.out.print("Rec:  ");
                            for (int i= 0 ;i<count;i++){
                                System.out.print(buffer[i]+" ");
                            }
                            //复制有效字节到新的字节数组中
                            //TODO:以后要注意粘帧的情况
                            byte[] frameData = new byte[count];
                            System.arraycopy(buffer, 0, frameData, 0, count);
                            //解码
                            final Frame frame =  DeEnCode.decodeFrame(frameData);
                            //放到线程池执行
                            msgPool.execute(new Runnable() {
                                    @Override
                                    public void run() {OnMessageCome(frame);
                                    }
                                });
                            }
                        else{
                            Thread.sleep(10);
                       }
                    } catch (Exception e) {
                        e.printStackTrace();
                        TcpClient.this.stop();
                    }
                }
            }
        }
    }
    public abstract  void OnTcpStop();
    public abstract void OnMessageCome(Frame frame);
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(client!=null)
            try {
                client.destory();
            } catch (IOException e) {
                e.printStackTrace();
            }
        client=null;
    }
}
