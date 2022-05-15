package persistence.mapper;

import domain.Sido;
import domain.Sigungu;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface SigunguMapper {

    @Select("SELECT * FROM SIGUNGU where left(regional_code,2) = #{sidoCode} order by region_name")
    @Results(id="SigunguJoinSet",value={
            @Result(property = "regionalCode",column = "regional_code"),
            @Result(property = "regionName",column = "region_name"),
            @Result(property = "eupMyeonDongList",column = "regional_code", many = @Many(select = "persistence.mapper.EupMyeonDongMapper.findEupMyeonDongJoin"))

    })
    List<Sigungu> findSigunguJoin(@Param("sidoCode")String sidoCode);
}
