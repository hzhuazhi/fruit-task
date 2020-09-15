package com.fruit.task.master.core.mapper.task;

import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.model.shortmsg.ShortMsgArrearsModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task:手机卡欠费短信的Dao层
 * @Author yoko
 * @Date 2020/9/15 19:04
 * @Version 1.0
 */
@Mapper
public interface TaskShortMsgArrearsMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询未跑的欠费短信信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<ShortMsgArrearsModel> getDataList(Object obj);

    /**
     * @Description: 更新手机欠费信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
