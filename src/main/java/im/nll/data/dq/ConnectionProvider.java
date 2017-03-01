package im.nll.data.dq;

import java.sql.Connection;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 2017/3/1 下午3:21
 */
public interface ConnectionProvider {
    Connection get();
}
