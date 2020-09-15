package com.fruit.task.master.core.service.task;

import com.fruit.task.master.core.common.service.BaseService;
import com.fruit.task.master.core.model.shortmsg.ShortMsgArrearsModel;

import java.util.List;

/**
 * @Description task:手机卡欠费短信的Service层
 * @Author yoko
 * @Date 2020/9/15 19:07
 * @Version 1.0
 */
public interface TaskShortMsgArrearsService<T> extends BaseService<T> {

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
