package com.fruit.task.master.core.service.task;

import com.fruit.task.master.core.common.service.BaseService;
import com.fruit.task.master.core.model.mobilecard.MobileCardShortMsgModel;

import java.util.List;

/**
 * @Description task:手机卡所有短信内容数据的Service层
 * @Author yoko
 * @Date 2020/9/13 21:42
 * @Version 1.0
 */
public interface TaskMobileCardShortMsgService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的手机短信信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<MobileCardShortMsgModel> getDataList(Object obj);

    /**
     * @Description: 更新手机短信信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
