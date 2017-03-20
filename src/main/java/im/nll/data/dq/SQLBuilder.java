package im.nll.data.dq;


import im.nll.data.dq.criterion.Criterion;
import im.nll.data.dq.criterion.Rs;
import im.nll.data.dq.exceptions.QueryException;
import im.nll.data.dq.order.Order;
import im.nll.data.dq.utils.ArrayUtils;
import im.nll.data.dq.utils.StringUtils;
import im.nll.data.dq.utils.Validate;

import javax.persistence.Entity;

/**
 * 简单的构建sql工具.
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.3
 * @date 2013-4-22下午6:05:31
 */
public class SQLBuilder {
    private Class tableClass;

    public static SQLBuilder from(String table, Class mapClass) {
        return new SQLBuilder(table, mapClass);
    }

    public static SQLBuilder of(String sql) {
        return new SQLBuilder(sql);
    }

    public static SQLBuilder from(Class clazz) {
        return new SQLBuilder(clazz);
    }

    private Object[] params = new Object[0];
    private StringBuilder baseBuilder = new StringBuilder();
    private StringBuilder whereBuilder = new StringBuilder();
    private StringBuilder orderBuilder = new StringBuilder();
    private StringBuilder limitBuilder = new StringBuilder();
    private String table;

    private SQLBuilder(String sql) {
        Validate.notEmpty(sql, "sql can not be empty! ");
        this.baseBuilder = new StringBuilder(sql);
    }

    private SQLBuilder(String table, Class mapClass) {
        Validate.notEmpty(table, "table can not be empty! ");
        this.table = table;
        this.tableClass = mapClass;
    }


    private SQLBuilder(Class<?> clazz) {
        Entity entityAnnotation = clazz.getAnnotation(Entity.class);
        if (entityAnnotation == null || StringUtils.isNullOrEmpty(entityAnnotation.name())) {
            throw new QueryException(clazz + " do not with Entity annotation, please set javax.persistence.Entity with name");
        }
        this.table = entityAnnotation.name();
        this.tableClass = clazz;
    }

    public Class getTableClass() {
        return this.tableClass;
    }

    /**
     * 添加条件
     *
     * @param criterion
     * @return
     */
    public SQLBuilder add(Criterion criterion) {
        if (whereBuilder.length() > 0) {
            whereBuilder.append(" and ");
        }
        whereBuilder.append(criterion.toSqlString());
        params = ArrayUtils.addAll(params, criterion.getParameters());
        return this;
    }

    /**
     * 动态添加and条件
     * <p>
     * 如果条件的值为空,就不会添加这个条件
     *
     * @param criterions 多个条件
     * @return
     */
    public SQLBuilder dyAnd(Criterion... criterions) {
        return add(Rs.dynamicAnd(criterions));
    }

    /**
     * 动态添加or条件
     * <p>
     * 如果条件的值为空,就不会添加这个条件
     *
     * @param criterions 多个条件
     * @return
     */
    public SQLBuilder dyOr(Criterion... criterions) {
        return add(Rs.dynamicOr(criterions));
    }

    /**
     * 添加排序
     *
     * @param order
     * @return
     */
    public SQLBuilder addOrder(Order order) {
        orderBuilder.append("order by ").append(order.toSqlString());
        return this;
    }

    /**
     * 分页，会safe处理offset和limit.
     *
     * @param offset
     * @param limit
     * @return
     */
    public SQLBuilder limit(int offset, int limit) {
        limitBuilder = new StringBuilder().append(" limit ").append(safeOffset(offset)).append(',').append(safeLimit(limit));
        return this;
    }

    public String toStatementSQL() {
        StringBuilder finalSql = new StringBuilder(baseBuilder);
        if (whereBuilder.length() > 0) {
            finalSql.append(" where ").append(whereBuilder).append(' ');
        }
        if (orderBuilder.length() > 0) {
            finalSql.append(' ').append(orderBuilder).append(' ');
        }
        if (limitBuilder.length() > 0) {
            finalSql.append(' ').append(limitBuilder).append(' ');
        }
        return finalSql.toString();
    }

    public String toSelectSQL() {
        StringBuilder finalSql = new StringBuilder("select * from ").append(table);
        if (whereBuilder.length() > 0) {
            finalSql.append(" where ").append(whereBuilder).append(' ');
        }
        if (orderBuilder.length() > 0) {
            finalSql.append(' ').append(orderBuilder).append(' ');
        }
        if (limitBuilder.length() > 0) {
            finalSql.append(limitBuilder);
        }
        return finalSql.toString();
    }

    public String toCountSQL() {
        StringBuilder finalSql = new StringBuilder("select count(1) from ").append(table);
        if (whereBuilder.length() > 0) {
            finalSql.append(" where ").append(whereBuilder).append(' ');
        }
        return finalSql.toString();
    }

    /**
     * 返回参数
     *
     * @return
     */
    public Object[] getParams() {
        return params;
    }

    private static final int MAX_OFFSET = 10000;
    private static final int MAX_LIMIT = 10000;
    private static final int DEFAULT_LIMIT = 10;

    public static int safeLimit(int limit) {
        limit = limit <= 0 ? DEFAULT_LIMIT : limit;
        return limit > MAX_LIMIT ? MAX_LIMIT : limit;
    }

    public static int safeOffset(int offset) {
        offset = offset < 0 ? 0 : offset;
        return offset > MAX_OFFSET ? MAX_OFFSET : offset;
    }
}
