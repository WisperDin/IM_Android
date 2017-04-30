package com.cst.im.NetWork;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cst.im.NetWork.proto.DeEnCode;
import com.cst.im.model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cjwddz on 2017/4/23.
 */

public abstract class TcpService extends Service {
    static TcpClient client;
    @Override
    public void onCreate() {
        super.onCreate();
        //这里是设置服务器的ip地址和端口
        client = new TcpClient("192.168.191.1",6666);
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
            OutputStream outputStream = socket.getOutputStream();
            UserModel userToLogin = new UserModel("lzy","123");
            outputStream.write(DeEnCode.encodeLoginFrame(userToLogin));
            // 发送读取的数据到服务端
            outputStream.flush();
            //new Thread(new KeepHeartThread()).start();
            new Thread(new ReceiveThread()).start();
        }
        public void stop(){
            if(running){
                running=false;
                OnTcpStop();
            }

        }
        /*public void sendJSON(JSONObject obj) throws IOException {
            if(socket==null)
                return;
            OutputStream out=socket.getOutputStream();
            out.write(obj.toString().getBytes());
            out.flush();
        }*/
/*        public JSONObject getHeartWord() throws JSONException {
            JSONObject obj=new JSONObject();
            //// TODO: 2017/4/23 心跳的消息码为0
            obj.put("code",0);
            return obj;
        }*/
        public void destory() throws IOException {
            if(socket!=null)
                socket.close();
        }
        /*class KeepHeartThread implements Runnable {
            long checkDelay = 10;
            long keepAliveDelay = 2000;
            public void run() {
                while(client.running){
                    if(System.currentTimeMillis()-client.lastSendTime>keepAliveDelay){
                        try {
                            client.sendJSON(getHeartWord());
                        } catch (IOException e) {
                            e.printStackTrace();
                            client.stop();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        client.lastSendTime = System.currentTimeMillis();
                    }else{
                        try {
                            Thread.sleep(checkDelay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            client.stop();
                        }
                    }
                }
            }
        }*/
       class ReceiveThread implements Runnable {
            private ExecutorService msgPool = Executors.newCachedThreadPool();
            byte[] buffer=new byte[1024];
            StringBuffer stringBuffer;
            int sum=0;
            public void run() {
                while(client.running){
                    try {
                        //if(sum==0 || stringBuffer==null)
                            //stringBuffer=new StringBuffer();
                        InputStream in = socket.getInputStream();
                        if(in.available()>0){
                            //读字节流
                            //int count= in.read(buffer,0,1024);
                            //解码
                            protocol.Protocol.Frame frame =  DeEnCode.decodeFbFrame(in);

                            //stringBuffer.append(new String(buffer,0,count));

                            //sum+=count;
                            //if(count>=1024)
                            //    continue;
                            //if(sum>0){
                                //String data=stringBuffer.toString();
                                String data="";

                                final JSONObject msg=new JSONObject(data);
                                stringBuffer=new StringBuffer();
                                sum=0;
                                msgPool.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            OnMessageCome(msg);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                       // }
                       // else{
                        //    Thread.sleep(10);
                       // }
                    } catch (Exception e) {
                        e.printStackTrace();
                        TcpClient.this.stop();
                    }
                }
            }
        }
    }
    public abstract  void OnTcpStop();
    public abstract void OnMessageCome(JSONObject msg) throws JSONException;
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
