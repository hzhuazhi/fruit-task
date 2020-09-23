package com.fruit.task.master.core.service.task;

import com.fruit.task.master.core.common.service.BaseService;
import com.fruit.task.master.core.model.issue.IssueModel;

import java.util.List;

/**
 * @Description task:下发的Service层
 * @Author yoko
 * @Date 2020/9/23 18:44
 * @Version 1.0
 */
public interface TaskIssueService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的下发信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<IssueModel> getDataList(Object obj);

    /**
     * @Description: 更新下发信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
