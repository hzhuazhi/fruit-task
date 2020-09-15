package com.fruit.task.master.core.service.impl.task;

import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.mapper.task.TaskOrderReplenishMapper;
import com.fruit.task.master.core.model.order.OrderReplenishModel;
import com.fruit.task.master.core.service.task.TaskOrderReplenishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:订单补单的Service层的实现层
 * @Author yoko
 * @Date 2020/9/15 17:43
 * @Version 1.0
 */
@Service
public class TaskOrderReplenishServiceImpl<T> extends BaseServiceImpl<T> implements TaskOrderReplenishService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskOrderReplenishMapper taskOrderReplenishMapper;



    public BaseDao<T> getDao() {
        return taskOrderReplenishMapper;
    }

    @Override
    public List<OrderReplenishModel> getDataList(Object obj) {
        return taskOrderReplenishMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskOrderReplenishMapper.updateStatus(obj);
    }
}
