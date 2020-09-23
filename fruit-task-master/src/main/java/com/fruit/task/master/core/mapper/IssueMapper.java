package com.fruit.task.master.core.mapper;

import com.fruit.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 下发的Dao层
 * @Author yoko
 * @Date 2020/9/23 11:43
 * @Version 1.0
 */
@Mapper
public interface IssueMapper<T> extends BaseDao<T> {
}
