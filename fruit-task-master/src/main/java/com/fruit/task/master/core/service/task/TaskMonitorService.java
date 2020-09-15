package com.fruit.task.master.core.service.task;

import com.fruit.task.master.core.common.service.BaseService;
import com.fruit.task.master.core.model.order.OrderModel;

import java.util.List;

/**
 * @Description task:监控的Service层
 * @Author yoko
 * @Date 2020/9/15 16:21
 * @Version 1.0
 */
public interface TaskMonitorService<T> extends BaseService<T> {

    /**
     * @Description: 获取银行卡ID集合
     * <p>
     *     根据日期获取订单表中所有去重复的银行卡ID
     * </p>
     * @param obj
     * @return
     * @author yoko
     * @date 2020/9/15 16:18
     */
    public List<Long> getBankIdListByOrder(Object obj);

    /**
     * @Description: 获取订单信息
     * <p>
     *     根据银行卡、支付类型获取超过失效时间并且最新的订单集合
     * </p>
     * @param obj
     * @return
     * @author yoko
     * @date 2020/9/15 16:19
     */
    public List<OrderModel> getOrderList(Object obj);
}
