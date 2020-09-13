package com.fruit.task.master.core.mapper;
import com.fruit.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 银行收款短信解析策略的Dao层
 * @Author yoko
 * @Date 2020/9/11 16:55
 * @Version 1.0
 */
@Mapper
public interface BankShortMsgStrategyMapper<T> extends BaseDao<T> {
}
