package persistence.mapper;

import domain.AverageData;
import domain.Sido;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AverageDataMapper {
    @Select("select total.region_name dong_name,sigun.region_name sigungu_name, sido.region_name sido_name, ifnull(floor(sum(price_sum)/sum(price_cnt)),0) average, p.population population\n" +
            "from\n" +
            "(\n" +
            "select b.region_name, b.regional_code, ifnull(s,0) price_sum, ifnull(c,0) price_cnt from (\n" +
            "              select sum(deal_amount) s,count(deal_amount) c ,regional_code,region_name \n" +
            "               from apartment \n" +
            "               where deal_year=#{year} and deal_month=#{month} \n" +
            "               group by regional_code, region_name)\n" +
            "               a right outer join eupmyeondong b \n" +
            "               on ( a.regional_code = b.regional_code and a.region_name=b.region_name)\n" +
            "union all\n" +
            "select b.region_name, b.regional_code, ifnull(s,0) price_sum, ifnull(c,0) price_cnt from (\n" +
            "              select sum(deal_amount) s,count(deal_amount) c ,regional_code,region_name \n" +
            "               from rowhouse \n" +
            "               where deal_year=#{year} and deal_month=#{month} \n" +
            "               group by regional_code, region_name)\n" +
            "               a right outer join eupmyeondong b \n" +
            "               on ( a.regional_code = b.regional_code and a.region_name=b.region_name)\n" +
            "union all\n" +
            "select b.region_name, b.regional_code, ifnull(s,0) price_sum, ifnull(c,0) price_cnt from (\n" +
            "              select sum(deal_amount) s,count(deal_amount) c ,regional_code,region_name \n" +
            "               from detachedhouse \n" +
            "               where deal_year=#{year} and deal_month=#{month} \n" +
            "               group by regional_code, region_name)\n" +
            "               a right outer join eupmyeondong b \n" +
            "               on ( a.regional_code = b.regional_code and a.region_name=b.region_name)) total\n" +
            "               join population p on total.region_name = p.region_name and total.regional_code=p.regional_code\n" +
            "               join sigungu sigun on total.regional_code=sigun.regional_code\n" +
            "               join sido on sido.regional_code = left(total.regional_code,2)\n" +
            "group by total.regional_code, total.region_name\n" +
            "order by total.regional_code, total.region_name;;"
    )
    @Results(id="AverageDataSet",value={
            @Result(property = "sidoName",column = "sido_name"),
            @Result(property = "sigunguName",column = "sigungu_name"),
            @Result(property = "dongName",column = "dong_name"),
            @Result(property = "average",column = "average"),
            @Result(property = "population",column = "population")
    })
    List<AverageData> findByDate(@Param("year") String year, @Param("month") String month);
}
