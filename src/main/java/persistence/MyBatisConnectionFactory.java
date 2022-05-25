package persistence;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import persistence.mapper.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

public class MyBatisConnectionFactory {

    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            String resource = "config.xml";//resources에 있는 데이터 베이스 설정 파일
            Reader reader = Resources.getResourceAsReader(resource);

            if (sqlSessionFactory == null) {
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
                Class[] mappers={
                        SidoMapper.class,
                        AverageDataMapper.class,
                        SigunguMapper.class,
                        EupMyeonDongMapper.class,
                        AverageAreaAmoumtApartmentMapper.class,
                        ApartmentMapper.class,
                        ApartmentIndexMapper.class
                        //mapper들  추가
                };
                for(Class mapper: mappers){
                    sqlSessionFactory.getConfiguration().addMapper(mapper);
                }
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}