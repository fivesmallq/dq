/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
package im.nll.data.dq.order;

import im.nll.data.dq.utils.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * 排序类.
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.1
 * @date 2013-1-17下午3:09:25
 */
public class Order {

    private final boolean ascending;
    private final String[] fields;
    private static Map<OrderMode, Boolean> types = new HashMap<>();

    static {
        types.put(OrderMode.ASC, true);
        types.put(OrderMode.DESC, false);
    }

    /**
     * Constructor for Order.
     */
    private Order(OrderMode mode, String... fields) {
        this.fields = fields;
        this.ascending = types.get(mode);
    }

    /**
     * 返回sql语句
     *
     * @return
     */
    public String toSqlString() {
        StringBuilder fragment = new StringBuilder();
        fragment.append(StringUtils.join(fields, ","));
        fragment.append(ascending ? " asc" : " desc");
        return fragment.toString();
    }

    /**
     * 升序
     *
     * @param fields 数据库字段
     * @return Order
     */
    public static Order asc(String... fields) {
        return new Order(OrderMode.ASC, fields);
    }

    /**
     * 动态构建order by
     *
     * @param order  排序方式
     * @param fields 字段
     * @return
     */
    public static Order custom(String order, String... fields) {
        return new Order(OrderMode.custom(order), fields);
    }

    /**
     * 降序
     *
     * @param fields 数据库字段
     * @return Order
     */
    public static Order desc(String... fields) {
        return new Order(OrderMode.DESC, fields);
    }

    @Override
    public String toString() {
        return Arrays.toString(fields) + ' ' + (ascending ? "asc" : "desc");
    }

    public boolean isAscending() {
        return ascending;
    }

    public String[] getFields() {
        return fields;
    }

}
