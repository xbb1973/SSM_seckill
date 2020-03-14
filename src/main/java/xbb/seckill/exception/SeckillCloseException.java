package xbb.seckill.exception;

/**
 * @author ：xbb
 * @date ：Created in 2020/3/14 2:55 上午
 * @description：秒杀关闭异常(extend SeckillException)
 * @modified By：
 * @version: v0.1$
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeckillCloseException(Throwable cause) {
        super(cause);
    }

    public SeckillCloseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
