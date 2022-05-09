package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AverageData implements Serializable {

    private String sidoName;
    private String sigunguName;
    private String dongName;
    private int average;
    private int population;


}
