package itschool.bluemarble.dao.base;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class CommomDao {
    HikariDataSource ds = new HikariDataSource();
    // DB 접속 정보
    final static private String DB_DRIVER = "org.postgresql.Driver";
    final static private String DB_URL = "jdbc:postgresql://127.0.0.1:5432/postgres";
    final static private String DB_USER = "postgres";
    final static private String DB_PASSWORD = "1234";

    //초기화 블록
    {
        ds.setDriverClassName(DB_DRIVER);
        ds.setJdbcUrl(DB_URL);
        ds.setUsername(DB_USER);
        ds.setPassword(DB_PASSWORD);
        // 아래 PoolSize는 기존 JDB에서는 제공하지 않음
        ds.setMaximumPoolSize(5);
    }

    // 1.Connection 모듈
    protected Connection connect(){
        try{
            Connection connection = ds.getConnection();
            System.out.println("DB 연결 성공"); // 출력문 Logger로 대체 예정
            return connection;
        }
        catch (SQLException e){
            throw new RuntimeException("DB연결 실패", e);
        }
    }
}
