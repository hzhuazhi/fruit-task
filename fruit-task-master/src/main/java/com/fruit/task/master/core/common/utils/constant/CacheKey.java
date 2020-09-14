package com.fruit.task.master.core.common.utils.constant;

/**
 * @author df
 * @Description:redis的key
 * @create 2019-05-22 15:43
 **/
public interface CacheKey {

    /**
     * 策略数据
     */
    String STRATEGY = "-1";

    /**
     * 短信的类型定位策略
     */
    String SHORT_MSG_STRATEGY = "-2";

    /**
     * 商户：商户的秘钥
     */
    String CHANNEL_SECRETKEY = "-3";

    /**
     * 锁：在筛选银行卡，需要先锁住此银行卡
     * 高并发避免出现问题
     */
    String LOCK_BANK = "-4";

    /**
     * 派单给银行卡时，要派单的金额，存入redis缓存
     * task:如果订单成功，则需要删除此task
     */
    String BANK_ORDER_MONEY = "-5";

    /**
     * 微信转卡日收款金额
     * task:需要检测收款限制
     */
    String WX_IN_DAY_MONEY = "-6";

    /**
     * 微信转卡月收款金额
     * task:需要检测收款限制
     */
    String WX_IN_MONTH_MONEY = "-7";

    /**
     * 微信转卡日收款次数
     * task:需要检测收款限制
     */
    String WX_IN_DAY_NUM = "-8";

    /**
     * 支付宝转卡日收款金额
     * task:需要检测收款限制
     */
    String ZFB_IN_DAY_MONEY = "-9";

    /**
     * 支付宝转卡月收款金额
     * task:需要检测收款限制
     */
    String ZFB_IN_MONTH_MONEY = "-10";

    /**
     * 支付宝转卡日收款次数
     * task:需要检测收款限制
     */
    String ZFB_IN_DAY_NUM = "-11";

    /**
     * 卡转卡日收款金额
     * task:需要检测收款限制
     */
    String CARD_IN_DAY_MONEY = "-12";

    /**
     * 卡转卡月收款金额
     * task:需要检测收款限制
     */
    String CARD_IN_MONTH_MONEY = "-13";

    /**
     * 卡转卡日收款次数
     * task:需要检测收款限制
     */
    String CARD_IN_DAY_NUM = "-14";

    /**
     * 短代API
     */
    String SHORT_CHAIN = "-15";

    /**
     * 短信的类型定位策略-多个
     * <p>
     *     根据类型的>=来查询
     * </p>
     */
    String SHORT_MSG_STRATEGY_TWO = "-16";



}
