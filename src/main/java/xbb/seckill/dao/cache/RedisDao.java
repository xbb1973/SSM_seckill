package xbb.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import xbb.seckill.entity.Seckill;


/**
 * @author ：xbb
 * @date ：Created in 2020/3/17 9:33 上午
 * @description：redis缓存
 * @modified By：
 * @version: v0.1$
 */
public class RedisDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public Seckill getSeckill(long seckillId) {
        // redis 操作逻辑
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckillId;
                // Redis、Jedis 并没有实现序列化操作
                // 典型的缓存访问逻辑：get->byte[]->反序列化->Object(Seckill)
                // 采用自定义序列化方式，开源：protostuff
                byte[] bytes = jedis.get(key.getBytes());
                // 缓存重获取到
                if (bytes != null) {
                    // 创建空对象
                    Seckill seckill = schema.newMessage();
                    // seckill被反序列化
                    ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                    return seckill;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public String putSeckill(Seckill seckill) {
        // 典型的缓存访问逻辑：get->byte[]->反序列化->Object(Seckill)
        // set Object(Seckill) -> 序列化 -> byte[] -> redis
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                // 超时缓存
                int timeout = 60 * 60;
                String result = jedis.setex(key.getBytes(), timeout, bytes);
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
