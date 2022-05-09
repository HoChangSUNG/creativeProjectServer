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
        List<AverageData> averageDataList2 = null;
        
        if(month == 1){
            averageDataList2 = averageDataDAO.findByDate(year-1,12);
        }
        else {
            averageDataList2 = averageDataDAO.findByDate(year,month-1);
        }

        List<FluctuationLate> list = new ArrayList<>();
        List<FluctuationLate> result = new ArrayList<>();

        for(int i = 0; i < averageDataList1.size(); i++ ) {
            float currentAverage = averageDataList1.get(i).getAverage();
            float lastAverage = averageDataList2.get(i).getAverage();
            float fluctuationRate;

            if(lastAverage == 0){
                fluctuationRate = 0;
            }
            else {
                fluctuationRate = (currentAverage - lastAverage) / lastAverage * 100;
            }

            list.add(new FluctuationLate(averageDataList1.get(i).getRegionalCode(),fluctuationRate,(int)(currentAverage - lastAverage)));
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

