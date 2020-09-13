package com.fruit.task.master.core.service.impl;
import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.mapper.BankShortMsgStrategyMapper;
import com.fruit.task.master.core.service.BankShortMsgStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 银行收款短信解析策略的Service层的实现层
 * @Author yoko
 * @Date 2020/9/11 16:56
 * @Version 1.0
 */
@Service
public class BankShortMsgStrategyServiceImpl<T> extends BaseServiceImpl<T> implements BankShortMsgStrategyService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 3分钟
     */
    public long THREE_MIN = 180;

    /**
     * 11分钟.
     */
    public long ELEVEN_MIN = 660;

    public long TWO_HOUR = 2;



    @Autowired
    private BankShortMsgStrategyMapper bankShortMsgStrategyMapper;

    public BaseDao<T> getDao() {
        return bankShortMsgStrategyMapper;
    }
}
