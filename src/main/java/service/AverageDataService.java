package service;

import domain.AverageData;
import domain.FluctuationLate;
import lombok.RequiredArgsConstructor;
import persistence.dao.AverageDataDAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class AverageDataService{

    private final AverageDataDAO averageDataDAO;

    public List<FluctuationLate> findFluctuationLateByDate(int year, int month){
        List<AverageData> averageDataList1 = averageDataDAO.findByDate(year,month);
        List<AverageData> averageDataList2 = averageDataDAO.findByDate(year,month-1);

        if(month == 1){
            averageDataList2 = averageDataDAO.findByDate(year-1,12);
        }

        List<FluctuationLate> list = new ArrayList<>();
        List<FluctuationLate> result = new ArrayList<>();

        for(int i = 0; i < averageDataList1.size(); i++ ) {
            float num1 = averageDataList1.get(i).getAverage();
            float num2 = averageDataList2.get(i).getAverage();
            float num3;

            if(num2 == 0){
                num3 = 0;
            }
            else {
                num3 = (num1 - num2) / num2 * 100;
            }

            list.add(new FluctuationLate(averageDataList1.get(i).getRegionalCode(),num3,(int)(num1 - num2)));
            Collections.sort(list);
        }

        for(int i=0;i<20;i++){ //상위 20개만 리턴
            FluctuationLate fluctuationLate = list.get(i);

            //여기에 지역 이름이랑 인구수 받는거
            fluctuationLate.setRegionName(String.valueOf(i)+"지역");
            fluctuationLate.setPopulation(i);
            result.add(list.get(i));

        }
        return result;
    }
}

