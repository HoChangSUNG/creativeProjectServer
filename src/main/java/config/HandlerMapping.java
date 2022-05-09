package config;

import controller.RealEstateCompareController;
import controller.RealEstateInfoController;
import controller.Controller;
import controller.RealEstateRecommendController;
import network.ProtocolType;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.MyBatisConnectionFactory;
import persistence.dao.AverageDataDAO;
import service.AverageDataService;

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
        SqlSessionFactory sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();

        //dao
        AverageDataDAO averageDataDAO = new AverageDataDAO(sqlSessionFactory);


        //service
        AverageDataService averageDataService = new AverageDataService(averageDataDAO);


        // 매핑 맵에 프로토콜 코드와 컨트롤러 매핑
        Controller realEstateInfoController = new RealEstateInfoController(averageDataService);
        Controller realEstateCompareController = new RealEstateCompareController();
        Controller realEstateRecommendController = new RealEstateRecommendController();

        handlerMappingMap.put(ProtocolType.REAL_ESTATE_INFO.getType(),realEstateInfoController);
        handlerMappingMap.put(ProtocolType.REAL_ESTATE_COMPARE.getType(), realEstateCompareController);
        handlerMappingMap.put(ProtocolType.REAL_ESTATE_RECOMMEND.getType(), realEstateRecommendController);

    }
}
