package com.jun.moiso.socket;

import android.content.Context;
import android.util.Log;

import com.jun.moiso.interfaces.SocketCallback;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

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

    private final int TIME_OUT = 3000;

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
    public void sendMouseWheelEvent(String direction, String number)
    {
        final String msg = "MOUSE=WHEEL&"+direction+"*"+number;
        sendToSeverMsg(msg);
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

    //서버로 메시지 송신
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

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    //재연결
    public void reconnect(String ip, int port, String group_name, String user_name, SocketCallback reconnectCallback)
    {
        //TODO : 재연결 (연결해제 -> 연결)

    }

    //연결해제
    public void disconnect(final SocketCallback disconnectCallBack)
    {
        disconnect_result = false;
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

                    socket.close();
                    socket = null;

                    if(socket == null || socket.isClosed())
                        disconnect_result = true;
                    else
                        disconnect_result = false;

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    socket = null;
                    disconnect_result = false;
                }

                disconnectCallBack.callback(disconnect_result);
            }
        });
        thread.start();

    }

    //연결
    public void connect(final String ip, final int port, final String group_name, final String user_name, final SocketCallback connectCallBack)
    {
        connect_result = false;

        final SocketAddress socketAddress = new InetSocketAddress(ip,port);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                Log.i(TAG,"Socket Connect try");
                try
                {
                    socket = getSocket();//소켓 염

                    socket.connect(socketAddress, TIME_OUT);
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


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    socket = null;
                    connect_result = false;

                }
                connectCallBack.callback(connect_result);
            }
        });
        thread.start();


    }

    //서버 측에서 먼저 socket 연결을 종료할 시 -> 메시지를 받아서 소켓 연결 및 ControlActivity 종료
    public void waitSocketClose(final SocketCallback socketCallback)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isFinish = false;
                try
                {
                    while (!isFinish)
                    {
                        inputStream = new ObjectInputStream(socket.getInputStream());

                        //메시지 수신
                        Object msg_object = inputStream.readObject();
                        if(msg_object.toString().equals("CONNECT_FINISH"))
                        {
                            socket.close();
                            socket = null;
                            isFinish = true;
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    socket = null;
                }
                socketCallback.callback(isFinish);
            }
        });
        thread.start();
    }

    private Socket getSocket(String ip, int port) throws  Exception
    {
        if(socket == null || socket.isClosed())
            socket = new Socket(ip, port);

        return socket;
    }

    public Socket getSocket()
    {
        try
        {
            if(socket == null || socket.isClosed())
                socket = new Socket();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return socket;
    }


}
