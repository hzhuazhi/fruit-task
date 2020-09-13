package com.fruit.task.master.core.mapper;
import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.model.order.OrderModel;
import org.apache.ibatis.annotations.Mapper;


/**
 * @Description 任务订单的Dao层
 * @Author yoko
 * @Date 2020/5/21 19:32
 * @Version 1.0
 */
@Mapper
public interface OrderMapper<T> extends BaseDao<T> {

    /**
     * @Description: 根据订单号查询订单状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/6/8 20:00
     */
    public int getOrderStatus(OrderModel model);

}
