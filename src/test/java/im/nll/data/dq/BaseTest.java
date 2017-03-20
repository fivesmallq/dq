package im.nll.data.dq;

import im.nll.data.dq.connection.ConnectionProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 2017/3/18 下午1:51
 */
public class BaseTest {
    static {
        try {
            Class.forName("org.h2.Driver");
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ConnectionProvider getH2Provider(){
        return new JdbcConnectionProvider();
    }
    public ConnectionProvider getMySQLProvider(){
        return new MySQLJdbcConnectionProvider();
    }

    static class JdbcConnectionProvider implements ConnectionProvider {

        @Override
        public Connection get() {
            try {
                Connection conn = DriverManager.
                        getConnection("jdbc:h2:mem:test");
                // return conn;
                return new net.sf.log4jdbc.ConnectionSpy(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    static class MySQLJdbcConnectionProvider implements ConnectionProvider {

        @Override
        public Connection get() {
            try {
                Connection conn =
                        DriverManager.getConnection("jdbc:mysql://localhost/dq?user=root&password=root&autoReconnect=true&connectionCollation=utf8mb4_general_ci&characterEncoding=UTF-8&useSSL=false");
                // return conn;
                return new net.sf.log4jdbc.ConnectionSpy(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
