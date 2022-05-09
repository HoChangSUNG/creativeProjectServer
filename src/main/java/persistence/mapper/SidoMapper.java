package persistence.mapper;

import domain.Sido;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SidoMapper {

    @Select("SELECT * FROM SIDO order by region_name asc")
    @Results(id="SidoSet",value={
            @Result(property = "regionalCode",column = "regional_code"),
            @Result(property = "regionName",column = "region_name"),

    })
    List<Sido> findAllSido();

    @Select("SELECT region_name FROM sigungu WHERE regional_code=#{regionalCode}")
    String findSigunguName(@Param("regionalCode") String regionalCode);

    @Select("SELECT region_name FROM sido WHERE regional_code=#{regionalCode}")
    String findSidoName(@Param("regionalCode") String regionalCode);

    @Select("SELECT sum(population) FROM population WHERE regional_code=#{regionalCode} GROUP BY regional_code")
    int findPopulation(@Param("regionalCode") String regionalCode);
}

