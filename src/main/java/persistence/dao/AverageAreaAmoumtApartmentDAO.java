package persistence.dao;

import domain.ApartmentForSearch;
import domain.AverageAreaAmoumtApartmentData;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.mapper.AverageAreaAmoumtApartmentMapper;

import java.util.List;

@RequiredArgsConstructor
public class AverageAreaAmoumtApartmentDAO {

    private final SqlSessionFactory sqlSessionFactory ;

    public List<AverageAreaAmoumtApartmentData> findApartmentByAverageAmount(ApartmentForSearch apartment){
        List<AverageAreaAmoumtApartmentData> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        AverageAreaAmoumtApartmentMapper mapper = session.getMapper(AverageAreaAmoumtApartmentMapper.class);

        try{
            list=mapper.findApartmentAreaAmount(apartment.getRegionalCode(), apartment.getRegionName());
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
