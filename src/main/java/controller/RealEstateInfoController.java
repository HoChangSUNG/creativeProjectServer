package controller;

import java.time.LocalDate;

import body.FluctuationRateWrapper;
import body.SelectRegionGraphData;
import domain.ApartmentIndex;
import domain.FluctuationRate;
import domain.Sido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import network.Packet;
import network.ProtocolType;
import network.protocolCode.RealEstateInfoCode;
import persistence.dao.ApartmentIndexDAO;
import persistence.dao.SidoDAO;
import service.AverageDataService;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class RealEstateInfoController implements Controller{

    private final AverageDataService averageDataService;
    //1. 지역별 부동산 가격 제공 컨트롤러
    private final SidoDAO sidoDAO;
    private final ApartmentIndexDAO apartmentIndexDAO;

    @Override
    public Packet process(Packet receivePacket) {
        log.info("RealEstateInfoController 입니다.");
        byte protocolCode = receivePacket.getProtocolCode();
        Packet packet = null;

        if(protocolCode == RealEstateInfoCode.SEND_DATA_REQ.getCode()){ // 기능 1.1
            packet = sendDataPacket(receivePacket);
        }
        else if(protocolCode == RealEstateInfoCode.SEND_GRAPH_DATA_REQ.getCode()){ //기능 1.2
            packet = sendGraphDataPacket(receivePacket);
        }
        else if(protocolCode == RealEstateInfoCode.REGION_SELECTION_REQ.getCode()){
            packet = sendSelectRegionList(receivePacket);
        } else if (protocolCode == RealEstateInfoCode.GRAPH_REGION_SELECTION_FIRST_REQ.getCode()) {
            packet = sendFirstRegion(receivePacket);
        } else if (protocolCode == RealEstateInfoCode.GRAPH_REGION_SELECTION_SECOND_REQ.getCode()) {
            packet = sendSecondRegion(receivePacket);
        } else {
            throw new RuntimeException("존재하지 않는 코드입니다");
        }
        return packet;
    }

    private Packet sendDataPacket(Packet receivePacket) {
        log.info("기능 1.1 실행");
        byte protocolType = ProtocolType.REAL_ESTATE_INFO.getType();
        byte ProtocolCode = RealEstateInfoCode.SEND_DATA_RES.getCode();

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue()-1;

        FluctuationRateWrapper fluctuationRateWrapper = new FluctuationRateWrapper();
        fluctuationRateWrapper.setApartmentFluctuationRate(averageDataService.findApartmentFRByDate(year,month));
        fluctuationRateWrapper.setRowhouseFluctuationRate(averageDataService.findRowhouseFRByDate(year,month));
        fluctuationRateWrapper.setDetachedhouseFluctuationRate(averageDataService.findDetachedhouseFRByDate(year,month));

        Packet packet = new Packet(protocolType,ProtocolCode,fluctuationRateWrapper);
        return packet;
    }

    private Packet sendGraphDataPacket(Packet receivePacket) {
        log.info("기능 1.2 실행");
        byte protocolType = ProtocolType.REAL_ESTATE_INFO.getType();
        byte ProtocolCode = RealEstateInfoCode.SEND_GRAPH_DATA_RES.getCode();
        String body = "1.2 SEND_GRAPH_DATA_RES";

        Packet packet = new Packet(protocolType,ProtocolCode,body);
        return packet;
    }

    private Packet sendSelectRegionList(Packet receivePacket){
        log.info("지역 선택 리스트 전달");
        byte protocolType = ProtocolType.REAL_ESTATE_INFO.getType();
        byte ProtocolCode = RealEstateInfoCode.REGION_SELECTION_RES.getCode();

        List<Sido> selectRegionList = sidoDAO.findSelectRegionList();

        return new Packet(protocolType,ProtocolCode,selectRegionList);
    }

    private Packet sendFirstRegion(Packet receivePackpet) {
        log.info("1 첫번째 기능 실행");

        String region = (String) receivePackpet.getBody();

        byte protocolType = ProtocolType.REAL_ESTATE_INFO.getType();
        byte protocolCode = RealEstateInfoCode.GRAPH_REGION_SELECTION_FIRST_RES.getCode();

        List<ApartmentIndex> apartmentIndexList = apartmentIndexDAO.findApartmentIndex(region);

        System.out.println(region);
        return new Packet(protocolType, protocolCode, apartmentIndexList);
    }

    private Packet sendSecondRegion(Packet receivePackpet) {
        log.info("1 두번째 기능 실행");

        String[] check = {"중구", "서구", "동구", "남구", "북구"};

        SelectRegionGraphData body = (SelectRegionGraphData) receivePackpet.getBody();
        String region = null;
        String sido = null;

        if(body.getSigunguName() == null){
            region = body.getSidoName();
        }
        else {
            if(body.getSigunguName().split(" ").length == 2) {
                region = body.getSigunguName().split(" ")[1];
                sido = body.getSigunguName().split(" ")[0];

                if(Arrays.stream(check).anyMatch(region::equals)){
                    region = region + "(" + sido.substring(0,2) + ")";
                }
            }
            else {
                region = body.getSigunguName();
            }

            if(Arrays.stream(check).anyMatch(region::equals)){
                region = body.getSigunguName() + "(" + body.getSidoName().substring(0,2) + ")";
            }
        }

        byte protocolType = ProtocolType.REAL_ESTATE_INFO.getType();
        byte protocolCode = RealEstateInfoCode.GRAPH_REGION_SELECTION_SECOND_RES.getCode();

        List<ApartmentIndex> apartmentIndexList = apartmentIndexDAO.findApartmentIndex(region);

        System.out.println(region);
        return new Packet(protocolType, protocolCode, apartmentIndexList);
    }

}
