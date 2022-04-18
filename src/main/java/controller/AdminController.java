package controller;

import lombok.extern.slf4j.Slf4j;
import network.Packet;

@Slf4j
public class AdminController implements Controller{
    @Override
    public Packet process(Packet receivePacket) {
        log.info("AdminController 입니다.");
        return null;
    }
}
