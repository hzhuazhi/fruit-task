package com.fruit.task.master.core.service.impl.task;

import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.mapper.task.TaskOrderMapper;
import com.fruit.task.master.core.model.order.OrderModel;
import com.fruit.task.master.core.service.task.TaskOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:订单的Service层的实现层
 * @Author yoko
 * @Date 2020/9/14 22:15
 * @Version 1.0
 */
@Service
public class TaskOrderServiceImpl<T> extends BaseServiceImpl<T> implements TaskOrderService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskOrderMapper taskOrderMapper;



    public BaseDao<T> getDao() {
        return taskOrderMapper;
    }

    @Override
    public List<OrderModel> getDataList(Object obj) {
        return taskOrderMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskOrderMapper.updateStatus(obj);
    }
}
