package config;

import controller.AdminController;
import controller.Controller;
import controller.ExitController;
import controller.StudentController;
import network.ProtocolType;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {

    private Map<Byte, Controller> handlerMappingMap = new HashMap<>();

    public AppConfig() {
        initMappingMap();
    }

    public Map<Byte, Controller> getMappingMap(){
        return handlerMappingMap;
    }

    private void initMappingMap(){
        // 매핑 맵에 프로토콜 코드와 컨트롤러 매핑
        Controller adminController = new AdminController();
        Controller exitController = new ExitController();
        Controller studentController = new StudentController();

        handlerMappingMap.put(ProtocolType.EXIT.getType(),exitController);
        handlerMappingMap.put(ProtocolType.STUDENT.getType(), studentController);
        handlerMappingMap.put(ProtocolType.ADMIN.getType(), adminController);

    }
}
