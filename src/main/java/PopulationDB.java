import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PopulationDB {

    //법정 동 별로 인구수 구해서 리스트에 넣어놓음.
    public static void main(String[] args) {
        ArrayList<PopulationDTO> populationDTOS = new ArrayList<>();

        File file = new File("C:/Users/82105/Desktop/newDongData.txt");

        try {
            BufferedReader inFiles = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF8"));

            String line = "";

            while((line = inFiles.readLine()) != null) {
                //System.out.println(line);
                String[] split = line.split("\t");

                long regionalCode = Long.parseLong(split[0].trim())/100000;
                String dongName = split[3].trim();
                long population = Long.parseLong(split[4].trim());

                PopulationDTO populationDTO = new PopulationDTO(regionalCode, dongName, population);

                if(populationDTOS.size()<1) // 최초 동 입력시
                    populationDTOS.add(populationDTO);

                PopulationDTO lastPopulationDTO = populationDTOS.get(populationDTOS.size() - 1);
                if(lastPopulationDTO.getDongName().equals(populationDTO.getDongName())) { // 같은 동이 이미 존재하는 경우
                    long curPopulationCnt = lastPopulationDTO.getPeopleCnt() + populationDTO.getPeopleCnt();
                    lastPopulationDTO.setPeopleCnt(curPopulationCnt);
                }
                else{ // 같은 동 존재 안하는 경우
                    populationDTOS.add(populationDTO);
                }
            }
            inFiles.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        populationDTOS.forEach(populationDTO -> {
            System.out.println(populationDTO.getRegionalCode()+populationDTO.getDongName()+populationDTO.getPeopleCnt());
        });
    }
}
