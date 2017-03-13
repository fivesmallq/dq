package im.nll.data.dq;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import im.nll.data.dq.criterion.Rs;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                        getConnection("jdbc:h2:./test");
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
        DQ.execute("drop table role");
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

    }

}
