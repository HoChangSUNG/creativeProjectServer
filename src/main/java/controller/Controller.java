package controller;

import network.Packet;

public interface Controller {
    Packet process(Packet receivePacket);
}
