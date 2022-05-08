package config;

import controller.RealEstateCompareController;
import controller.RealEstateInfoController;
import controller.Controller;
import controller.RealEstateRecommendController;
import network.ProtocolType;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {

    private Map<Byte, Controller> handlerMappingMap = new HashMap<>();

    public HandlerMapping() {
        initMappingMap();
    }

    public Map<Byte, Controller> getMappingMap(){
        return handlerMappingMap;
    }

    private void initMappingMap(){
        // 매핑 맵에 프로토콜 코드와 컨트롤러 매핑
        Controller realEstateInfoController = new RealEstateInfoController();
        Controller realEstateCompareController = new RealEstateCompareController();
        Controller realEstateRecommendController = new RealEstateRecommendController();

        handlerMappingMap.put(ProtocolType.REAL_ESTATE_INFO.getType(),realEstateInfoController);
        handlerMappingMap.put(ProtocolType.REAL_ESTATE_COMPARE.getType(), realEstateCompareController);
        handlerMappingMap.put(ProtocolType.REAL_ESTATE_RECOMMEND.getType(), realEstateRecommendController);

    }
}