package com.fruit.task.master.core.mapper;
import com.fruit.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 订单补单的Dao层
 * @Author yoko
 * @Date 2020/9/11 21:55
 * @Version 1.0
 */
@Mapper
public interface OrderReplenishMapper<T> extends BaseDao<T> {
}
