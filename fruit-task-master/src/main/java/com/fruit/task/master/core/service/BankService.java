package com.fruit.task.master.core.service;

import com.fruit.task.master.core.common.service.BaseService;
import com.fruit.task.master.core.model.bank.BankModel;

import java.util.List;

/**
 * @Description 银行的Service层
 * @Author yoko
 * @Date 2020/5/18 19:06
 * @Version 1.0
 */
public interface BankService<T> extends BaseService<T> {

    /**
     * @Description: 获取银行卡以及银行卡的放量策略数据
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/12 16:24
     */
    public List<BankModel> getBankAndStrategy(BankModel model);

}
