package persistence.mapper;

import domain.AverageAreaAmoumtApartmentData;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface AverageAreaAmoumtApartmentMapper {
        @Select("select avg(a) avg,count(a) cnt,deal_year ,deal_month\n"+
                "from (SELECT deal_year,deal_month, deal_amount/area a ,region_name,regional_code\n"+
                        "FROM apartment\n"+
                        "where regional_code=#{regionalCode} and region_name=#{regionName} and deal_year>2020 \n"+
                        "order by deal_month) temp\n"+
                "group by deal_month,region_name,regional_code\n"+
                "order by deal_year,deal_month;"
        )
        @Results(id ="AverageAreaAmoumtApartmentData", value = {
                @Result(property = "dealMonth",column = "deal_month"),
                @Result(property = "dealYear",column = "deal_year"),
                @Result(property = "averageAreaAmoumt",column = "avg"),
                @Result(property = "averageCnt",column = "cnt"),

        })
        List<AverageAreaAmoumtApartmentData> findApartmentAreaAmount(@Param("regionalCode") String regionalCode,
                                                                     @Param("regionName") String regionName);
}
