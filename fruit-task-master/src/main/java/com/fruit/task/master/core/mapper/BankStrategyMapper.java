package com.fruit.task.master.core.mapper;
import com.fruit.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 银行卡放量策略的Dao层
 * @Author yoko
 * @Date 2020/9/11 16:39
 * @Version 1.0
 */
@Mapper
public interface BankStrategyMapper<T> extends BaseDao<T> {
}
