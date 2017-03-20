package im.nll.data.dq;

import com.google.common.collect.Lists;
import im.nll.data.dq.criterion.Rs;
import org.junit.Test;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 2017/3/20 下午5:34
 */
public class SQLBuilderTest {
    @Test
    public void from() throws Exception {

    }

    @Test
    public void of() throws Exception {
        SQLBuilder sqlBuilder = SQLBuilder.of("select * from role")
                .add(Rs.in("id", Lists.newArrayList("1", "2", "3")));
        System.out.println(sqlBuilder.toCountSQL());
        System.out.println(sqlBuilder.toSelectSQL());
        System.out.println(sqlBuilder.toStatementSQL());
    }

    @Test
    public void from1() throws Exception {

    }

    @Test
    public void getTableClass() throws Exception {

    }

    @Test
    public void add() throws Exception {

    }

    @Test
    public void dyAnd() throws Exception {

    }

    @Test
    public void dyOr() throws Exception {

    }

    @Test
    public void addOrder() throws Exception {

    }

    @Test
    public void limit() throws Exception {

    }

    @Test
    public void toStatementSQL() throws Exception {

    }

    @Test
    public void toSelectSQL() throws Exception {

    }

    @Test
    public void toCountSQL() throws Exception {

    }

    @Test
    public void getParams() throws Exception {

    }

    @Test
    public void safeLimit() throws Exception {

    }

    @Test
    public void safeOffset() throws Exception {

    }

}
