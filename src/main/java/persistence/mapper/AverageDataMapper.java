package persistence.mapper;

import domain.AverageData;
import domain.Sido;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AverageDataMapper {
    @Select("select total.region_name, total.regional_code, ifnull(floor(sum(price_sum)/sum(price_cnt)),0) average, p.population population" +
            " from" +
            " (" +
            "select b.region_name, b.regional_code, ifnull(s,0) price_sum, ifnull(c,0) price_cnt" +
            " from (" +
            "select sum(deal_amount) s,count(deal_amount) c ,regional_code,region_name" +
            " from apartment" +
            " where deal_year=#{year} and deal_month=#{month}" +
            " group by regional_code, region_name) a " +
            " right outer join eupmyeondong b " +
            " on ( a.regional_code = b.regional_code and a.region_name=b.region_name)" +
            " union all" +
            " select b.region_name, b.regional_code, ifnull(s,0) price_sum, ifnull(c,0) price_cnt" +
            " from (" +
            " select sum(deal_amount) s,count(deal_amount) c ,regional_code,region_name " +
            " from rowhouse " +
            " where deal_year=#{year} and deal_month=#{month}" +
            " group by regional_code, region_name) a" +
            " right outer join eupmyeondong b " +
            " on ( a.regional_code = b.regional_code and a.region_name=b.region_name)" +
            " union all" +
            " select b.region_name, b.regional_code, ifnull(s,0) price_sum, ifnull(c,0) price_cnt" +
            " from (" +
            " select sum(deal_amount) s,count(deal_amount) c ,regional_code,region_name" +
            " from detachedhouse" +
            " where deal_year=#{year} and deal_month=#{month}" +
            " group by regional_code, region_name) a" +
            " right outer join eupmyeondong b" +
            " on ( a.regional_code = b.regional_code and a.region_name=b.region_name)) total" +
            " join population p on total.region_name = p.region_name and total.regional_code=p.regional_code" +
            " group by regional_code,region_name"+
            " order by regional_code, region_name;"
    )
    @Results(id="AverageDataSet",value={
            @Result(property = "regionalCode",column = "regional_code"),
            @Result(property = "regionName",column = "region_name"),
            @Result(property = "average",column = "average"),
            @Result(property = "population",column = "population")
    })
    List<AverageData> findByDate(@Param("year") String year, @Param("month") String month);
}


