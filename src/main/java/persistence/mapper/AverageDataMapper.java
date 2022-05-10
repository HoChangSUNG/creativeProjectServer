package persistence.mapper;

import domain.AverageData;
import domain.Sido;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AverageDataMapper {
    @Select("select ifnull(floor(sum(price_sum)/sum(price_cnt)),0) average, regional_code, price_cnt\n" +
            "from\n" +
            "(\n" +
            "select b.regional_code, ifnull(s,0) price_sum, ifnull(c,0) price_cnt from (\n" +
            "              select sum(deal_amount) s,count(deal_amount) c ,regional_code \n" +
            "               from apartment \n" +
            "               where deal_year=#{year} and deal_month=#{month} \n" +
            "               group by regional_code)\n" +
            "               a right outer join sigungu b \n" +
            "               on ( a.regional_code = b.regional_code)\n" +
            "               ) total\n" +
            "group by total.regional_code\n" +
            "order by total.regional_code;"
    )
    @Results(id ="AverageDataSet", value = {
            @Result(property = "average",column = "average"),
            @Result(property = "regionalCode",column = "regional_code"),
            @Result(property = "priceCnt", column = "price_cnt")
    })
    List<AverageData> findApartmentByDate(@Param("year") String year, @Param("month") String month);

    @Select("select ifnull(floor(sum(price_sum)/sum(price_cnt)),0) average, regional_code, price_cnt\n" +
            "from\n" +
            "(\n" +
            "select b.regional_code, ifnull(s,0) price_sum, ifnull(c,0) price_cnt from (\n" +
            "              select sum(deal_amount) s,count(deal_amount) c ,regional_code \n" +
            "               from rowhouse \n" +
            "               where deal_year=#{year} and deal_month=#{month} \n" +
            "               group by regional_code)\n" +
            "               a right outer join sigungu b \n" +
            "               on ( a.regional_code = b.regional_code)\n" +
            "               ) total\n" +
            "group by total.regional_code\n" +
            "order by total.regional_code;"
    )
    @ResultMap("AverageDataSet")
    List<AverageData> findRowhouseByDate(@Param("year") String year, @Param("month") String month);

    @Select("select ifnull(floor(sum(price_sum)/sum(price_cnt)),0) average, regional_code, price_cnt\n" +
            "from\n" +
            "(\n" +
            "select b.regional_code, ifnull(s,0) price_sum, ifnull(c,0) price_cnt from (\n" +
            "              select sum(deal_amount) s,count(deal_amount) c ,regional_code \n" +
            "               from detachedhouse \n" +
            "               where deal_year=#{year} and deal_month=#{month} \n" +
            "               group by regional_code)\n" +
            "               a right outer join sigungu b \n" +
            "               on ( a.regional_code = b.regional_code)\n" +
            "               ) total\n" +
            "group by total.regional_code\n" +
            "order by total.regional_code;"
    )
    @ResultMap("AverageDataSet")
    List<AverageData> findDetachedhouseByDate(@Param("year") String year, @Param("month") String month);
}
