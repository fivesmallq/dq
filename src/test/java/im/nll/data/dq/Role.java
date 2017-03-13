package im.nll.data.dq;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 2017/3/13 下午11:19
 */
@Entity(name = "role")
public class Role {
    @Id
    public String id;
    public Date createTime;
    public Date updateTime;
    public String name;
    public String disable;
}
