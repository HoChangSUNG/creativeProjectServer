package persistence.dao;

import domain.Population;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.mapper.SidoMapper;

@RequiredArgsConstructor
public class SigunguDAO {

    private final SqlSessionFactory sqlSessionFactory ;

    public Population findSigungu(String regionalCode) {
        SqlSession session = sqlSessionFactory.openSession();
        SidoMapper mapper = session.getMapper(SidoMapper.class);

        Population population = null;

        try{
            String sigungu;
            sigungu = mapper.findSidoName(regionalCode.substring(0,2)) + " ";
            sigungu += mapper.findSigunguName(regionalCode);
            population = new Population(regionalCode, sigungu, mapper.findPopulation(regionalCode));
            session.commit();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return population;
    }
}
