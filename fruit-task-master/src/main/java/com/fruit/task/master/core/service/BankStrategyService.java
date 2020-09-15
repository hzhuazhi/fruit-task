package com.fruit.task.master.core.service;

import com.fruit.task.master.core.common.service.BaseService;
import com.fruit.task.master.core.model.bank.BankStrategyModel;

/**
 * @Description 银行卡放量策略的Service层
 * @Author yoko
 * @Date 2020/9/11 16:40
 * @Version 1.0
 */
public interface BankStrategyService<T> extends BaseService<T> {

    /**
     * @Description: 银行卡限制
     * <p>
     *     check银行卡是否超过放量限制，如果超过放量限制，则进行redis缓存
     * </p>
     * @param bankStrategyModel - 放量策略
     * @param payType - 支付类型
     * @param dayNum - 日成功次数
     * @param dayMoney - 日成功金额
     * @param monthMoney - 月成功金额
     * @return void
     * @author yoko
     * @date 2020/9/15 11:37
     */
    public void bankStrategyLimit(BankStrategyModel bankStrategyModel, int payType, int dayNum, String dayMoney, String monthMoney);
}
