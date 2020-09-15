package com.fruit.task.master.core.mapper.task;

import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.model.order.OrderReplenishModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task:订单补单的Dao层
 * @Author yoko
 * @Date 2020/9/15 17:38
 * @Version 1.0
 */
@Mapper
public interface TaskOrderReplenishMapper<T> extends BaseDao<T> {

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
