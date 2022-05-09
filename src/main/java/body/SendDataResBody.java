package body;

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
}
