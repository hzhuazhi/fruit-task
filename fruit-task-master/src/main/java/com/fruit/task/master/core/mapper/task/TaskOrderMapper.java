package com.fruit.task.master.core.mapper.task;

import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.model.order.OrderModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task:订单的Dao层
 * @Author yoko
 * @Date 2020/9/14 21:57
 * @Version 1.0
 */
@Mapper
public interface TaskOrderMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询未跑的订单信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<OrderModel> getDataList(Object obj);

    /**
     * @Description: 更新订单信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);

    /**
     * @Description: 获取要同步给下游的订单数据
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/8 17:38
     */
    public List<OrderModel> getOrderNotifyList(Object obj);
}
