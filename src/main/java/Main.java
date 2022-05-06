
import jdk.swing.interop.SwingInterOpUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.Month;


public class Main {
    public static void main(String[] args) {
        DBInit dbInit = new DBInit();
        dbInit.initRegion();

//        LocalDate startDate = LocalDate.of(2022, 1, 1); // dayOfMonth는 무조건 1로 설정
//        LocalDate endDate = LocalDate.of(2022,5,1); // dayOfMonth는 무조건 1로 설정


//        dbInit.initRegion();
//        dbInit.initPopulation();
//       dbInit.initApartment(startDate,endDate,"11110","DxPzuHVkt4yG1p%2FSpF%2FXChtnOm3jcdhJunTY%2F87a9cRE%2FNc2XYWJ1hgyZHZmpdF6g2ZWaHYGSD%2F5LioW0lc0DQ%3D%3D");
//       dbInit.initRowHouse(startDate,endDate,"11110","DxPzuHVkt4yG1p%2FSpF%2FXChtnOm3jcdhJunTY%2F87a9cRE%2FNc2XYWJ1hgyZHZmpdF6g2ZWaHYGSD%2F5LioW0lc0DQ%3D%3D");
//       dbInit.initDetachedHouse(startDate,endDate,"11110", "DxPzuHVkt4yG1p%2FSpF%2FXChtnOm3jcdhJunTY%2F87a9cRE%2FNc2XYWJ1hgyZHZmpdF6g2ZWaHYGSD%2F5LioW0lc0DQ%3D%3D");

//        dbInit.initPopulation();

        List<Sigungu> sigungus = dbInit.getSigungu();
        for (Sigungu sigungu : sigungus) {
            System.out.println(sigungu.getRegionalCode()+sigungu.getRegionName());
        }
    }

}
