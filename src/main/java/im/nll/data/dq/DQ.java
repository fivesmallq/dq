package im.nll.data.dq;

import im.nll.data.dq.connection.ConnectionProvider;
import im.nll.data.dq.connection.PlayConnectionProvider;
import im.nll.data.dq.mapper.BeanMapper;
import im.nll.data.dq.mapper.ObjectColumnMapper;
import im.nll.data.dq.utils.Validate;
import org.skife.jdbi.v2.*;
import org.skife.jdbi.v2.util.LongColumnMapper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 2017/2/22 下午8:01
 */
public class DQ {
    private static ConnectionProvider connectionProvider;

    public static void with(ConnectionProvider provider) {
        connectionProvider = provider;
    }

    private static Handle getHandle() {
        if (connectionProvider == null) {
            connectionProvider = new PlayConnectionProvider();
        }
        Connection connection = connectionProvider.get();
        Validate.notNull(connection, "you must set connection first! ");
        return DBI.open(connection);
    }

    public static <T> List<T> query(SQLBuilder builder) {
        return query(builder.toSelectSQL(), builder.getTableClass(), builder.getParams());
    }

    public static <T> List<T> query(String sql, Class<T> clazz, Object... params) {
        validateParameter(params);
        Handle h = getHandle();
        Query<T> query = h.createQuery(sql).map(new BeanMapper<>(clazz));
        int i = 0;
        for (Object param : params) {
            query.bind(i, param);
            i++;
        }
        return query.list();
    }

    public static <T> List<T> bindQuery(String sql, Class<T> clazz, Map<String, Object> params) {
        Handle h = getHandle();
        Query<T> query = mapQuery(sql, clazz, h);
        for (Map.Entry<String, Object> param : params.entrySet()) {
            query.bind(param.getKey(), param.getValue());
        }
        return query.list();
    }

    public static Long count(String sql, Object... params) {
        validateParameter(params);
        Handle h = getHandle();
        Query<Long> query = h.createQuery(sql).map(LongColumnMapper.PRIMITIVE);
        int i = 0;
        for (Object param : params) {
            query.bind(i, param);
            i++;
        }
        return query.first();
    }

    public static Long bindCount(String sql, Map<String, Object> params) {
        Handle h = getHandle();
        Query<Long> query = h.createQuery(sql).map(LongColumnMapper.PRIMITIVE);
        for (Map.Entry<String, Object> param : params.entrySet()) {
            query.bind(param.getKey(), param.getValue());
        }
        return query.first();
    }


    public static <T> T get(String sql, Class<T> clazz, Object... params) {
        validateParameter(params);
        Handle h = getHandle();
        Query<T> query;
        query = mapQuery(sql, clazz, h);
        int i = 0;
        for (Object param : params) {
            query.bind(i, param);
            i++;
        }
        T result = query.first();
        return result;
    }


    public static <T> T bindGet(String sql, Class<T> clazz, Map<String, Object> params) {
        Handle h = getHandle();
        Query<T> query = mapQuery(sql, clazz, h);
        for (Map.Entry<String, Object> param : params.entrySet()) {
            query.bind(param.getKey(), param.getValue());
        }
        return query.first();
    }

    private static <T> Query<T> mapQuery(String sql, Class<T> clazz, Handle h) {
        Query<T> query;
        if (isPrimitive(clazz)) {
            query = h.createQuery(sql).map(new ObjectColumnMapper(clazz));
        } else {
            query = h.createQuery(sql).map(new BeanMapper<>(clazz));
        }
        return query;
    }

    public static Long count(SQLBuilder builder) {
        return count(builder.toCountSQL(), builder.getParams());
    }

    public static void execute(String sql, Object... params) {
        Handle h = getHandle();
        h.execute(sql, params);
    }

    public static int bindExecute(String sql, Map<String, Object> params) {
        Handle h = getHandle();
        return h.createStatement(sql).bindFromMap(params).execute();
    }


    public static int update(String sql, Object... params) {
        Handle h = getHandle();
        Update update = h.createStatement(sql);
        int i = 0;
        for (Object param : params) {
            update.bind(i, param);
            i++;
        }
        return update.execute();
    }

    public static int bindUpdate(String sql, Map<String, Object> params) {
        Handle h = getHandle();
        Update update = h.createStatement(sql);
        update.bindFromMap(params);
        return update.execute();
    }

    public static int[] batch(String sql, List<Object[]> params) {
        Handle h = getHandle();
        PreparedBatch batch = h.prepareBatch(sql);
        for (Object[] paramArray : params) {
            batch.add(paramArray);
        }
        return batch.execute();
    }

    public static int[] bindBatch(String sql, List<Map<String, Object>> params) {
        Handle h = getHandle();
        PreparedBatch batch = h.prepareBatch(sql);
        for (Map param : params) {
            batch.add(param);
        }
        return batch.execute();
    }

    private static void validateParameter(Object[] params) {
        if (params.length > 0) {
            Validate.isFalse(params[0] instanceof Map, "maybe you can use bind method! ");
        }
    }

    /************************* 后添加的方法 ***********************************/

    private final static List<Class<?>> PrimitiveClasses = new ArrayList<Class<?>>() {
        private static final long serialVersionUID = 1L;

        {
            add(Long.class);
            add(Integer.class);
            add(String.class);
            add(java.util.Date.class);
            add(java.sql.Date.class);
            add(java.sql.Timestamp.class);
        }
    };

    /**
     * 判断一个类是否是原生类型
     *
     * @param cls
     * @return
     */
    public final static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive() || PrimitiveClasses.contains(cls);
    }

}
