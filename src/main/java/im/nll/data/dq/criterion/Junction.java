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
import im.nll.data.dq.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * 多表达式组合处理.
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.1
 * @date 2013-1-19下午12:36:45
 */
public class Junction implements Criterion {
    private final Nature nature;
    private final List<Criterion> conditions = new ArrayList<Criterion>();

    protected Junction(Nature nature) {
        this.nature = nature;
    }

    public Junction add(Criterion criterion) {
        conditions.add(criterion);
        return this;
    }

    public Nature getNature() {
        return nature;
    }

    public Iterable<Criterion> conditions() {
        return conditions;
    }

    @Override
    public String toSqlString() throws QueryException {
        if (conditions.size() == 0) {
            return "1=1";
        }
        StringBuilder buffer = new StringBuilder().append('(');
        Iterator<Criterion> itr = conditions.iterator();
        while (itr.hasNext()) {
            buffer.append(itr.next().toSqlString());
            if (itr.hasNext()) {
                buffer.append(' ').append(nature.getOperator()).append(' ');
            }
        }
        return buffer.append(')').toString();
    }

    @Override
    public Object[] getParameters() throws QueryException {
        ArrayList<Object> typedValues = new ArrayList<>();
        for (Criterion condition : conditions) {
            Object[] subValues = condition.getParameters();
            Collections.addAll(typedValues, subValues);
        }
        return typedValues.toArray(new Object[typedValues.size()]);
    }

    @Override
    public String toString() {
        return '(' + StringUtils.join(' ' + nature.getOperator() + ' ',
                conditions.iterator()) + ')';
    }

    public static enum Nature {
        AND, OR;

        public String getOperator() {
            return name().toLowerCase();
        }
    }

    @Override
    public String[] getPropertyNames() throws QueryException {
        String[] strings = new String[]{};
        for (Criterion one : conditions) {
            strings = ArrayUtils.addAll(strings, one.getPropertyNames());
        }
        return strings;
    }
}
