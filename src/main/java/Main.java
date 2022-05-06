import domain.Sigungu;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DBInit dbInit = new DBInit();
        dbInit.initRegion();

//        dbInit.initApartment();
//        dbInit.initRowHouse();
//        dbInit.initDetachedHouse();


//        dbInit.initPopulation();

        List<Sigungu> sigungus = dbInit.getSigungu();
        for (Sigungu sigungu : sigungus) {
            System.out.println(sigungu.getRegionalCode()+sigungu.getRegionName());
        }

    }
}
