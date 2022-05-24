package controller;

import body.SendDataResBody;
import domain.ApartmentForSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import network.Packet;
import network.ProtocolType;
import network.protocolCode.RealEstateInfoCode;
import network.protocolCode.RealEstateRecommendCode;
import service.AverageAreaAoumtApartmentService;

@RequiredArgsConstructor
@Slf4j
public class RealEstateRecommendController implements Controller{

    private final AverageAreaAoumtApartmentService averageAreaAoumtApartmentService;
    //3. 부동산 추천 컨트롤러
    @Override
    public Packet process(Packet receivePacket) {
        log.info("RealEstateRecommendController 입니다.");
        byte protocolCode = receivePacket.getProtocolCode();
        Packet packet = null;

        if(protocolCode == RealEstateRecommendCode.RECORD_INFO_REQ.getCode()){ // 기능 3.1
            packet = recordInfoPacket(receivePacket);
        }
        else if(protocolCode == RealEstateRecommendCode.REGION_RECOMMEND_REQ.getCode()){ //기능 3.2
            packet = regionRecommendPacket(receivePacket);
        }
        else{
            throw new RuntimeException("존재하지 않는 코드입니다");
        }
        return packet;
    }

    private Packet recordInfoPacket(Packet receivePacket) {
        log.info("기능 3.1 실행");
        byte protocolType = ProtocolType.REAL_ESTATE_RECOMMEND.getType();
        byte ProtocolCode = RealEstateRecommendCode.RECORD_INFO_RES.getCode();

        ApartmentForSearch apartmentForSearch = (ApartmentForSearch)receivePacket.getBody();

        SendDataResBody sendDataResBody = new SendDataResBody();
        sendDataResBody.setAverageAreaAmoumtApartmentList(averageAreaAoumtApartmentService.findApartmentAoumtdata(apartmentForSearch));
        Packet packet = new Packet(protocolType,ProtocolCode,sendDataResBody);
        return packet;
    }
    private Packet regionRecommendPacket(Packet receivePacket) {
        log.info("기능 3.2 실행");
        byte protocolType = ProtocolType.REAL_ESTATE_RECOMMEND.getType();
        byte ProtocolCode = RealEstateRecommendCode.REGION_RECOMMEND_RES.getCode();
        String body = "3.2 REGION_RECOMMEND_RES";

        Packet packet = new Packet(protocolType,ProtocolCode,body);
        return packet;
    }



}
