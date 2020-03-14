package xbb.seckill.service;

import org.apache.ibatis.type.BlobByteObjectArrayTypeHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xbb.seckill.dao.ISeckillDao;
import xbb.seckill.dao.ISuccessKilledDao;
import xbb.seckill.dto.Exposer;
import xbb.seckill.dto.SeckillExecution;
import xbb.seckill.entity.Seckill;
import xbb.seckill.exception.RepeatKillException;
import xbb.seckill.exception.SeckillCloseException;
import xbb.seckill.util.Util;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author ：xbb
 * @date ：Created in 2020/3/14 5:01 上午
 * @description：
 * @modified By：
 * @version: $
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml" })

public class ISeckillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> seckills = seckillService.getSeckillList();
        logger.info("SeckillList={}", seckills);
    }

    @Test
    public void getById() {
        long seckillId = 1000;
        Seckill seckill = seckillService.getById(seckillId);
        logger.info("Seckill={}", seckill);
    }


    @Test
    public void exportSeckillUrl() {
        long seckillId = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        logger.info("Exposer={}", exposer);
    }

    @Test
    public void executeSeckill() {
        long seckillId = 1000;
        long phone = Util.getPhoneNum();
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()){
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, phone, exposer.getMd5());
                logger.info("SeckillExecution={}", seckillExecution);
            } catch (RepeatKillException | SeckillCloseException e) {
                logger.error(e.getMessage());
            }
        } else {
            logger.warn("Exposer={}", exposer);
        }
    }
}