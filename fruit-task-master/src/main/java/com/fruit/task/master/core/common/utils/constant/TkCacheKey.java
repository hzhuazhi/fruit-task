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


}
