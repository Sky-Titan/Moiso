import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

public class Setting extends JFrame {

    private JLabel ip_info, port_info, certify_info;
    private JLabel ip_address;
    private JTextField port_number, certify_number;
    private JButton connect;
    public JLabel client_ip,client_ip2, group_name, group_name2, people, people2;
    public JLabel receive_info,receive_info2;

    private ServerSocket serverSocket;

    public Robot robot;

    private static final int MAX_THREAD = 20;
    private ArrayList<SocketThread> threads;

    //연결 스레드 정해줌
    public int connect_thread = 0;

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

                for(int i=0;i<threads.size();i++)
                {
                    threads.get(i).disconnect();
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


        threads = new ArrayList<>();

        setVisible(true);
        waitConnect();


    }

    //연결 대기
    public void waitConnect()
    {
        try
        {
            System.out.println("Java server start....");
            serverSocket = new ServerSocket(Integer.parseInt(port_number.getText()));

            System.out.println("reading from port....");
            robot = new Robot();

            //연결 요청 기다림
            while(true) {

                Socket sock = serverSocket.accept();
                InetAddress clientHost = sock.getLocalAddress();
                int clientPort = sock.getPort();

                Callback callback = new Callback() {
                    @Override
                    public void removeCallback(SocketThread thread) {
                        threads.remove(thread);
                    }
                };

                //스레드 최대 개수
                if(threads.size() < MAX_THREAD)
                {
                    threads.add(new SocketThread(sock, this, callback));
                    threads.get(threads.size()-1).start();
                    System.out.println("Current Threads Size : "+threads.size());
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.err.println("exception 발생");
        }
    }



}
