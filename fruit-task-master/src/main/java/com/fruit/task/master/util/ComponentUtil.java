package com.fruit.task.master.util;

import com.fruit.task.master.core.common.redis.RedisIdService;
import com.fruit.task.master.core.common.redis.RedisService;
import com.fruit.task.master.core.common.utils.constant.LoadConstant;
import com.fruit.task.master.core.service.*;
import com.fruit.task.master.core.service.task.*;

/**
 * 工具类
 */
public class ComponentUtil {
    public static RedisIdService redisIdService;
    public static RedisService redisService;
    public static LoadConstant loadConstant;
    public static StrategyService strategyService;


    public static MobileCardService mobileCardService;
    public static MobileCardShortMsgService mobileCardShortMsgService;
    public static ShortMsgStrategyService shortMsgStrategyService;
    public static ShortMsgArrearsService shortMsgArrearsService;
    public static BankTypeService bankTypeService;
    public static BankService bankService;
    public static BankCollectionService bankCollectionService;
    public static BankStrategyService bankStrategyService;
    public static BankShortMsgStrategyService bankShortMsgStrategyService;
    public static BankShortMsgService bankShortMsgService;
    public static ChannelService channelService;
    public static ChannelBankService channelBankService;
    public static MerchantService merchantService;
    public static MerchantRechargeService merchantRechargeService;
    public static OrderService orderService;
    public static OrderReplenishService orderReplenishService;
    public static StatisticsClickPayService statisticsClickPayService;
    public static ShortChainService shortChainService;




    public static TaskMobileCardShortMsgService taskMobileCardShortMsgService;
    public static TaskBankShortMsgService taskBankShortMsgService;
    public static TaskOrderService taskOrderService;
    public static TaskMonitorService taskMonitorService;
    public static TaskOrderReplenishService taskOrderReplenishService;
    public static TaskShortMsgArrearsService taskShortMsgArrearsService;


}
