package im.nll.data.dq.exceptions;

/**
 * 查询异常.
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 2013-1-17下午4:03:58
 */
public class QueryException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public QueryException() {
    }

    public QueryException(String message) {
        super(message);
    }

    public QueryException(Throwable cause) {
        super(cause);
    }

    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }

}
