package com.fruit.task.master.core.service.impl.task;

import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.mapper.task.TaskMonitorMapper;
import com.fruit.task.master.core.model.order.OrderModel;
import com.fruit.task.master.core.service.task.TaskMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:监控的Service层的实现层
 * @Author yoko
 * @Date 2020/9/15 16:22
 * @Version 1.0
 */
@Service
public class TaskMonitorServiceImpl<T> extends BaseServiceImpl<T> implements TaskMonitorService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskMonitorMapper taskMonitorMapper;



    public BaseDao<T> getDao() {
        return taskMonitorMapper;
    }

    @Override
    public List<Long> getBankIdListByOrder(Object obj) {
        return taskMonitorMapper.getBankIdListByOrder(obj);
    }

    @Override
    public List<OrderModel> getOrderList(Object obj) {
        return taskMonitorMapper.getOrderList(obj);
    }
}
