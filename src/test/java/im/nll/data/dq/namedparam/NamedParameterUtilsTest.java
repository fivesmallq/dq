package im.nll.data.dq.namedparam;

import com.google.common.collect.Lists;
import im.nll.data.dq.BaseTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 2017/3/20 下午11:06
 */
public class NamedParameterUtilsTest extends BaseTest {

    @Test
    public void parseSqlStatement() throws Exception {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", Lists.newArrayList("1", "2", "3"));
        parameters.addValue("id", "1");
        parameters.addValue("id2", "2");
        parameters.addValue("id3", "2");
        ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement("select * from role where id>:id3 and id in(:ids) and id>:id and id!= :id2");
        String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, parameters);
        System.out.println(sqlToUse);
        Object[] params = NamedParameterUtils.buildValueArray(parsedSql, parameters, null);
        System.out.println(Arrays.toString(params));
        List<SqlParameter> declaredParameters = NamedParameterUtils.buildSqlParameterList(parsedSql, parameters);

        System.out.println(parsedSql.getOriginalSql());

    }

    @Test
    public void substituteNamedParameters() throws Exception {

    }

    @Test
    public void buildValueArray() throws Exception {

    }

    @Test
    public void buildSqlTypeArray() throws Exception {

    }

    @Test
    public void buildSqlParameterList() throws Exception {

    }

    @Test
    public void parseSqlStatementIntoString() throws Exception {

    }

    @Test
    public void substituteNamedParameters1() throws Exception {

    }

    @Test
    public void buildValueArray1() throws Exception {

    }

}
