package com.fruit.task.master.core.mapper;
import com.fruit.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 银行卡短信数据表的Dao层
 * @Author yoko
 * @Date 2020/9/11 17:11
 * @Version 1.0
 */
@Mapper
public interface BankShortMsgMapper<T> extends BaseDao<T> {
}
