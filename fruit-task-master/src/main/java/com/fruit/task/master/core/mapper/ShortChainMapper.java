package com.fruit.task.master.core.mapper;
import com.fruit.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 短链API的Dao层
 * @Author yoko
 * @Date 2020/9/8 21:37
 * @Version 1.0
 */
@Mapper
public interface ShortChainMapper<T> extends BaseDao<T> {
}
