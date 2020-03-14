package xbb.seckill.exception;

/**
 * @author ：xbb
 * @date ：Created in 2020/3/14 2:57 上午
 * @description：秒杀业务异常(run time exception)
 * @modified By：
 * @version: v0.1$
 */
public class SeckillException extends RuntimeException {

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeckillException(Throwable cause) {
        super(cause);
    }

    public SeckillException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
