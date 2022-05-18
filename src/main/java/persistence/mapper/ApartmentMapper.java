package persistence.mapper;

import domain.Apartment;
import domain.ApartmentInfo1;
import domain.Sido;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ApartmentMapper {


    @Select("SELECT DISTINCT apartment_name,area FROM APARTMENT WHERE region_name =#{regionName} and regional_code =#{regionalCode}")
    @Results(id="ApartmentList",value={
            @Result(property = "apartmentName",column = "apartment_name"),
            @Result(property = "area",column = "area")
    })
    List<ApartmentInfo1> findApartment(@Param("regionName") String regionName, @Param("regionalCode") String regionalCode);
}

