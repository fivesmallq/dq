package im.nll.data.dq;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import im.nll.data.dq.connection.ConnectionProvider;
import im.nll.data.dq.criterion.Rs;
import im.nll.data.dq.entity.Role;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 2017/3/3 上午11:06
 */
public class DQTest {

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    static class JdbcConnectionProvider implements ConnectionProvider {

        @Override
        public Connection get() {
            try {
                Connection conn = DriverManager.
                        getConnection("jdbc:h2:mem:test");
                return conn;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @BeforeClass
    public static void setUp() throws Exception {
        DQ.with(new JdbcConnectionProvider());
        DQ.execute("CREATE TABLE `role` (\n" +
                "  `id` varchar(255) NOT NULL,\n" +
                "  `name` varchar(45) NOT NULL,\n" +
                "  `disable` varchar(45) DEFAULT NULL,\n" +
                "  `create_time` datetime DEFAULT NULL,\n" +
                "  `update_time` datetime DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
        DQ.execute("INSERT INTO `role` (`id`, `name`, `disable`, `create_time`, `update_time`)\n" +
                "VALUES\n" +
                "\t('58bfcd1c867538143af5bb5e', '护士长', 'false', '2017-03-08 17:21:33', '2017-03-13 15:31:35');\n");
        DQ.execute("INSERT INTO `role` (`id`, `name`, `disable`, `create_time`, `update_time`)\n" +
                "VALUES\n" +
                "\t('58bfcd51867538143af5bb62', '医生', 'false', '2017-03-08 17:22:25', '2017-03-08 17:22:25');\n");
        DQ.execute("INSERT INTO `role` (`id`, `name`, `disable`, `create_time`, `update_time`)\n" +
                "VALUES\n" +
                "\t('58bfcd3c867538143af5bb60', '客服', 'false', '2017-03-08 17:22:04', '2017-03-08 17:22:04');\n");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void query() throws Exception {
        DQ.with(new JdbcConnectionProvider());
        List<String> roleIds = new ArrayList<>();
        roleIds.add("58bfcd1c867538143af5bb5e");
        roleIds.add("58bfcd51867538143af5bb62");
        roleIds.add("58bfcd3c867538143af5bb60");
        SQLBuilder sqlBuilder = SQLBuilder.from(Role.class).dyAnd(
                Rs.eq("disable", "false"),
                Rs.in("id", roleIds)
        );
        List<Role> roles = DQ.query(sqlBuilder);
        System.out.println(JSON.toJSONString(roles, true));
    }

    @Test
    public void query1() throws Exception {

    }

    @Test
    public void bindQuery() throws Exception {
        DQ.with(new JdbcConnectionProvider());
        List<Role> roles = DQ.bindQuery("select * from role where disable=:disable", Role.class, ImmutableMap.of("disable", "false"));
        System.out.println(JSON.toJSONString(roles, true));
    }

    @Test
    public void count() throws Exception {
        DQ.with(new JdbcConnectionProvider());
        Long count = DQ.count("select count(1) from role");
        System.out.println(count);
    }

    @Test
    public void get() throws Exception {
        DQ.with(new JdbcConnectionProvider());
        Role role = DQ.get("select * from role where id=?", Role.class, "58bfcd3c867538143af5bb60");
        System.out.println(JSON.toJSONString(role, true));
        String name = DQ.get("select name from role where id=?", String.class, "58bfcd3c867538143af5bb60");
        System.out.println(name);
    }

    @Test
    public void bindGet() throws Exception {
        DQ.with(new JdbcConnectionProvider());
        Role role = DQ.bindGet("select * from role where id=:id", Role.class, ImmutableMap.of("id", "58bfcd3c867538143af5bb60"));
        System.out.println(JSON.toJSONString(role, true));
    }

    @Test
    public void count1() throws Exception {

    }

    @Test
    public void bindCount() throws Exception {
        DQ.with(new JdbcConnectionProvider());
        Long count = DQ.bindCount("select count(1) from role where id=:id", ImmutableMap.of("id", "58bfcd3c867538143af5bb60"));
        System.out.println(count);
    }

    @Test
    public void execute() throws Exception {

    }

    @Test
    public void bindExecute() throws Exception {

    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void bindUpdate() throws Exception {

    }

    @Test
    public void batch() throws Exception {
        DQ.with(new JdbcConnectionProvider());
        Object[] param1 = new Object[]{UUID.randomUUID().toString(), "test1"};
        Object[] param2 = new Object[]{UUID.randomUUID().toString(), "test2"};
        List<Object[]> list = Lists.newArrayList(param1, param2);
        Long count = DQ.count("select count(1) from role");
        System.out.println(count);
        DQ.batch("insert into role(id,name) values(?,?)", list);
        count = DQ.count("select count(1) from role");
        System.out.println(count);
    }

    @Test
    public void bindBatch() throws Exception {
        DQ.with(new JdbcConnectionProvider());
        List<Map<String, Object>> list = Lists.newArrayList(
                ImmutableMap.of("id", UUID.randomUUID().toString(), "name", "test1"),
                ImmutableMap.of("id", UUID.randomUUID().toString(), "name", "test2"));
        Long count = DQ.count("select count(1) from role");
        System.out.println(count);
        DQ.bindBatch("insert into role(id,name) values(:id,:name)", list);
        count = DQ.count("select count(1) from role");
        System.out.println(count);
    }

}
