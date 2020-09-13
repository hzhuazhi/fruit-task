package com.fruit.task.master.core.service.impl;
import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.mapper.BankShortMsgMapper;
import com.fruit.task.master.core.service.BankShortMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 银行卡短信数据表的Service层的实现层
 * @Author yoko
 * @Date 2020/9/11 17:12
 * @Version 1.0
 */
@Service
public class BankShortMsgServiceImpl<T> extends BaseServiceImpl<T> implements BankShortMsgService<T> {
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
    private BankShortMsgMapper bankShortMsgMapper;

    public BaseDao<T> getDao() {
        return bankShortMsgMapper;
    }
}
