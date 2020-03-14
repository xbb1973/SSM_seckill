package xbb.seckill.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xbb.seckill.entity.SuccessKilled;

import javax.annotation.Resource;

import static org.junit.Assert.*;

//配置spring和junit整合，这样junit在启动时就会加载spring容器
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})

public class ISuccessKilledDaoTest {

    @Resource
    private ISuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        long seckillId = 1000;
        long userPhone = 13476191877L;
        int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
        System.out.println("insertCount=" + insertCount);
    }

    @Test
    public void queryByIdWithSeckill() {
        long seckillId = 1000L;
        long userPhone = 13476191877L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }
}