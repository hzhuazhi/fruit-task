package com.fruit.task.master.core.mapper;
import com.fruit.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 手机卡欠费短信的Dao层
 * @Author yoko
 * @Date 2020/9/11 15:13
 * @Version 1.0
 */
@Mapper
public interface ShortMsgArrearsMapper<T> extends BaseDao<T> {
}
