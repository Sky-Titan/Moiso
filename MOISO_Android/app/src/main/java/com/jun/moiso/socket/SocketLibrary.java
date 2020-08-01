package com.jun.moiso.socket;

import android.content.Context;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketLibrary {

    private static SocketLibrary socketLibrary;
    private static final String TAG = "SocketLibrary";

    private boolean connect_result, disconnect_result, reconnect_result;
    private boolean send_result;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private int port ;
    private String ip = "";

    private Socket socket;

    private Context context;

    private SocketLibrary(){

    }

    public SocketLibrary getInstance()
    {
        if(socketLibrary != null)
            socketLibrary = new SocketLibrary();
        return socketLibrary;
    }

    //키보드 이벤트 송신
    public boolean sendKeyboardEvent(String key, String motion)
    {
        final String msg = key+ "/" + motion;
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                Log.i(TAG,"Send Keyboard Event");
                try
                {
                    outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject(msg);
                    outputStream.flush();

                    inputStream = new ObjectInputStream(socket.getInputStream());

                    //메시지 수신
                    Object msg_object = inputStream.readObject();
                    Log.i(TAG, "Send Keyboard Event ["+msg_object.toString()+"]");

                    //TODO : 전송 성공 메시지 혹은 실패 메시지
                    if(msg_object.toString().equals(""))
                        send_result = true;
                    else
                        send_result = false;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        return send_result;
    }

    //재연결
    public boolean reconnect(String ip, int port)
    {
        disconnect_result = disconnect();

        //연결해제 성공
        if(disconnect_result)
        {
            connect_result = connect(ip, port);

            //연결 성공
            if(connect_result)
                reconnect_result = true;
            else//연결 실패
                reconnect_result = false;
        }
        else// 연결 해제 실패
        {
            reconnect_result = false;
        }

        return reconnect_result;
    }

    //연결해제
    public boolean disconnect()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG,"disconnect");

                try {
                    socket = getSocket();

                    outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject("FINISH");
                    outputStream.flush();

                    inputStream = new ObjectInputStream(socket.getInputStream());
                    Object object = inputStream.readObject();

                    //서버 메시지 수신
                    Log.i(TAG, "Socket Disconnect ["+object.toString()+"]");

                    if (!socket.isClosed() && socket != null && object.toString().equals("OK"))
                        socket.close();

                    if(socket.isClosed())
                        disconnect_result = true;
                    else
                        disconnect_result = false;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        return disconnect_result;
    }

    //연결
    public boolean connect(final String ip, final int port)
    {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                Log.i(TAG,"Socket Connect try");
                try
                {
                    socket = getSocket(ip, port);//소켓 염
                    socket.setKeepAlive(true);//소켓 연결 알기 위해서

                    outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject("START");
                    outputStream.flush();

                    inputStream = new ObjectInputStream(socket.getInputStream());

                    //메시지 수신
                    Object msg_object = inputStream.readObject();
                    Log.i(TAG, "Socket Connect ["+msg_object.toString()+"]");

                    //TODO : 연결 성공 메시지 혹은 실패 메시지
                    if(msg_object.toString().equals(""))
                        connect_result = true;
                    else
                        connect_result = false;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        return connect_result;
    }

    private Socket getSocket(String ip, int port) throws  Exception
    {
        if(socket == null)
            socket = new Socket(ip, port);
        return socket;
    }

    private Socket getSocket() throws  Exception
    {
        if(socket == null)
            socket = new Socket();
        return socket;
    }
}
