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
import im.nll.data.dq.utils.ArrayUtils;

/**
 * not null表达式.
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.1
 * @date 2013-1-19下午12:37:32
 */
public class NotNullExpression implements Criterion {

    private final String propertyName;

    private static final Object[] NO_VALUES = new Object[0];

    protected NotNullExpression(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String toSqlString() throws QueryException {
        return propertyName + " is not null";
    }

    @Override
    public Object[] getParameters() throws QueryException {
        return NO_VALUES;
    }

    @Override
    public String toString() {
        return propertyName + " is null";
    }

    @Override
    public String[] getPropertyNames() throws QueryException {
        return ArrayUtils.toStringArray(propertyName);
    }
}
