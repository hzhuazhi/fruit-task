package com.fruit.task.master.core.mapper;
import com.fruit.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 手机卡所有短信内容数据的Dao层
 * @Author yoko
 * @Date 2020/9/11 11:55
 * @Version 1.0
 */
@Mapper
public interface MobileCardShortMsgMapper<T> extends BaseDao<T> {
}
