package im.nll.data.dq.connection;

import play.Play;
import play.db.DB;

import java.sql.Connection;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 2017/3/13 下午10:47
 */
public class PlayConnectionProvider implements ConnectionProvider {
    @Override
    public Connection get() {
        String logSql = Play.configuration.getProperty("dq.logSql", "false");
        if ("true".equals(logSql)) {
            return new net.sf.log4jdbc.ConnectionSpy(DB.getConnection());
        } else {
            return DB.getConnection();
        }
    }
}
