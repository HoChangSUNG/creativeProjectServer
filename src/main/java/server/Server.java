package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;
import java.util.UUID;

import config.HandlerMapping;
import controller.Controller;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Server {

    private static ClientHandler[] handlers;
    private static int handlersSize;
    private static int handlersIndex;

    public Server() {
        handlersSize = 3;
        handlers = new ClientHandler[handlersSize];
        handlersIndex = 0;
    }

    public void run()throws IOException {
        Socket socket = null; // 사용 포트 번호
        String handlerId = null; // 클라이언트 핸들러 ID

        HandlerMapping handlerMapping = new HandlerMapping();
        Map<Byte, Controller> mappingMap = handlerMapping.getMappingMap(); // handler mapping map 가져오기

        ServerSocket serverSocket = new ServerSocket(); //서버 소켓 생성
        serverSocket.setReuseAddress(true);
        SocketAddress address = new InetSocketAddress(5000);
        serverSocket.bind(address);

        log.info("클라이언트 접속 대기중");
        log.info("현재 실행되는 핸들러 개수 = {}\n",handlersIndex);

        while (true){
            socket = serverSocket.accept(); //클라이언트용 소켓 생성
            handlerId = UUID.randomUUID().toString();

            log.info("클라이언트 접속 port 번호 = {} Client handler ID = {}",socket.getPort(),handlerId);

            startClientHandler(socket, handlerId, mappingMap); //클라이언트 핸들러 생성, 실행
        }
    }

    private void startClientHandler(Socket socket, String handlerId, Map<Byte, Controller> mappingMap) throws IOException {
        if(handlersIndex == handlersSize){
            log.info("생성 가능한 쓰레드 개수를 초과할 수 없습니다.");
            return;
        }

        handlers[handlersIndex] = new ClientHandler(mappingMap, socket, handlerId);//clientHandler 쓰레드 생성
        handlers[handlersIndex++].start(); //쓰레드 실행
        log.info("현재 실행되는 핸들러 개수 = {}\n",handlersIndex);

    }

    private static int findHandlerIndex(String uuid){
        for(int i=0;i<handlersSize;i++){
            if(handlers[i].getUuid().equals(uuid))
                return i;
        }
        return -1;
    }

    public static synchronized void deleteClientHandler(String uuid){
        log.info("Client handler 제거 시도 ID = {}",uuid);
        int curIndex = findHandlerIndex(uuid);
        if(curIndex<0){
            log.warn("Client handler 제거 실패, 존재하지 않는 Client handler, Client handler ID = {}",uuid);
            return;
        }

        for(int i=curIndex;i<handlersIndex-1;i++){
            handlers[curIndex] = handlers[curIndex+1];
        }

        handlersIndex--;
        log.info("핸들러 제거 완료 ID = {}",uuid);
        log.info("현재 실행되는 핸들러 개수 = {}\n",handlersIndex);
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.run();
    }
}
