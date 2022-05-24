package service;

import domain.ApartmentForSearch;
import domain.AverageAreaAmoumtApartmentData;
import lombok.RequiredArgsConstructor;
import persistence.dao.AverageAreaAmoumtApartmentDAO;

import java.util.List;

@RequiredArgsConstructor
public class AverageAreaAoumtApartmentService {

    private final AverageAreaAmoumtApartmentDAO averageAreaAmoumtApartmentDAO;

    public List<AverageAreaAmoumtApartmentData> findApartmentAoumtdata(ApartmentForSearch aprtment){
        List<AverageAreaAmoumtApartmentData> DataList = averageAreaAmoumtApartmentDAO.findApartmentByAverageAmount(aprtment);
        return DataList;
    }
}
