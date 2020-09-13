package com.fruit.task.master.core.mapper;
import com.fruit.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 短信的类型定位策略的Dao层
 * @Author yoko
 * @Date 2020/9/11 14:56
 * @Version 1.0
 */
@Mapper
public interface ShortMsgStrategyMapper<T> extends BaseDao<T> {
}
