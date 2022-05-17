package persistence.dao;

import domain.ApartmentIndex;
import domain.AverageData;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.mapper.ApartmentIndexMapper;
import persistence.mapper.AverageDataMapper;

import java.util.List;

@RequiredArgsConstructor
public class ApartmentIndexDAO {
    private final SqlSessionFactory sqlSessionFactory ;

    public List<ApartmentIndex> findApartmentIndex(String region){
        List<ApartmentIndex> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        ApartmentIndexMapper mapper = session.getMapper(ApartmentIndexMapper.class);

        try{
            list=mapper.findApartmentIndex(region);
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
