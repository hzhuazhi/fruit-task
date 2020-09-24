package com.fruit.task.master.core.common.utils.constant;

/**
 * @Description task任务的redis的key
 * @Author yoko
 * @Date 2020/6/3 14:50
 * @Version 1.0
 */
public interface TkCacheKey {

    /**
     * LOCK-手机短信类容的task的redis
     */
    String LOCK_MOBILE_CARD_SHORT_MSG = "-1";

    /**
     * 手机卡的信息
     */
    String MOBILE_CARD_PHONE_NUM = "-2";

    /**
     * LOCK-手机短信类容的task的redis
     * <p>
     *     正式处理逻辑运算
     * </p>
     */
    String LOCK_MOBILE_CARD_SHORT_MSG_HANDLE = "-3";

    /**
     * LOCK-银行卡短信
     */
    String LOCK_BANK_SHORT_MSG = "-4";

    /**
     * LOCK-银行卡短信已经扩充完毕的数据
     */
    String LOCK_BANK_SHORT_MSG_WORK_TYPE = "-5";

    /**
     * LOCK-失效订单
     */
    String LOCK_ORDER_INVALID = "-6";

    /**
     * LOCK-成功订单
     */
    String LOCK_ORDER_SUCCESS = "-7";

    /**
     * LOCK-同步订单
     */
    String LOCK_ORDER_SEND = "-8";

    /**
     * LOCK-监控：监控银行卡
     */
    String LOCK_MONITOR_BANK = "-9";

    /**
     * LOCK-订单补单
     */
    String LOCK_ORDER_REPLENISH = "-10";

    /**
     * LOCK-手机欠费短信
     */
    String LOCK_SHORT_MSG_ARREARS = "-11";

    /**
     * LOCK-卡商充值
     */
    String LOCK_MERCHANT_RECHARGE = "-12";

    /**
     * LOCK-卡商的账户ID
     */
    String LOCK_ACCOUNT_ID = "-13";

    /**
     * LOCK-下发-分配操作
     */
    String LOCK_ISSUE_DISTRIBUTION = "-14";

    /**
     * LOCK-下发-是否归集完毕操作
     */
    String LOCK_ISSUE_COMPLETE = "-15";

    /**
     * LOCK-下发-同步下发的成功数据
     */
    String LOCK_ISSUE_SEND = "-16";

}
