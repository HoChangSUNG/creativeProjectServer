package controller;

import lombok.extern.slf4j.Slf4j;
import network.Packet;

@Slf4j
public class StudentController implements Controller{
    @Override
    public Packet process(Packet receivePacket) {
        log.info("StudentController 입니다.");
        return null;
    }
}
