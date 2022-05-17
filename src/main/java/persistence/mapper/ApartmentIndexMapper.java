package persistence.mapper;

import domain.ApartmentIndex;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ApartmentIndexMapper {

    @Select("select * from apartmentindex where region = #{region} order by date")
    @Results(id = "apartmentIndex", value = {
            @Result(property = "region", column = "region"),
            @Result(property = "date", column = "date"),
            @Result(property = "index", column = "index"),
            @Result(property = "fluctuation", column = "fluctuation")
    })
    List<ApartmentIndex> findApartmentIndex(@Param("region") String region);

}
