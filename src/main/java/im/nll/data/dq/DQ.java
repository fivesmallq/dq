package im.nll.data.dq;

import im.nll.data.dq.utils.Validate;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.NameTokenizers;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;
import org.skife.jdbi.v2.util.LongColumnMapper;
import play.db.DB;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 2017/2/22 下午8:01
 */
public class DQ {

    private static Handle getHandle() {
        Connection connection = DB.getConnection();
        Validate.notNull(connection, "you must set connection first! ");
        return DBI.open(connection);
    }

    public static <T> List<T> query(SQLBuilder builder) {
        return query(builder.toSelectSQL(), builder.getParams());
    }

    public static <T> List<T> query(String sql, Object... params) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
        Handle h = getHandle();
        Query<Map<String, Object>> query = h.createQuery(sql);
        int i = 0;
        for (Object param : params) {
            query.bind(i, param);
            i++;
        }
        return modelMapper.map(query.list(), new TypeToken<List<T>>() {
        }.getType());
    }

    public static <T> List<T> bindQuery(String sql, Map<String, Object> params) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
        Handle h = getHandle();
        Query<Map<String, Object>> query = h.createQuery(sql);
        for (Map.Entry<String, Object> param : params.entrySet()) {
            query.bind(param.getKey(), param.getValue());
        }
        return modelMapper.map(query.list(), new TypeToken<List<T>>() {
        }.getType());
    }

    public static Long count(String sql, Object... params) {
        Handle h = getHandle();
        Query<Long> query = h.createQuery(sql).map(LongColumnMapper.PRIMITIVE);
        int i = 0;
        for (Object param : params) {
            query.bind(i, param);
            i++;
        }
        return query.first();
    }

    public static Long count(SQLBuilder builder) {
        return count(builder.toCountSQL(), builder.getParams());
    }

    public static void batch(String sql, List<Map<String, Object>> params) {
        Handle h = getHandle();
        Query<Long> query = h.createQuery(sql).map(LongColumnMapper.PRIMITIVE);
        int i = 0;
        for (Object param : params) {
            query.bind(i, param);
            i++;
        }
        //TODO
    }


}
