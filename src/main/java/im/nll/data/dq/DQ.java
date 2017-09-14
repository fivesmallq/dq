package im.nll.data.dq;

import im.nll.data.dq.connection.ConnectionProvider;
import im.nll.data.dq.connection.PlayConnectionProvider;
import im.nll.data.dq.mapper.BeanMapper;
import im.nll.data.dq.mapper.ObjectColumnMapper;
import im.nll.data.dq.namedparam.MapSqlParameterSource;
import im.nll.data.dq.namedparam.NamedParameterUtils;
import im.nll.data.dq.namedparam.ParsedSql;
import im.nll.data.dq.utils.Validate;
import org.skife.jdbi.v2.*;
import org.skife.jdbi.v2.util.LongColumnMapper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
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

    public static <T> List<T> namedQuery(String sql, Class<T> clazz, Map<String, Object> params) {
        return mapNamedQuery(sql, clazz, params).list();
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

    public static Long namedCount(String sql, Map<String, Object> params) {
        return namedGet(sql, Long.class, params);
    }


    public static <T> T get(String sql, Class<T> clazz, Object... params) {
        validateParameter(params);
        Handle h = getHandle();
        Query<T> query = mapQuery(sql, clazz, h);
        int i = 0;
        for (Object param : params) {
            query.bind(i, param);
            i++;
        }
        T result = query.first();
        return result;
    }


    public static <T> T namedGet(String sql, Class<T> clazz, Map<String, Object> params) {
        return mapNamedQuery(sql, clazz, params).first();
    }


    public static Long count(SQLBuilder builder) {
        return count(builder.toCountSQL(), builder.getParams());
    }

    public static void execute(String sql, Object... params) {
        Handle h = getHandle();
        h.execute(sql, params);
    }

    public static int namedExecute(String sql, Map<String, Object> params) {
        return namedUpdate(sql, params);
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

    public static int namedUpdate(String sql, Map<String, Object> params) {
        Handle h = getHandle();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValues(params);
        ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sql);
        String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, parameters);
        Object[] paramsArray = NamedParameterUtils.buildValueArray(parsedSql, parameters, null);
        Update update = h.createStatement(sqlToUse);
        mapNamedParams(paramsArray, update);
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

    public static int[] namedBatch(String sql, List<Map<String, Object>> params) {
        Handle h = getHandle();
        PreparedBatch batch = h.prepareBatch(sql);
        for (Map param : params) {
            batch.add(param);
        }
        return batch.execute();
    }

    private static void validateParameter(Object[] params) {
        if (params.length > 0) {
            Validate.isFalse(params[0] instanceof Map, "maybe you can use named method! ");
        }
    }

    private static <T> Query<T> mapNamedQuery(String sql, Class<T> clazz, Map<String, Object> params) {
        Handle h = getHandle();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValues(params);
        ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sql);
        String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, parameters);
        Object[] paramsArray = NamedParameterUtils.buildValueArray(parsedSql, parameters, null);
        Query<T> query = mapQuery(sqlToUse, clazz, h);
        mapNamedParams(paramsArray, query);
        return query;
    }

    private static <T extends SQLStatement<T>> void mapNamedParams(Object[] paramsArray, SQLStatement<T> query) {
        int i = 0;
        for (Object param : paramsArray) {
            if (param instanceof Collection) {
                Collection<?> entries = (Collection<?>) param;
                for (Object entry : entries) {
                    if (entry instanceof Object[]) {
                        Object[] valueArray = ((Object[]) entry);
                        for (Object argValue : valueArray) {
                            query.bind(i, argValue);
                            i++;
                        }
                    } else {
                        query.bind(i, entry);
                        i++;
                    }
                }
            } else {
                query.bind(i, param);
                i++;
            }
        }
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
