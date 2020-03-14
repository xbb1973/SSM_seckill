package xbb.seckill.enums;

import xbb.seckill.entity.Seckill;

/**
 * @author ：xbb
 * @date ：Created in 2020/3/14 4:20 上午
 * @description：状态枚举类
 * @modified By：
 * @version: v0.1$
 */
public enum SeckillStateEnum {

    SUCCESS(1, "秒杀成功"),
    END(0, "秒杀结束"),
    REPEAT_KILL(-1, "重复秒杀"),
    INNER_ERROR(-2, "系统异常"),
    DATA_REWRITE(-3, "数据篡改");

    private int state;

    private String stateInfo;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    SeckillStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    @Override
    public String toString() {
        return "SeckillStateEnum{" +
                "state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                '}';
    }

    public static SeckillStateEnum stateOf(int index) {
        for (SeckillStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }

}
