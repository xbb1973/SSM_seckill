package xbb.seckill.exception;

/**
 * @author ：xbb
 * @date ：Created in 2020/3/14 2:54 上午
 * @description：重复秒杀异常(extend SeckillException)
 * @modified By：
 * @version: v0.1$
 */
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatKillException(Throwable cause) {
        super(cause);
    }

    public RepeatKillException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
