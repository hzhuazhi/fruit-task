package com.fruit.task.master.core.mapper;
import com.fruit.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 商户与银行卡的绑定关系的Dao层
 * @Author yoko
 * @Date 2020/9/8 20:52
 * @Version 1.0
 */
@Mapper
public interface ChannelBankMapper<T> extends BaseDao<T> {
}
