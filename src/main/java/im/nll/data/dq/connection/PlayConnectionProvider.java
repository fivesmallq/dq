package im.nll.data.dq.connection;

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
        return DB.getConnection();
    }
}
