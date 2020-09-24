package com.fruit.task.master.core.service;


import com.fruit.task.master.core.common.service.BaseService;
import com.fruit.task.master.core.model.issue.IssueModel;

/**
 * @Description 下发的Service层
 * @Author yoko
 * @Date 2020/9/23 13:41
 * @Version 1.0
 */
public interface IssueService<T> extends BaseService<T> {

    /**
     * @Description: 更新是否分配完毕归属
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/24 21:51
     */
    public int updateDistribution(IssueModel model);
}
