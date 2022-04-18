package server;

import controller.Controller;
import lombok.extern.slf4j.Slf4j;
import network.Packet;
import network.ProtocolType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

@Slf4j
public class ClientHandler extends Thread{

    private Map<Byte, Controller> controllerMappingMap;
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private String uuid;


    public ClientHandler(Map<Byte, Controller> mapping, Socket socket, String uuid) throws IOException {
        this.controllerMappingMap = mapping;
        this.socket = socket;
        is = socket.getInputStream();
        os = socket.getOutputStream();
        this.uuid = uuid;
    }

    @Override
    public void run(){
        Packet receivePacket = null;
        Packet sendPacket = null;

        try{
            log.info("client Handler 실행. handler ID = {}",uuid);

            while (true){

                //클라이언트가 보낸 패킷 읽기
                receivePacket = readPacket(is);

                //protocolCode 확인
                byte protocolCode = receivePacket.getProtocolType();

                //protocolCode에 맞게 매핑된 컨트롤러 가져오기
                Controller controller = getController(protocolCode);
                
                //컨트롤러 실행
                sendPacket = controller.process(receivePacket);

                //클라이언트에게 패킷 보내기
                writePacket(os,sendPacket);
            }
        }catch(IOException e){
            e.printStackTrace();
        } finally {
            exit(); // socket 종료
            Server.deleteClientHandler(uuid);
        }

        log.info("client Handler 종료. handler ID = {}",uuid);
    }

    private Controller getController(byte protocolCode) {
        Controller controller = controllerMappingMap.get(protocolCode);

        if(controller==null){// 핸들러 매핑 정보가 일치하지 않는 경우
            throw new IllegalArgumentException("Protocol code에 맞는 controller가 존재하지 않습니다. 현재 protocol Code = "+ ProtocolType.get(protocolCode));
        }
        return controller;
    }

    private Packet readPacket(InputStream is) throws IOException {
        //패킷 읽기
        //분할해서 넘어온 패킷 하나로 합치기
        return new Packet();
    }

    private void writePacket(OutputStream os, Packet sendPacket) {
        // 패킷 클라이언트에 보내기
        // 분할해야 하면 분할해서 보내기
    }

    private void exit() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUuid() {
        return uuid;
    }
}
