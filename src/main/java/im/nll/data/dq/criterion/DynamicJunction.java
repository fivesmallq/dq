package im.nll.data.dq.criterion;


import im.nll.data.dq.exceptions.QueryException;
import im.nll.data.dq.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 动态查询表达式.
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.1
 * @date 2013-1-18下午5:40:28
 */
public class DynamicJunction extends Junction {
    private final List<Criterion> conditions = new ArrayList<Criterion>();
    Object[] allParams = new Object[0];

    protected DynamicJunction(Nature nature) {
        super(nature);
    }

    @Override
    public DynamicJunction add(Criterion criterion) {
        conditions.add(criterion);
        return this;
    }

    @Override
    public String toSqlString() throws QueryException {
        boolean allNull = true;
        if (conditions.size() == 0) {
            return "1=1";
        }
        StringBuilder buffer = new StringBuilder().append('(');
        Iterator<Criterion> itr = conditions.iterator();
        while (itr.hasNext()) {
            Criterion criterion = itr.next();
            StringBuilder andBuilder = new StringBuilder().append(' ')
                    .append(super.getNature().getOperator()).append(' ');
            boolean isNull = false;
            for (Object one : criterion.getParameters()) {
                if (one == null) {
                    isNull = true;
                } else {
                    allNull = false;
                }
            }
            if (!isNull) {
                buffer.append(andBuilder).append(criterion.toSqlString());
                allParams = ArrayUtils.addAll(allParams,
                        criterion.getParameters());
            }
        }
        if (allNull) {
            return "1=1";
        }
        buffer.delete(
                buffer.indexOf(" " + super.getNature().getOperator() + " "),
                (" " + super.getNature().getOperator() + " ").length() + 1);
        return buffer.append(')').toString();
    }

    @Override
    public Object[] getParameters() throws QueryException {
        return allParams;
    }

}
