package persistence.dao;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;

@RequiredArgsConstructor
public class SigunguDAO {

    private final SqlSessionFactory sqlSessionFactory ;

}
