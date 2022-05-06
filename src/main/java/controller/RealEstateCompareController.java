package controller;

import lombok.extern.slf4j.Slf4j;
import network.Packet;
import network.ProtocolType;
import network.protocolCode.RealEstateCompareCode;
import network.protocolCode.RealEstateInfoCode;

@Slf4j
public class RealEstateCompareController implements Controller{

    //2. 부동산 지역 비교 컨트롤러
    @Override
    public Packet process(Packet receivePacket) {
        log.info("RealEstateCompareController 입니다.");
        byte protocolCode = receivePacket.getProtocolCode();
        Packet packet = null;

        if(protocolCode == RealEstateCompareCode.REAL_ESTATE_COMPARE_REQ.getCode()){ // 기능 2.1
            packet = realEstateComparePacket(receivePacket);
        }
        else{
            throw new RuntimeException("존재하지 않는 코드입니다.");
        }
        return packet;
    }

    private Packet realEstateComparePacket(Packet receivePacket) {
        log.info("기능 2.1 실행");
        byte protocolType = ProtocolType.REAL_ESTATE_COMPARE.getType();
        byte ProtocolCode = RealEstateCompareCode.REAL_ESTATE_COMPARE_RES.getCode();
        String body = "2.1 REAL_ESTATE_COMPARE_RES";

        Packet packet = new Packet(protocolType,ProtocolCode,body);
        return packet;
    }
}
