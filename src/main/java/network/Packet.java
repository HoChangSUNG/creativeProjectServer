package network;

public class Packet {

    private byte protocolCode;

    public byte getProtocolType(){
        return protocolCode;
    }

    public void setProtocolCode(byte code){
        protocolCode = code;
    }

}
