package com.fruit.task.master.core.mapper;
import com.fruit.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 商户信息的Dao层
 * @Author yoko
 * @Date 2020/9/8 20:52
 * @Version 1.0
 */
@Mapper
public interface ChannelMapper<T> extends BaseDao<T> {
}
