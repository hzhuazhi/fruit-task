package com.fruit.task.master.core.service.impl.task;

import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.mapper.task.TaskBankShortMsgMapper;
import com.fruit.task.master.core.model.bank.BankShortMsgModel;
import com.fruit.task.master.core.service.task.TaskBankShortMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:银行卡短信数据的Service层的实现层
 * @Author yoko
 * @Date 2020/9/14 15:32
 * @Version 1.0
 */
@Service
public class TaskBankShortMsgServiceImpl<T> extends BaseServiceImpl<T> implements TaskBankShortMsgService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskBankShortMsgMapper taskBankShortMsgMapper;



    public BaseDao<T> getDao() {
        return taskBankShortMsgMapper;
    }

    @Override
    public List<BankShortMsgModel> getDataList(Object obj) {
        return taskBankShortMsgMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskBankShortMsgMapper.updateStatus(obj);
    }
}
