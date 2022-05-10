import domain.Population;
import domain.Sigungu;
import lombok.extern.slf4j.Slf4j;
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
import java.util.NoSuchElementException;

@Slf4j
public class DBInit {

    private Connection conn;
    private String path = ""; // 시도 시군구 법정동 인구수 및 지역코드 파일
    private String path1 = "";
    private String path2= "";
    private String path3= "";

    public DBInit() {
        try {

            Class.forName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
            conn = DriverManager.getConnection(
                    "jdbc:log4jdbc:mysql://localhost:3306/realestate?serverTimezone=UTC", // url
                    "", // user
                    "" // password
            );
            conn.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTagValue(String tag, Element element) {
        try{
            NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
            Node item = (Node) nodeList.item(0);

            return item.getTextContent();
        }catch (NullPointerException e){
            if(tag.equals("건축년도"))//"건축년도" 태그가 api에 없는 경우.
                return "0";
            else
                throw new NoSuchElementException(tag+"가 호출한 api에 없는 태그입니다.");// 해당 tag 이름이 없는 경우.
        }

    }

    public void initApartment(LocalDate startDate, LocalDate endDate, String lawd_cd, String serviceKey) { // 아파트 실거래 openApi 정보 db에 저장
        String start = getDateToString(startDate);
        String end = getDateToString(endDate);
        PreparedStatement pstmt = null;
        String deal_ymd;

        try {
            pstmt = conn.prepareStatement("insert into apartment(deal_amount, build_year, deal_year, deal_month, deal_day, apartment_name, area, floor, jibun, region_name,regional_code) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            for (; !startDate.isAfter(endDate); startDate = startDate.plusMonths(1)) {

                deal_ymd = getDealYmd(startDate);
                String url = "http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTrade";

                Document documentInfo = DocumentBuilderFactory
                        .newInstance()
                        .newDocumentBuilder()
                        .parse(url + "?serviceKey=" + serviceKey + "&LAWD_CD=" + lawd_cd + "&DEAL_YMD=" + deal_ymd);

                documentInfo.normalize();
                NodeList nodeList = documentInfo.getElementsByTagName("item");
                System.out.println(nodeList.getLength());

                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        pstmt.setInt(1, Integer.parseInt(getTagValue("거래금액", element).replace(",", "").trim()));
                        pstmt.setInt(2, Integer.parseInt(getTagValue("건축년도", element).trim()));
                        pstmt.setInt(3, Integer.parseInt(getTagValue("년", element).trim()));
                        pstmt.setInt(4, Integer.parseInt(getTagValue("월", element).trim()));
                        pstmt.setInt(5, Integer.parseInt(getTagValue("일", element).trim()));
                        pstmt.setString(6, (getTagValue("아파트", element).trim()));
                        pstmt.setFloat(7, Float.parseFloat(getTagValue("전용면적", element).trim()));
                        pstmt.setInt(8, Integer.parseInt(getTagValue("층", element).trim()));
                        pstmt.setString(9, getTagValue("지번", element).trim());
                        pstmt.setString(10, (getTagValue("법정동", element).trim().split(" ")[0]));
                        pstmt.setString(11, (getTagValue("지역코드", element).trim()));

                        pstmt.addBatch();
                    }
                }
                pstmt.executeBatch();
                conn.commit();
                pstmt.clearBatch();
            }


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
        log.info("아파트 실거래 정보 저장 지역코드:={} startDate = {}, endDate = {}", lawd_cd, start, end);
    }

    private String getDealYmd(LocalDate startDate) {// api 호출시 사용하는 dealYmd
        return String.format("%d%02d", startDate.getYear(), startDate.getMonthValue());
    }

    private String getDateToString(LocalDate date) {
        return String.format("%d-%02d", date.getYear(), date.getMonthValue());
    }

    public void initRowHouse(LocalDate startDate, LocalDate endDate, String lawd_cd, String serviceKey) { //연립 다세대 실거래 openApi 정보 db에 저장
        String start = getDateToString(startDate);
        String end = getDateToString(endDate);
        PreparedStatement pstmt = null;
        String deal_ymd;

        try {

            pstmt = conn.prepareStatement("insert into rowhouse(deal_amount, build_year, deal_year, deal_month, deal_day, rowhouse_name, area, floor, jibun, region_name,regional_code) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            for (; !startDate.isAfter(endDate); startDate = startDate.plusMonths(1)) {
                deal_ymd = getDealYmd(startDate);

                String url = "http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcRHTrade";

                Document documentInfo = DocumentBuilderFactory
                        .newInstance()
                        .newDocumentBuilder()
                        .parse(url + "?serviceKey=" + serviceKey + "&LAWD_CD=" + lawd_cd + "&DEAL_YMD=" + deal_ymd);

                documentInfo.normalize();
                NodeList nodeList = documentInfo.getElementsByTagName("item");
                //System.out.println(nodeList.getLength());


                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        pstmt.setInt(1, Integer.parseInt(getTagValue("거래금액", element).replace(",", "").trim()));
                        pstmt.setInt(2, Integer.parseInt(getTagValue("건축년도", element).trim()));
                        pstmt.setInt(3, Integer.parseInt(getTagValue("년", element).trim()));
                        pstmt.setInt(4, Integer.parseInt(getTagValue("월", element).trim()));
                        pstmt.setInt(5, Integer.parseInt(getTagValue("일", element).trim()));
                        pstmt.setString(6, (getTagValue("연립다세대", element).trim()));
                        pstmt.setFloat(7, Float.parseFloat(getTagValue("전용면적", element).trim()));
                        pstmt.setInt(8, Integer.parseInt(getTagValue("층", element).trim()));
                        pstmt.setString(9, getTagValue("지번", element).trim());
                        pstmt.setString(10, (getTagValue("법정동", element).trim().split(" ")[0]));
                        pstmt.setString(11, (getTagValue("지역코드", element).trim()));

                        pstmt.addBatch();
                    }
                }

            }

            pstmt.executeBatch();
            conn.commit();
            pstmt.clearBatch();

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
                    e3.printStackTrace();
                }
            }
        }
        log.info("연립다세대 실거래 정보 저장 지역코드:={} startDate = {}, endDate = {}", lawd_cd, start, end);

    }

    public void initDetachedHouse(LocalDate startDate, LocalDate endDate, String lawd_cd, String serviceKey) {//단독/다가구 실거래 openApi 정보 db에 저장
        String start = getDateToString(startDate);
        String end = getDateToString(endDate);
        PreparedStatement pstmt = null;
        String deal_ymd;

        try {
            pstmt = conn.prepareStatement("insert into detachedhouse(deal_amount, build_year, deal_year, deal_month, deal_day, house_type, area, region_name,regional_code) values(?, ?, ?, ?, ?, ?, ?, ?, ?)");

            for (; !startDate.isAfter(endDate); startDate = startDate.plusMonths(1)) {

                deal_ymd = getDealYmd(startDate);

                String url = "http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcSHTrade";

                Document documentInfo = DocumentBuilderFactory
                        .newInstance()
                        .newDocumentBuilder()
                        .parse(url + "?serviceKey=" + serviceKey + "&LAWD_CD=" + lawd_cd + "&DEAL_YMD=" + deal_ymd);

                documentInfo.normalize();
                NodeList nodeList = documentInfo.getElementsByTagName("item");
                System.out.println(nodeList.getLength());


                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        pstmt.setInt(1, Integer.parseInt(getTagValue("거래금액", element).replace(",", "").trim()));
                        pstmt.setInt(2, Integer.parseInt(getTagValue("건축년도", element).trim()));
                        pstmt.setInt(3, Integer.parseInt(getTagValue("년", element).trim()));
                        pstmt.setInt(4, Integer.parseInt(getTagValue("월", element).trim()));
                        pstmt.setInt(5, Integer.parseInt(getTagValue("일", element).trim()));
                        pstmt.setString(6, (getTagValue("주택유형", element).trim()));
                        pstmt.setFloat(7, Float.parseFloat(getTagValue("연면적", element).trim()));
                        pstmt.setString(8, (getTagValue("법정동", element).trim().split(" ")[0]));
                        pstmt.setString(9, (getTagValue("지역코드", element).trim()));

                        pstmt.addBatch();

                    }
                }

            }

            pstmt.executeBatch();
            conn.commit();
            pstmt.clearBatch();

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
                    e3.printStackTrace();
                }
            }
        }
        log.info("단독/다가구 실거래 정보 저장 지역코드:={} startDate = {}, endDate = {}", lawd_cd, start, end);

    }

    public  List<Sigungu> getSigungu(){ // 시군구 이름,지역 코드 얻기(대현이형 코드 사용)
        ArrayList<String> input = new ArrayList<>();
        List<Sigungu> sigungus = new ArrayList<>();
        String[] input_arr = new String[0];
        BufferedReader br = null;
        String[] eupmyeondong;
        String[] sigungu;
        String[] sido;

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

                sigungu[i] = arr[0].substring(0, 5) + " " + arr[2];
            }

            sigungu = Arrays.stream(sigungu).distinct().toArray(String[]::new);
            for (String a : sigungu) {
                String[] sgg = a.split(" ");
                if(sgg.length==2)
                    sigungus.add( new Sigungu(sgg[0],sgg[1]));
                else
                    sigungus.add( new Sigungu(sgg[0],sgg[1]+" "+sgg[2]));
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return sigungus;
    }
    public void initHouse() {// 실거래 데이터 저장
        ArrayList<String> input1 = new ArrayList<>();
        ArrayList<String> input2 = new ArrayList<>();
        ArrayList<String> input3 = new ArrayList<>();
        BufferedReader br = null;
        String[] apartment = new String[0];
        String[] rowhouse = new String[0];
        String[] detachedhouse = new String[0];

        PreparedStatement pstmt = null;

        try {
            br = new BufferedReader(new FileReader(new File(path1)));
            String s1;
            while ((s1 = br.readLine()) != null) {
                input1.add(s1);
            }
            br.close();
            apartment = input1.toArray(apartment);

            br = new BufferedReader(new FileReader(new File(path2)));
            String s2;
            while ((s2 = br.readLine()) != null) {
                input2.add(s2);
            }
            br.close();
            rowhouse = input2.toArray(rowhouse);

            br = new BufferedReader(new FileReader(new File(path3)));
            String s3;
            while ((s3 = br.readLine()) != null) {
                input3.add(s3);
            }
            br.close();
            detachedhouse = input3.toArray(detachedhouse);

            pstmt = conn.prepareStatement("insert into apartment(deal_amount, build_year, deal_year, deal_month, deal_day, apartment_name, area, floor, jibun, region_name,regional_code) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (String a : apartment) {
                String[] sd = a.split("\t");

                pstmt.setInt(1, Integer.parseInt(sd[1]));
                pstmt.setInt(2, Integer.parseInt(sd[2]));
                pstmt.setInt(3, Integer.parseInt(sd[3]));
                pstmt.setInt(4, Integer.parseInt(sd[4]));
                pstmt.setInt(5, Integer.parseInt(sd[5]));
                pstmt.setString(6, sd[6]);
                pstmt.setFloat(7, Float.parseFloat(sd[7]));
                pstmt.setInt(8, Integer.parseInt(sd[8]));
                pstmt.setString(9, sd[9]);
                pstmt.setString(10, sd[10]);
                pstmt.setString(11, sd[11]);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            pstmt.clearBatch();

            pstmt = conn.prepareStatement("insert into rowhouse(deal_amount, build_year, deal_year, deal_month, deal_day, rowhouse_name, area, floor, jibun, region_name,regional_code) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (String a : rowhouse) {
                String[] sd = a.split("\t");
                pstmt.setInt(1, Integer.parseInt(sd[1]));
                pstmt.setInt(2, Integer.parseInt(sd[2]));
                pstmt.setInt(3, Integer.parseInt(sd[3]));
                pstmt.setInt(4, Integer.parseInt(sd[4]));
                pstmt.setInt(5, Integer.parseInt(sd[5]));
                pstmt.setString(6, sd[6]);
                pstmt.setFloat(7, Float.parseFloat(sd[7]));
                pstmt.setInt(8, Integer.parseInt(sd[8]));
                pstmt.setString(9, sd[9]);
                pstmt.setString(10, sd[10]);
                pstmt.setString(11, sd[11]);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            pstmt.clearBatch();

            pstmt = conn.prepareStatement("insert into detachedhouse(deal_amount, build_year, deal_year, deal_month, deal_day, house_type, area, region_name,regional_code) values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (String a : detachedhouse) {
                String[] sd = a.split("\t");

                pstmt.setInt(1, Integer.parseInt(sd[1]));
                pstmt.setInt(2, Integer.parseInt(sd[2]));
                pstmt.setInt(3, Integer.parseInt(sd[3]));
                pstmt.setInt(4, Integer.parseInt(sd[4]));
                pstmt.setInt(5, Integer.parseInt(sd[5]));
                pstmt.setString(6, sd[6]);
                pstmt.setFloat(7, Float.parseFloat(sd[7]));
                pstmt.setString(8, sd[8]);
                pstmt.setString(9, sd[9]);

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
                } catch (SQLException e3) {
                }
            }
        }
        log.info("실거래 정보 저장 완료");
    }

    public void initRegion() {// 시도,시군구,법정동 데이터 저장
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
                if(sgg.length==2)
                    pstmt.setString(2, sgg[1]);
                else
                    pstmt.setString(2, sgg[1]+" "+sgg[2]);
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
                } catch (SQLException e3) {
                }
            }
        }
        log.info("시도, 시군구별 지역코드, 법정동 이름 저장 완료");
    }

    private List<Population> getPopulationDTOS() { // 법정동별 인구수 계산
        ArrayList<Population> populationDTOS = new ArrayList<>();

        File file = new File(path); // 법정동 인구 수 파일 경로

        try {
            BufferedReader inFiles = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF8"));

            String line = "";

            while ((line = inFiles.readLine()) != null) {
                //System.out.println(line);
                String[] split = line.split("\t");

                int regionalCode = Integer.parseInt(split[0].trim().substring(0, 5)); //지역코드 5자리만 추출
                String dongName = split[3].trim(); //동이름 추출
                int population = Integer.parseInt(split[4].trim()); //인구수 추출

                Population populationDTO = new Population(String.valueOf(regionalCode), dongName, population);

                if (populationDTOS.size() < 1) // 최초 동 입력시
                    populationDTOS.add(populationDTO);

                Population lastPopulationDTO = populationDTOS.get(populationDTOS.size() - 1);
                if (lastPopulationDTO.getRegionName().equals(populationDTO.getRegionName())) { // 같은 동이 이미 존재하는 경우
                    int curPopulationCnt = lastPopulationDTO.getPopulation() + populationDTO.getPopulation();
                    lastPopulationDTO.setPopulation(curPopulationCnt);
                } else { // 같은 동 존재 안하는 경우
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

    public void initPopulation() {// 읍면동 별 인구수 저장
        List<Population> populationDTOS = getPopulationDTOS();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("insert into population values(?, ?, ?)");
            for (Population population : populationDTOS) {
                pstmt.setString(1, population.getRegionName()); // 읍면동 이름
                pstmt.setString(2, population.getRegionalCode()); // 읍면동 이름
                pstmt.setInt(3, population.getPopulation()); //읍면동별 인구수
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            pstmt.clearBatch();

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
        log.info("법정동별 인구수 저장 완료");
    }
}
