package body;

import domain.FluctuationRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FluctuationRateWrapper {
    private List<FluctuationRate> apartmentFluctuationRate;
    private List<FluctuationRate> rowhouseFluctuationRate;
    private List<FluctuationRate> detachedhouseFluctuationRate;
}
