/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2008-2012, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
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
 */
package im.nll.data.dq.criterion;


import im.nll.data.dq.exceptions.QueryException;
import im.nll.data.dq.utils.ArrayUtils;

/**
 * 简单表达式处理.
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.1
 * @date 2013-1-19下午12:38:17
 */
public class SimpleExpression implements Criterion {
    private final String propertyName;
    private final Object value;
    private boolean ignoreCase;
    private final String op;

    protected SimpleExpression(String propertyName, Object value, String op) {
        this.propertyName = propertyName;
        this.value = value;
        this.op = op;
    }

    protected SimpleExpression(String propertyName, Object value, String op,
                               boolean ignoreCase) {
        this.propertyName = propertyName;
        this.value = value;
        this.ignoreCase = ignoreCase;
        this.op = op;
    }

    public SimpleExpression ignoreCase() {
        ignoreCase = true;
        return this;
    }

    @Override
    public String toSqlString() throws QueryException {
        StringBuilder fragment = new StringBuilder();
        fragment.append(propertyName);
        fragment.append(getOp()).append('?');
        return fragment.toString();
    }

    @Override
    public Object[] getParameters() throws QueryException {
        Object icvalue = ignoreCase ? value.toString().toLowerCase() : value;
        return new Object[]{icvalue};
    }

    @Override
    public String toString() {
        return propertyName + getOp() + value;
    }

    protected final String getOp() {
        return op;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String[] getPropertyNames() throws QueryException {
        return ArrayUtils.toStringArray(propertyName);
    }

}
