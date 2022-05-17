package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentIndex implements Serializable {

    private String region;
    private Date date;
    private float index;
    private float fluctuation;
}
