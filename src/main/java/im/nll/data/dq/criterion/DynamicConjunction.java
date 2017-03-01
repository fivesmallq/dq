package im.nll.data.dq.criterion;

/**
 * 动态查询表达式.
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.1
 * @date 2013-1-18下午5:40:28
 */
public class DynamicConjunction extends DynamicJunction {

    public DynamicConjunction() {
        super(Nature.AND);
    }
}
