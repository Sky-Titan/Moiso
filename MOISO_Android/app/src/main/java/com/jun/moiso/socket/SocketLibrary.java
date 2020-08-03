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

    private boolean isFinish = false;

    private Socket socket;

    private Context context;

    private SocketLibrary(){

    }

    public static SocketLibrary getInstance()
    {
        if(socketLibrary == null)
            socketLibrary = new SocketLibrary();
        return socketLibrary;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    //마우스 버튼 이벤트 송신
    public void sendMouseButtonEvent(String direction, String movement)
    {
        final String msg = "MOUSE=BUTTON&"+direction+"/"+movement;
        sendToSeverMsg(msg);
    }

    //마우스 드래그 이벤트 송신
    public void sendMouseDragEvent(int x, int y, int sensetivity)
    {
        final String msg = "MOUSE=DRAG&"+x+"/"+y+"*"+sensetivity;
        sendToSeverMsg(msg);
    }

    //키보드 이벤트 송신
    public void sendKeyboardEvent(int key_code, String motion)
    {
        //isFinish = false;
        final String msg = "KEYBOARD="+key_code+ "&" + motion;
        sendToSeverMsg(msg);
    }

    private void sendToSeverMsg(final String msg)
    {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

               // Log.i(TAG,"Send Event "+msg);
                try
                {
                    outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject(msg);
                    outputStream.flush();

                    inputStream = new ObjectInputStream(socket.getInputStream());

                    //메시지 수신
                    Object msg_object = inputStream.readObject();
                  //  Log.i(TAG, "Send Event ["+msg_object.toString()+"]");

                    //TODO : 전송 성공 메시지 혹은 실패 메시지
                  /*  if(msg_object.toString().equals("KEYBOARD_PROCESS_COMPLETE"))
                        send_result = true;
                    else
                        send_result = false;

                    isFinish = true;
               */ }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    //재연결
    public boolean reconnect(String ip, int port, String group_name, String user_name)
    {
        disconnect_result = disconnect();

        //연결해제 성공
        if(disconnect_result)
        {
            connect_result = connect(ip, port, group_name, user_name);

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
    public boolean connect(final String ip, final int port, final String group_name, final String user_name)
    {
        isFinish = false;
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                Log.i(TAG,"Socket Connect try");
                try
                {
                    socket = getSocket(ip, port);//소켓 염
                    socket.setKeepAlive(true);//소켓 연결 알기 위해서

                    outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject("START&"+group_name+"&"+user_name);
                    outputStream.flush();

                    inputStream = new ObjectInputStream(socket.getInputStream());

                    //메시지 수신
                    Object msg_object = inputStream.readObject();
                    Log.i(TAG, "Socket Connect ["+msg_object.toString()+"]");

                    //TODO : 연결 성공 메시지 혹은 실패 메시지
                    if(msg_object.toString().equals("CONNECT_COMPLETE"))
                        connect_result = true;
                    else
                        connect_result = false;

                    isFinish = true;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        //작업 스레드 종료시까지 대기기
        while(!isFinish);

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
