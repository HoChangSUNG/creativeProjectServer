import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PopulationDTO {

    private long regionalCode;
    private String dongName;
    private long peopleCnt;

}
