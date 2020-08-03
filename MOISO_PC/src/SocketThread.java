import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Enumeration;
import java.util.StringTokenizer;

public class SocketThread extends Thread {

    private Socket sock;
    private ServerSocket serverSocket;
    private Robot robot;

    private Setting setting;

    //연결된 사용자 이름
    private String user_name;

    private int SOCKET_NUMBER;
    private int port;

    public SocketThread() {
    }

    public SocketThread(int SOCKET_NUMBER) {
        this.SOCKET_NUMBER = SOCKET_NUMBER;
    }

    public SocketThread(int SOCKET_NUMBER, Socket sock) {
        this.SOCKET_NUMBER = SOCKET_NUMBER;
        this.sock = sock;
    }

    public SocketThread(int SOCKET_NUMBER, Setting setting) {
        this.SOCKET_NUMBER = SOCKET_NUMBER;
        this.setting = setting;
    }

    public SocketThread(int SOCKET_NUMBER, Socket sock, Setting setting) {
        this.SOCKET_NUMBER = SOCKET_NUMBER;
        this.sock = sock;
        this.setting = setting;
    }

    @Override
    public void run() {
        try{
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

            ObjectInputStream inputStream;
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
                System.out.println("exception 발생");
            }

            //수신 메시지 String 변환
            String receive = String.valueOf(obj);
            System.out.println("Thread ["+SOCKET_NUMBER+"] receive : "+receive);

            StringTokenizer strtok = new StringTokenizer(receive, "&");

            String key = strtok.nextToken();
            String group_name = strtok.nextToken();
            user_name = strtok.nextToken();

            setting.group_name2.setText(group_name);
            setting.people2.setText(setting.people2.getText()+" "+user_name);


            if(key.equals("START"))
            {
                setting.receive_info2.setText(key);

                ObjectOutputStream outputStream = new ObjectOutputStream(sock.getOutputStream());
                outputStream.writeObject("CONNECT_COMPLETE");
                outputStream.flush();

                //입력 대기
                while(true)
                {
                    if(sock.isClosed() || !sock.isConnected())
                        break;
                    inputStream = new ObjectInputStream(sock.getInputStream());
                    obj = inputStream.readObject();

                    receive = String.valueOf(obj);
                    strtok = new StringTokenizer(receive, "=");

                    //System.out.println("receive : "+receive);

                    //입력 종류
                    String input_type = strtok.nextToken();
                    String parse_str = strtok.nextToken();

                    //마우스
                    if(input_type.equals("MOUSE"))
                    {
                        inputMouse(parse_str);

                    }
                    //키보드
                    else if(input_type.equals("KEYBOARD"))
                    {
                        inputKeyboard(parse_str);
                    }
                    //종료
                    else
                    {
                        outputStream = new ObjectOutputStream(sock.getOutputStream());
                        outputStream.writeObject("CONNECT_FINISH");
                        outputStream.flush();
                        sock.close();
                        break;
                    }

                    setting.receive_info2.setText(input_type+"_PROCESS_COMPLETE");
                    outputStream = new ObjectOutputStream(sock.getOutputStream());
                    outputStream.writeObject(input_type+"_PROCESS_COMPLETE");
                    outputStream.flush();
                }
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.err.println("exception 발생");
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
}
