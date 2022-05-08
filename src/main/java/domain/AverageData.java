package domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
public class AverageData {

    private String regionName;
    private String regionalCode;
    private int average;
    private int population;

}
