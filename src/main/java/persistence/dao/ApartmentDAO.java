package persistence.dao;

import domain.Apartment;
import domain.ApartmentInfo1;
import domain.ApartmentInfo3;
import domain.AverageData;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.mapper.ApartmentMapper;
import persistence.mapper.AverageDataMapper;

import java.util.List;

@RequiredArgsConstructor
public class ApartmentDAO {

    private final SqlSessionFactory sqlSessionFactory ;


    public List<ApartmentInfo1> findApartment(String regionName, String regionalCode){
        List<ApartmentInfo1> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        ApartmentMapper mapper = session.getMapper(ApartmentMapper.class);

        try{
            list=mapper.findApartment(regionName,regionalCode);
            session.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return list;
    }

    public List<ApartmentInfo3> findApartmentInfo(float area, String apartmentName, String regionalCode, String regionName){
        List<ApartmentInfo3> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        ApartmentMapper mapper = session.getMapper(ApartmentMapper.class);

        try{
            list=mapper.findApartmentInfo(area,apartmentName,regionalCode,regionName);
            session.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return list;
    }
}
