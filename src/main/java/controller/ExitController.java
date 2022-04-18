package controller;

import lombok.extern.slf4j.Slf4j;
import network.Packet;

@Slf4j
public class ExitController implements Controller{
    @Override
    public Packet process(Packet receivePacket) {
        log.info("ExitController 입니다.");
        return null;
    }
}
