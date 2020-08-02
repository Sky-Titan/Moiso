import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Enumeration;
import java.util.StringTokenizer;

public class Setting extends JFrame {

    private JLabel ip_info, port_info, certify_info;
    private JLabel ip_address;
    private JTextField port_number, certify_number;
    private JButton connect;
    private JLabel client_ip,client_ip2, group_name, group_name2, people, people2;
    private JLabel receive_info,receive_info2;
    private Socket sock;
    private ServerSocket serverSocket;
    private Robot robot;

    int count = 0;

    public Setting()
    {
        super("MOISO");

        setLocation(550, 230);
        setSize(300, 500);
        setBackground(Color.WHITE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // TODO Auto-generated method stub

                try
                {
                    	ObjectOutputStream outputStream = new ObjectOutputStream(sock.getOutputStream());
                    		outputStream.writeObject("연결 종료");
                    		outputStream.flush();
                    	sock.close();
                }
                catch (Exception exception) {
                    // TODO: handle exception
                    exception.printStackTrace();
                }
                super.windowClosing(e);
                System.exit(0);
            }
        });
        setLayout(null);



        port_info = new JLabel("Port 번호 :");
        port_info.setBounds(50, 50, 100, 50);
        add(port_info);

        port_number = new JTextField("5001", 1);
        port_number.setDocument(new IntegerDocument());//숫자만 입력
        port_number.setText("5001");
        port_number.setBounds(50, 100, 200, 25);
        port_number.setBackground(Color.WHITE);
        add(port_number);


        client_ip = new JLabel("외부 ip :");
        client_ip.setBounds(80, 180, 150, 30);
        add(client_ip);

        String currentIp="x";

        try
        {
            URL whatismyip = new URL("http://checkip.amazonaws.com");// ip주소 가져오기
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            currentIp = in.readLine(); //you get the IP as a String

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        client_ip2 = new JLabel(currentIp);
        client_ip2.setBounds(80, 200, 150, 30);
        add(client_ip2);

        group_name = new JLabel("그룹 이름 : ");
        group_name.setBounds(80, 240, 150, 30);
        add(group_name);

        group_name2 = new JLabel("x");
        group_name2.setBounds(80, 260, 150, 30);
        add(group_name2);

        people = new JLabel("연결된 멤버 : ");
        people.setBounds(80,300,150,30);
        add(people);

        people2 = new JLabel("");
        people2.setBounds(80,320,150,30);
        add(people2);

        receive_info = new JLabel("수신 신호 : ");
        receive_info.setBounds(80, 360, 150, 30);
        add(receive_info);

        receive_info2 = new JLabel("x");
        receive_info2.setBounds(80, 380, 150, 30);
        add(receive_info2);

        Thread t= new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                connectStart();
            }
        });
        t.start();


        setVisible(true);
    }

    public void connectStart()
    {
        try
        {
            int port = Integer.parseInt(port_number.getText());
            System.out.println("Java server start....");
            serverSocket = new ServerSocket(port);

            System.out.println("reading from port....");
            robot = new Robot();

            while(true)
            {
                sock = serverSocket.accept();
                InetAddress clientHost = sock.getLocalAddress();
                int clientPort = sock.getPort();

                System.out.println("Client connect host : "+clientHost+" port: "+clientPort);

                ObjectInputStream inputStream;
                java.lang.Object obj;

                try
                {
                    inputStream = new ObjectInputStream(sock.getInputStream());
                    obj = inputStream.readObject();
                }
                catch(Exception e)
                {
                    if(!sock.isClosed())
                        sock.close();
                    System.out.println("exception 발생");
                    continue;
                }
                String receive = String.valueOf(obj);

                System.out.println(receive);

                StringTokenizer strtok = new StringTokenizer(receive, "&");

                String key = strtok.nextToken();
                String group_name = strtok.nextToken();
                String user_id = strtok.nextToken();

                group_name2.setText(group_name);
                people2.setText(people2.getText()+" "+user_id);


                if(key.equals("START"))
                {
                    receive_info2.setText(key);

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
                        receive_info2.setText(input_type+"_PROCESS_COMPLETE");
                        outputStream = new ObjectOutputStream(sock.getOutputStream());
                        outputStream.writeObject(input_type+"_PROCESS_COMPLETE");
                        outputStream.flush();
                    }
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
    public void inputKeyboard(String str)
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
    public void inputMouse(String str)
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
