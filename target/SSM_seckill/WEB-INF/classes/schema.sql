USE seckill;
CREATE TABLE seckill (
                         `seckill_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
                         `name` VARCHAR ( 120 ) NOT NULL COMMENT '商品名称',
                         `number` INT NOT NULL COMMENT '库存数量',
                         `start_time` TIMESTAMP NOT NULL DEFAULT '2018-5-5 00:00:00' COMMENT '开始时间',
                         `end_time` TIMESTAMP NOT NULL DEFAULT '2018-5-5 00:00:00' COMMENT '结束时间',
                         `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         PRIMARY KEY ( seckill_id ),
                         KEY idx_start_time ( start_time ),
                         KEY idx_end_time ( end_time ),
                         KEY idx_create_time ( create_time )
) ENGINE = INNODB AUTO_INCREMENT = 1000 DEFAULT CHARSET = utf8 COMMENT = '秒杀库存表';
INSERT INTO seckill ( name, number, start_time, end_time )
VALUES
( '1000秒杀iPhone6S', 100, '2020-03-01 00:00:00', '2020-04-02 00:00:00' ),
( '500秒杀MBP', 200, '2020-03-01 00:00:00', '2020-04-02 00:00:00' ),
( '300秒杀iPad', 100, '2020-03-01 00:00:00', '2020-04-02 00:00:00' ),
( '200秒杀小米MIX', 300, '2020-03-01 00:00:00', '2020-04-02 00:00:00' );
CREATE TABLE success_killed (
                                `seckill_id` BIGINT NOT NULL auto_increment COMMENT '商品库存id',
                                `user_phone` BIGINT NOT NULL COMMENT '用户手机号',
                                `state` TINYINT NOT NULL DEFAULT - 1 COMMENT '状态标志，-1：无效 0：成功 1：已付款',
                                `create_time` TIMESTAMP NOT NULL COMMENT '创建时间时间',
                                PRIMARY KEY ( seckill_id, user_phone ),
                                KEY idx_create_tim ( create_time )
) ENGINE = INNODB AUTO_INCREMENT = 1000 DEFAULT CHARSET = utf8 COMMENT = '秒杀明细表';