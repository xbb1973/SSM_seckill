package xbb.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xbb.seckill.dao.ISeckillDao;
import xbb.seckill.entity.Seckill;

import static org.junit.Assert.*;

/**
 * @author ：xbb
 * @date ：Created in 2020/3/17 10:14 上午
 * @description：
 * @modified By：
 * @version: $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml" })
public class RedisDaoTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private int id = 1003;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private ISeckillDao seckillDao;

    @Test
    public void testSeckill() throws Exception {
        Seckill seckill = redisDao.getSeckill(id);
        if (seckill == null) {
            seckill = seckillDao.queryById(id);
            if (seckill != null) {
                String result = redisDao.putSeckill(seckill);
                logger.info(result);
                seckill = redisDao.getSeckill(id);
                logger.info(seckill.toString());
            }
        }
    }

}