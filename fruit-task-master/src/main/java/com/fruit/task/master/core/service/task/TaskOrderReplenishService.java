package com.fruit.task.master.core.service.task;

import com.fruit.task.master.core.common.service.BaseService;
import com.fruit.task.master.core.model.order.OrderReplenishModel;

import java.util.List;

/**
 * @Description task:订单补单的Service层
 * @Author yoko
 * @Date 2020/9/15 17:40
 * @Version 1.0
 */
public interface TaskOrderReplenishService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的订单补单信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<OrderReplenishModel> getDataList(Object obj);

    /**
     * @Description: 更新订单补单信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
