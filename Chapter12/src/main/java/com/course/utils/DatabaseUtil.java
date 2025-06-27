package com.course.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class DatabaseUtil {
    public static SqlSession getSqlSession() throws IOException {
        //根据配置文件连接数据库
        Reader reader = Resources.getResourceAsReader("databaseConfig.xml");
        //建立起数据库连接
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);
        //得到连接,sqlSession就是能够执行配置文件中的sql语句
        SqlSession sqlSession = factory.openSession();
        return sqlSession;
    }
}
