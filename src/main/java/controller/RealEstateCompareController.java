package controller;

import domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import network.Packet;
import network.ProtocolType;
import network.protocolCode.RealEstateCompareCode;
import network.protocolCode.RealEstateInfoCode;
import persistence.dao.ApartmentDAO;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.*;

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
        else if(protocolCode == RealEstateCompareCode.REAL_ESTATE_APARTMENT_INFO_REQ.getCode()){ // 기능 2.2
            packet = realEstateApartmentInfoResPacket(receivePacket);
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

        SelectApartRegion selectApartRegion = (SelectApartRegion) receivePacket.getBody();


        log.info("선택된 시군구 : {}, 선택된 읍면동 : {}",selectApartRegion.getSigunguName(),selectApartRegion.getEupmyeondongName());
        List<ApartmentInfo1> apartmentList = null;
        apartmentList = apartmentDAO.findApartment(selectApartRegion.getEupmyeondongName(), selectApartRegion.getSigunguRegionCode());
//        for(int i = 0; i < apartmentList.size(); i ++) {
//            double tempArea = Math.round(apartmentList.get(i).getArea() / 3.3058 * 10)/10.0;
//            apartmentList.get(i).setArea((float)tempArea);
//        }


        Packet packet = new Packet(protocolType,ProtocolCode,apartmentList);
        return packet;
    }

    private Packet realEstateApartmentInfoResPacket(Packet receivePacket) {
        log.info("기능 2.2 실행");
        byte protocolType = ProtocolType.REAL_ESTATE_COMPARE.getType();
        byte ProtocolCode = RealEstateCompareCode.REAL_ESTATE_APARTMENT_INFO_RES.getCode();

        ApartmentInfo2 apartmentInfo = (ApartmentInfo2) receivePacket.getBody();

        List<ApartmentInfo3> apartmentList = null;
        float area = (float)Math.floor(apartmentInfo.getArea());
        apartmentList = apartmentDAO.findApartmentInfo(area,apartmentInfo.getApartmentName(),apartmentInfo.getRegionalCode(),apartmentInfo.getRegionName());

        Packet packet = new Packet(protocolType,ProtocolCode,apartmentList);
        return packet;
    }
}
