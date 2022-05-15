package persistence.mapper;

import domain.EupMyeonDong;
import domain.Sigungu;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface EupMyeonDongMapper {

    @Select("SELECT * FROM eupmyeondong where regional_code = #{sigunguCode} order by region_name")
    @Results(id="EupMyeonDongJoinSet",value={
            @Result(property = "regionCode",column = "regional_code"),
            @Result(property = "regionName",column = "region_name")
    })
    List<EupMyeonDong> findEupMyeonDongJoin(@Param("sigunguCode")String sigunguCode);
}
