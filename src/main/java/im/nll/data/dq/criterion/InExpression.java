/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
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
import im.nll.data.dq.utils.StringUtils;

/**
 * in 表达式.
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.1
 * @date 2013-1-19下午12:36:34
 */
public class InExpression implements Criterion {

    private final String propertyName;
    private final Object[] values;

    protected InExpression(String propertyName, Object[] values) {
        this.propertyName = propertyName;
        this.values = values;
    }

    @Override
    public String toSqlString() throws QueryException {
        // TODO 优化参数小于3个的情况
        if (values.length > 1) {
            String singleValueParam = StringUtils.repeat("?, ",
                    values.length - 1) + "?";
            return propertyName + " in (" + singleValueParam + ')';
        } else {
            return propertyName + " = ?";
        }
    }

    @Override
    public Object[] getParameters() throws QueryException {
        return values;
    }

    @Override
    public String toString() {
        return propertyName + " in (" + (values) + ')';
    }

    @Override
    public String[] getPropertyNames() throws QueryException {
        return ArrayUtils.toStringArray(propertyName);
    }

}
