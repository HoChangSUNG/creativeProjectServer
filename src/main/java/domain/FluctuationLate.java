package domain;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FluctuationLate implements Serializable {
    private String sidoName;
    private String sigunguName;
    private String dongName;
    private float fluctuationLateData;
}
