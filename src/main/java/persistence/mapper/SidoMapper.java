package persistence.mapper;

import domain.Sido;
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
}

