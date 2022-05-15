package persistence.dao;

import domain.Sido;
import domain.Sigungu;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.mapper.SidoMapper;
import persistence.mapper.SigunguMapper;

import java.util.List;

@RequiredArgsConstructor
public class SidoDAO {
    private final SqlSessionFactory sqlSessionFactory ;

    public List<Sido> findAllSido(){
        List<Sido> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        SidoMapper mapper = session.getMapper(SidoMapper.class);

        try{
            list=mapper.findAllSido();
            session.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return list;
    }

    public List<Sido> findSelectRegionList(){ // 지역 선택 list
        List<Sido> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        SidoMapper mapper = session.getMapper(SidoMapper.class);
        try{
            list=mapper.findJoinSido();
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
