package persistence.dao;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;

@RequiredArgsConstructor
public class PopulationDAO {

    private final SqlSessionFactory sqlSessionFactory ;

}
