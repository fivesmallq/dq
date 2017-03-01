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
package im.nll.data.dq.criterion;


import im.nll.data.dq.exceptions.QueryException;

/**
 * sql表达式.
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.1
 * @date 2013-1-19下午12:38:28
 */
public class SQLCriterion implements Criterion {

    private final String sql;
    private final Object[] typedValues;

    protected SQLCriterion(String sql, Object[] values) {
        this.sql = sql;
        this.typedValues = values;
    }

    @Override
    public String toSqlString() throws QueryException {
        // TODO sql别名处理
        return sql;
    }

    @Override
    public Object[] getParameters() throws QueryException {
        return typedValues;
    }

    @Override
    public String toString() {
        return sql;
    }

    @Override
    public String[] getPropertyNames() throws QueryException {
        return new String[0];
    }

}
