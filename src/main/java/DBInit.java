import domain.Population;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBInit {

    private Connection conn;

    public DBInit() {
        try {
            conn = DriverManager.getConnection(
                    "", // url
                    "", // user
                    "" // password
            );
            conn.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node item = (Node) nodeList.item(0);
        if (item == null) {
            return null;
        }

        return item.getTextContent();
    }

    public void initApartment() {

        PreparedStatement pstmt = null;
        try {

            pstmt = conn.prepareStatement("insert into apartment(deal_amount, build_year, deal_year, deal_month, deal_day, apartment_name, area, floor, jibun, region_name) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            String url = "http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTrade";
            String serviceKey = "";
            String lawd_cd = "11110";
            String deal_ymd = "201001";

            Document documentInfo = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(url + "?serviceKey=" + serviceKey + "&LAWD_CD=" + lawd_cd + "&DEAL_YMD=" + deal_ymd);

            documentInfo.normalize();
            NodeList nodeList = documentInfo.getElementsByTagName("item");
            System.out.println(nodeList.getLength());

            LocalDate startDate = LocalDate.of(2011, 1, 1);
            LocalDate endDate = LocalDate.now();

            for (; startDate.isAfter(endDate); startDate.plusMonths(1)) {

                deal_ymd = String.valueOf(startDate.getYear()) + String.valueOf(startDate.getMonth());

                for (; ; ) { // 지역코드

                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);

                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) node;

                            pstmt.setInt(1, Integer.parseInt(getTagValue("거래금액", element).replace(",", "")));
                            pstmt.setInt(2, Integer.parseInt(getTagValue("건축년도", element)));
                            pstmt.setInt(3, Integer.parseInt(getTagValue("년", element)));
                            pstmt.setInt(4, Integer.parseInt(getTagValue("월", element)));
                            pstmt.setInt(5, Integer.parseInt(getTagValue("일", element)));
                            pstmt.setString(6, (getTagValue("아파트", element)));
                            pstmt.setFloat(7, Float.parseFloat(getTagValue("전용면적", element)));
                            pstmt.setInt(8, Integer.parseInt(getTagValue("층", element)));
                            pstmt.setString(9, getTagValue("지번", element));
                            pstmt.setString(10, (getTagValue("지역코드", element)));
                            pstmt.setString(11, (getTagValue("법정동", element)));

                            pstmt.addBatch();
                        }
                    }
                }
            }

            pstmt.executeBatch();
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e3) {
                }
            }
        }
    }

    public void initRowHouse() {

        PreparedStatement pstmt = null;
        try {

            pstmt = conn.prepareStatement("insert into rowhouse(deal_amount, build_year, deal_year, deal_month, deal_day, rowhouse_name, area, floor, jibun, region_name) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            String url = "http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcRHTrade";
            String serviceKey = "";
            String lawd_cd = "11110";
            String deal_ymd = "201001";

            Document documentInfo = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(url + "?serviceKey=" + serviceKey + "&LAWD_CD=" + lawd_cd + "&DEAL_YMD=" + deal_ymd);

            documentInfo.normalize();
            NodeList nodeList = documentInfo.getElementsByTagName("item");
            System.out.println(nodeList.getLength());

            LocalDate startDate = LocalDate.of(2011, 1, 1);
            LocalDate endDate = LocalDate.now();

            for (; startDate.isAfter(endDate); startDate.plusMonths(1)) {

                deal_ymd = String.valueOf(startDate.getYear()) + String.valueOf(startDate.getMonth());

                for (; ; ) { // 지역코드

                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);

                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) node;

                            pstmt.setInt(1, Integer.parseInt(getTagValue("거래금액", element).replace(",", "")));
                            pstmt.setInt(2, Integer.parseInt(getTagValue("건축년도", element)));
                            pstmt.setInt(3, Integer.parseInt(getTagValue("년", element)));
                            pstmt.setInt(4, Integer.parseInt(getTagValue("월", element)));
                            pstmt.setInt(5, Integer.parseInt(getTagValue("일", element)));
                            pstmt.setString(6, (getTagValue("연립다세대명", element)));
                            pstmt.setFloat(7, Float.parseFloat(getTagValue("전용면적", element)));
                            pstmt.setInt(8, Integer.parseInt(getTagValue("층", element)));
                            pstmt.setString(9, getTagValue("지번", element));
                            pstmt.setString(10, (getTagValue("지역코드", element)));
                            pstmt.setString(11, (getTagValue("법정동", element)));

                            pstmt.addBatch();
                        }
                    }
                }
            }

            pstmt.executeBatch();
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e3) {
                }
            }
        }
    }

    public void initDetachedHouse() {

        PreparedStatement pstmt = null;
        try {

            pstmt = conn.prepareStatement("insert into detachedhouse(deal_amount, build_year, deal_year, deal_month, deal_day, house_type, area, floor, jibun, region_name) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            String url = "http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcShTrade";
            String serviceKey = "";
            String lawd_cd = "11110";
            String deal_ymd = "201001";

            Document documentInfo = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(url + "?serviceKey=" + serviceKey + "&LAWD_CD=" + lawd_cd + "&DEAL_YMD=" + deal_ymd);

            documentInfo.normalize();
            NodeList nodeList = documentInfo.getElementsByTagName("item");
            System.out.println(nodeList.getLength());

            LocalDate startDate = LocalDate.of(2011, 1, 1);
            LocalDate endDate = LocalDate.now();

            for (; startDate.isAfter(endDate); startDate.plusMonths(1)) {

                deal_ymd = String.valueOf(startDate.getYear()) + String.valueOf(startDate.getMonth());

                for (; ; ) { // 지역코드

                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);

                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) node;

                            pstmt.setInt(1, Integer.parseInt(getTagValue("거래금액", element).replace(",", "")));
                            pstmt.setInt(2, Integer.parseInt(getTagValue("건축년도", element)));
                            pstmt.setInt(3, Integer.parseInt(getTagValue("년", element)));
                            pstmt.setInt(4, Integer.parseInt(getTagValue("월", element)));
                            pstmt.setInt(5, Integer.parseInt(getTagValue("일", element)));
                            pstmt.setString(6, (getTagValue("주택유형", element)));
                            pstmt.setFloat(7, Float.parseFloat(getTagValue("전용면적", element)));
                            pstmt.setInt(8, Integer.parseInt(getTagValue("층", element)));
                            pstmt.setString(9, getTagValue("지번", element));
                            pstmt.setString(10, (getTagValue("지역코드", element)));
                            pstmt.setString(11, (getTagValue("법정동", element)));

                            pstmt.addBatch();



/*                  System.out.println("##############################");
                    System.out.println("거래금액 : " + getTagValue("거래금액", element));
                    System.out.println("건축년도 : " + getTagValue("건축년도", element));
                    System.out.println("계약년 : " + getTagValue("년", element));
                    System.out.println("계약월 : " + getTagValue("월", element));
                    System.out.println("계약일 : " + getTagValue("일", element));
                    System.out.println("법정동 : " + getTagValue("법정동", element));
                    System.out.println("아파트명 : " + getTagValue("아파트", element));
                    System.out.println("면적 : " + getTagValue("전용면적", element));
                    System.out.println("층 : " + getTagValue("층", element));
                    System.out.println("지번 : " + getTagValue("지번", element));
                    System.out.println("지역코드 : " + getTagValue("지역코드", element));*/

                        }
                    }
                }
            }

            pstmt.executeBatch();
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e3) {
                }
            }
        }
    }

    public void initRegion() {
        String path = "src/main/resources/code.txt";
        ArrayList<String> input = new ArrayList<>();
        String[] input_arr = new String[0];
        BufferedReader br = null;
        String[] eupmyeondong;
        String[] sigungu;
        String[] sido;

        PreparedStatement pstmt = null;

        try {
            br = new BufferedReader(new FileReader(new File(path)));
            String s;
            while ((s = br.readLine()) != null) {
                input.add(s);
            }
            br.close();

            input_arr = input.toArray(input_arr);

            eupmyeondong = new String[input_arr.length];
            sigungu = new String[input_arr.length];
            sido = new String[input_arr.length];

            for (int i = 0; i < input_arr.length; i++) {
                String[] arr = input_arr[i].split("\t");

                sido[i] = arr[0].substring(0, 2) + " " + arr[1];
                sigungu[i] = arr[0].substring(0, 5) + " " + arr[2];
                eupmyeondong[i] = arr[0].substring(0, 5) + " " + arr[3];
            }

            eupmyeondong = Arrays.stream(eupmyeondong).distinct().toArray(String[]::new);
            sigungu = Arrays.stream(sigungu).distinct().toArray(String[]::new);
            sido = Arrays.stream(sido).distinct().toArray(String[]::new);

            pstmt = conn.prepareStatement("insert into sido values(?, ?)");

            for (String a : sido) {
                String[] sd = a.split(" ");
                pstmt.setString(1, sd[0]);
                pstmt.setString(2, sd[1]);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            pstmt.clearBatch();

            pstmt = conn.prepareStatement("insert into sigungu values(?, ?)");
            for (String a : sigungu) {
                String[] sgg = a.split(" ");
                pstmt.setString(1, sgg[0]);
                pstmt.setString(2, sgg[1]);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            pstmt.clearBatch();

            pstmt = conn.prepareStatement("insert into eupmyeondong values(?, ?)");
            for (String a : eupmyeondong) {
                String[] emd = a.split(" ");
                pstmt.setString(1, emd[1]);
                pstmt.setString(2, emd[0]);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e3){
                }
            }
        }
    }

    private List<Population> getPopulationDTOS() {
        ArrayList<Population> populationDTOS = new ArrayList<>();

        File file = new File("src/main/resources/code.txt"); // 법정동 인구 수 파일 경로

        try {
            BufferedReader inFiles = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF8"));

            String line = "";

            while((line = inFiles.readLine()) != null) {
                //System.out.println(line);
                String[] split = line.split("\t");

                int regionalCode = Integer.parseInt(split[0].trim())/100000;
                String dongName = split[3].trim();
                int population = Integer.parseInt(split[4].trim());

                Population populationDTO = new Population(String.valueOf(regionalCode), dongName, population);

                if(populationDTOS.size()<1) // 최초 동 입력시
                    populationDTOS.add(populationDTO);

                Population lastPopulationDTO = populationDTOS.get(populationDTOS.size() - 1);
                if(lastPopulationDTO.getRegionName().equals(populationDTO.getRegionName())) { // 같은 동이 이미 존재하는 경우
                    int curPopulationCnt = lastPopulationDTO.getPopulation() + populationDTO.getPopulation();
                    lastPopulationDTO.setPopulation(curPopulationCnt);
                }
                else{ // 같은 동 존재 안하는 경우
                    populationDTOS.add(populationDTO);
                }
            }
            inFiles.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        populationDTOS.forEach(populationDTO -> {
//            System.out.println(populationDTO.getRegionalCode()+populationDTO.getDongName()+populationDTO.getPeopleCnt());
//        });

        return populationDTOS;
    }


}
