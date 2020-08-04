import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.StringTokenizer;

public class SocketThread extends Thread {

    private Socket sock;

    private Robot robot;

    private Setting setting;

    //연결된 사용자 이름
    private String user_name;

    //Setting에서 받은 callback 객체
    private Callback callback;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;


    public SocketThread(Socket sock, Setting setting, Callback callback) {

        this.sock = sock;
        this.setting = setting;
        this.callback = callback;
    }

    @Override
    public void run() {
        try
        {
            robot = new Robot();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        connectStart();
    }

    public Socket getSock() {
        return sock;
    }

    private void connectStart()
    {
        try
        {
            java.lang.Object obj = null;

            try
            {
                //클라이언트 메시지 수신
                inputStream = new ObjectInputStream(sock.getInputStream());
                obj = inputStream.readObject();
            }
            catch(Exception e)
            {
                if(!sock.isClosed())
                    sock.close();
                System.out.println("[Exception] input receive FAIL");
            }

            //수신 메시지 String 변환
            String receive = String.valueOf(obj);


            StringTokenizer strtok = new StringTokenizer(receive, "&");

            String key = strtok.nextToken();
            String group_name = strtok.nextToken();
            user_name = strtok.nextToken();

            System.out.println("Thread ["+user_name+"] receive : "+receive);

            //화면에 그룹 이름 적용
            setting.group_name2.setText(group_name);
            //화면에 연결 사용자 추가
            memberCountUp();

            //연결 시작
            if(key.equals("START"))
            {
                setting.receive_info2.setText(key);

                sendResponse("CONNECT_COMPLETE");

                //입력 대기
                while(sock!=null)
                {
                    if(sock.isClosed() || !sock.isConnected())
                        break;

                    inputStream = new ObjectInputStream(sock.getInputStream());
                    obj = inputStream.readObject();

                    receive = String.valueOf(obj);
                    strtok = new StringTokenizer(receive, "=");

                    //입력 종류
                    String input_type = strtok.nextToken();
                    String parse_str = "";

                    //마우스
                    if(input_type.equals("MOUSE"))
                    {
                        parse_str = strtok.nextToken();
                        inputMouse(parse_str);
                    }
                    //키보드
                    else if(input_type.equals("KEYBOARD"))
                    {
                        parse_str = strtok.nextToken();
                        inputKeyboard(parse_str);
                    }
                    //종료
                    else
                    {
                        disconnect();
                        break;
                    }

                    String msg = input_type + "_PROCESS_COMPLETE";
                    setting.receive_info2.setText(msg);

                    sendResponse(msg);
                }
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.err.println("Connect Exception");

            socketClose();
            memberCountDown();
            callback.removeCallback(this);
        }
    }

    //연결 해제
    public void disconnect()
    {
        sendResponse("CONNECT_FINISH");
        socketClose();
        memberCountDown();
        callback.removeCallback(this);
    }

    //소켓 닫기
    private void socketClose()
    {
        try
        {
            sock.close();
            sock = null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Socket Close Exception");
        }
    }

    //member 수 늘임
    private void memberCountUp()
    {
        setting.people2.setText(Integer.parseInt(setting.people2.getText())+1+"");
    }

    //member 수 줄임
    private void memberCountDown()
    {
        setting.people2.setText(Integer.parseInt(setting.people2.getText())-1+"");
    }


    //응답 송신
    private void sendResponse(String msg)
    {
        try
        {
            outputStream = new ObjectOutputStream(sock.getOutputStream());
            outputStream.writeObject(msg);
            outputStream.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Send Response Exception");
        }
    }

    //키보드 인풋 처리
    private void inputKeyboard(String str)
    {
        StringTokenizer strtok = new StringTokenizer(str,"&");
        int key_code = Integer.parseInt(strtok.nextToken());//키코드
        String movement = strtok.nextToken();//PRESS, RELEASE

        if(movement.equals("PRESS"))
        {
            robot.keyPress(key_code);
            System.out.println(KeyEvent.getKeyText(key_code)+" PRESS");
        }
        else
        {
            robot.keyRelease(key_code);
            System.out.println(KeyEvent.getKeyText(key_code)+" RELEASE");
        }
    }

    //마우스 인풋 처리
    private void inputMouse(String str)
    {
        StringTokenizer strtok = new StringTokenizer(str,"&");
        String motion = strtok.nextToken();//모션 ex) DRAG, BUTTON, WHEEL
        String key = strtok.nextToken();//키 ex) x/y, LEFT, WHEEL, RIGHT ....

        if(motion.equals("DRAG"))//마우스 커서 드래그
        {
            StringTokenizer sense_tok = new StringTokenizer(key,"*");
            String pos = sense_tok.nextToken();
            int sensitivity = Integer.parseInt(sense_tok.nextToken());//감도

            StringTokenizer pos_tok = new StringTokenizer(pos,"/");
            int x = Integer.parseInt(pos_tok.nextToken());//위치 x
            int y = Integer.parseInt(pos_tok.nextToken());//위치 y

            PointerInfo pointerInfo = MouseInfo.getPointerInfo();

            robot.mouseMove(x * sensitivity + (int)pointerInfo.getLocation().getX(), y * sensitivity + (int)pointerInfo.getLocation().getY());
        }
        else if(motion.equals("BUTTON"))//마우스 버튼 클릭
        {
            StringTokenizer direction_tok = new StringTokenizer(key,"/");
            String direction = direction_tok.nextToken();//LEFT, WHEEL, RIGHT
            String movement = direction_tok.nextToken();//PRESS, RELEASE

            if(direction.equals("LEFT"))//왼쪽 버튼
            {
                if(movement.equals("PRESS"))
                {
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                }
                else//RELEASE
                {
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                }
            }
            else if(direction.equals("WHEEL"))//휠 버튼
            {
                if(movement.equals("PRESS"))
                {
                    robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
                }
                else//RELEASE
                {
                    robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
                }
            }
            else//오른쪽 버튼
            {
                if(movement.equals("PRESS"))
                {
                    robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                }
                else//RELEASE
                {
                    robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                }
            }
        }
        else//휠
        {
            StringTokenizer sense_tok = new StringTokenizer(key,"*");
            String direction = sense_tok.nextToken();
            int sensitivity = Integer.parseInt(sense_tok.nextToken());

            //위
            if(direction.equals("UP"))
            {
                robot.mouseWheel(-1 * sensitivity); //1은 기본 이동값
            }
            else if(direction.equals("BAR"))//바 드래그
            {
                int move = sensitivity;
                robot.mouseWheel(move);
            }
            else
            {
                robot.mouseWheel(1 * sensitivity); //1은 기본 이동값
            }
        }
    }
/*
    private String getLocalServerIp()
    {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress())
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (SocketException ex) {}
        return null;
    }
    */
}
