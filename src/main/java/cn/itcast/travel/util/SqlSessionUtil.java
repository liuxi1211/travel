package cn.itcast.travel.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * SqlSessionFactory工具类
 * 创建SqlSessionFacory
 */
public class SqlSessionUtil {
    private static SqlSessionFactory sqlSessionFactory;

    private SqlSessionUtil() {
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {
            synchronized (SqlSessionUtil.class) {
                if (sqlSessionFactory == null) {
                    try {
                        InputStream resourceAsStream = Resources.getResourceAsStream("mybatis.config.xml");
                        sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sqlSessionFactory;
    }


    public static SqlSession openSession() {
        return getSqlSessionFactory().openSession();
    }
}
