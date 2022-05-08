package persistence.dao;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;

@RequiredArgsConstructor
public class DetachedHouseDAO {

    private final SqlSessionFactory sqlSessionFactory ;

}
