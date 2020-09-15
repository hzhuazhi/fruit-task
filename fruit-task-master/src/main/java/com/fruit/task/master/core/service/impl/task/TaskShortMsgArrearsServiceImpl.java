package com.fruit.task.master.core.service.impl.task;

import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.mapper.task.TaskShortMsgArrearsMapper;
import com.fruit.task.master.core.model.shortmsg.ShortMsgArrearsModel;
import com.fruit.task.master.core.service.task.TaskShortMsgArrearsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:手机卡欠费短信的Service层的实现层
 * @Author yoko
 * @Date 2020/9/15 19:08
 * @Version 1.0
 */
@Service
public class TaskShortMsgArrearsServiceImpl<T> extends BaseServiceImpl<T> implements TaskShortMsgArrearsService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskShortMsgArrearsMapper taskShortMsgArrearsMapper;



    public BaseDao<T> getDao() {
        return taskShortMsgArrearsMapper;
    }

    @Override
    public List<ShortMsgArrearsModel> getDataList(Object obj) {
        return taskShortMsgArrearsMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskShortMsgArrearsMapper.updateStatus(obj);
    }
}
