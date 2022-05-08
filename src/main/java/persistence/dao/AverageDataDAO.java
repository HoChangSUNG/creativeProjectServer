package persistence.dao;

import domain.AverageData;
import domain.Sido;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.mapper.AverageDataMapper;
import persistence.mapper.SidoMapper;

import java.util.List;
@RequiredArgsConstructor
public class AverageDataDAO {
    private final SqlSessionFactory sqlSessionFactory ;

    public List<AverageData> findByDate(int year,int month){
        List<AverageData> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        AverageDataMapper mapper = session.getMapper(AverageDataMapper.class);

        try{
            list=mapper.findByDate(String.valueOf(year),String.valueOf(month));
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
