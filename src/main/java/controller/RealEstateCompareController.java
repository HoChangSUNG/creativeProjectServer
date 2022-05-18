package controller;

import domain.Apartment;
import domain.ApartmentInfo1;
import domain.EupMyeonDong;
import domain.Sigungu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import network.Packet;
import network.ProtocolType;
import network.protocolCode.RealEstateCompareCode;
import network.protocolCode.RealEstateInfoCode;
import persistence.dao.ApartmentDAO;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class RealEstateCompareController implements Controller{

    //2. 부동산 지역 비교 컨트롤러
    final private ApartmentDAO apartmentDAO;
    @Override
    public Packet process(Packet receivePacket) {
        log.info("RealEstateCompareController 입니다.");
        byte protocolCode = receivePacket.getProtocolCode();
        Packet packet = null;

        if(protocolCode == RealEstateCompareCode.REAL_ESTATE_APARTMENT_REQ.getCode()){ // 기능 2.1
            packet = realEstateApartmentResPacket(receivePacket);
        }
        else{
            throw new RuntimeException("존재하지 않는 코드입니다.");
        }
        return packet;
    }

    private Packet realEstateApartmentResPacket(Packet receivePacket) {
        log.info("기능 2.1 실행");
        byte protocolType = ProtocolType.REAL_ESTATE_COMPARE.getType();
        byte ProtocolCode = RealEstateCompareCode.REAL_ESTATE_APARTMENT_RES.getCode();

        Sigungu sigungu = null;
        sigungu = (Sigungu) receivePacket.getBody();

        List<ApartmentInfo1> apartmentList = null;
        apartmentList = apartmentDAO.findApartment(sigungu.getEupMyeonDongList().get(sigungu.getEupMyeonDongIndex()).getRegionName(), sigungu.getRegionalCode());

        Packet packet = new Packet(protocolType,ProtocolCode,apartmentList);
        return packet;
    }
}
