package body;

import domain.Apartment;
import domain.ApartmentInfo1;
import domain.AverageData;
import domain.FluctuationRate;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SendDataResBody implements Serializable {
    private List<AverageData> averageDataList;
    private  List<FluctuationRate> fluctuationLateList;
    private List<ApartmentInfo1> apartmentList;
}
