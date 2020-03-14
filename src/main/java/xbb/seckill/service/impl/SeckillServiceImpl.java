package xbb.seckill.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import sun.rmi.runtime.Log;
import xbb.seckill.dao.ISeckillDao;
import xbb.seckill.dao.ISuccessKilledDao;
import xbb.seckill.dto.Exposer;
import xbb.seckill.dto.SeckillExecution;
import xbb.seckill.entity.Seckill;
import xbb.seckill.entity.SuccessKilled;
import xbb.seckill.enums.SeckillStateEnum;
import xbb.seckill.exception.RepeatKillException;
import xbb.seckill.exception.SeckillCloseException;
import xbb.seckill.exception.SeckillException;
import xbb.seckill.service.ISeckillService;

import java.util.Date;
import java.util.List;

/**
 * @author ：xbb
 * @date ：Created in 2020/3/14 3:24 上午
 * @description：实现seckill service
 * @modified By：
 * @version: v0.1$
 */
@Service // @Components @Service @Dao @Controller
public class SeckillServiceImpl implements ISeckillService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    // 注入Service依赖 @Autowired @Resource @Inject
    // 这样spring会去查找ISeckillDao的实例将其注入seckillDao，不需要我们使用seckillDao = new来创建实例/
    @Autowired
    private ISeckillDao seckillDao;

    @Autowired
    private ISuccessKilledDao successKilledDao;

    private final String slat = "DJSAHD1ge1h2keJD%@EGDKsdasdj1kjl*";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 20);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = getById(seckillId);

        // 没有该商品
        if (seckill == null) {
            return new Exposer(false, seckillId);
        }

        // 秒杀未开始
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date currentTime = new Date();
        if (currentTime.getTime() < startTime.getTime() || currentTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, currentTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        // md5 转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional
    /**
     * 使用注解控制事务方法的优点:
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如只有一条修改操作、只读操作不要事务控制。mysql行级锁？
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        // 执行秒杀业务逻辑: 1 减库存 + 2 记录购买记录
        Date currentTime = new Date();
        try {
            // 1 减库存
            int updateCount = seckillDao.reduceNumber(seckillId, currentTime);
            if (updateCount <= 0) {
                // 没有更新记录，表示无秒杀操作，秒杀结束
                throw new SeckillCloseException("seckill is closed");
            } else {
                // 2 记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                // 唯一:seckillId, userPhone 联合主键
                if (insertCount <= 0) {
                    // 重复秒杀
                    throw new RepeatKillException("seckill is duplicate");
                } else {
                    // 秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException | RepeatKillException e1) {
            throw e1;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 所有的编译期异常都转化为运行期异常
            // spring的声明式事务异常处理机制处理RunTimeException和Error，进行回滚rollback
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }
}
