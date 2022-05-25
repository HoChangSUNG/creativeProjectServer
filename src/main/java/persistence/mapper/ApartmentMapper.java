package persistence.mapper;

import domain.Apartment;
import domain.ApartmentInfo1;
import domain.ApartmentInfo3;
import domain.Sido;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ApartmentMapper {


    @Select("SELECT DISTINCT apartment_name,area FROM APARTMENT WHERE region_name =#{regionName} and regional_code =#{regionalCode} ORDER BY apartment_name,area")
    @Results(id="ApartmentList1",value={
            @Result(property = "apartmentName",column = "apartment_name"),
            @Result(property = "area",column = "area")
    })
    List<ApartmentInfo1> findApartment(@Param("regionName") String regionName, @Param("regionalCode") String regionalCode);

    @Select("SELECT deal_year,deal_month,apartment_name,area,avg(deal_amount),count(deal_amount) from apartment where truncate(area,0) = #{area} and apartment_name =#{apartmentName}  and regional_code = #{regionalCode} and region_name  = #{regionName} group by apartment_name,regional_code,area,deal_year,deal_month;")
    @Results(id="ApartmentList2",value={
            @Result(property = "dealYear",column = "deal_year"),
            @Result(property = "dealMonth",column = "deal_month"),
            @Result(property = "apartmentName",column = "apartment_name"),
            @Result(property = "area",column = "area"),
            @Result(property = "averagePrice",column = "avg(deal_amount)"),
            @Result(property = "tradingVolume",column = "count(deal_amount)")

    })
    List<ApartmentInfo3> findApartmentInfo(@Param("area") float area, @Param("apartmentName") String apartmentName, @Param("regionalCode") String regionalCode, @Param("regionName") String regionName);
}

