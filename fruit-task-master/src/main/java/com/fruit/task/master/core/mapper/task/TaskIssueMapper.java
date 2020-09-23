package com.fruit.task.master.core.mapper.task;

import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.model.issue.IssueModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task:下发的Dao层
 * @Author yoko
 * @Date 2020/9/23 18:42
 * @Version 1.0
 */
@Mapper
public interface TaskIssueMapper<T> extends BaseDao<T> {

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
