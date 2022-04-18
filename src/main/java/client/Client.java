package client;

import lombok.extern.slf4j.Slf4j;
import network.ProtocolType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

@Slf4j
public class Client {
    private final static int serverPort = 5000;
    private Socket socket;
    private OutputStream os;
    private InputStream is;

    public Client(InetAddress ip) throws IOException {
        socket = new Socket(ip,serverPort);
        os = socket.getOutputStream();
        is = socket.getInputStream();
        run();
    }
    public void run() throws IOException {

        log.info("클라이언트 실행");
        byte[] b = new byte[3];
        b[0] = ProtocolType.ADMIN.getType();
        b[1] = ProtocolType.STUDENT.getType();
        b[2] = ProtocolType.EXIT.getType();
        for (byte b1 : b) {
            System.out.println(ProtocolType.get(b1));
        }
        while (true){
            for (byte b1 : b) {
                os.write(b1);
            }

        }

    }
    public static void main(String[] args) {
        try {
            InetAddress ip = InetAddress.getByName("localhost");
            Client client = new Client(ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
