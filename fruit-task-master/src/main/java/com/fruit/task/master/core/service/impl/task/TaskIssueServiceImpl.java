package com.fruit.task.master.core.service.impl.task;

import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.mapper.task.TaskIssueMapper;
import com.fruit.task.master.core.model.issue.IssueModel;
import com.fruit.task.master.core.service.task.TaskIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:下发的Service层的实现层
 * @Author yoko
 * @Date 2020/9/23 18:45
 * @Version 1.0
 */
@Service
public class TaskIssueServiceImpl<T> extends BaseServiceImpl<T> implements TaskIssueService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskIssueMapper taskIssueMapper;



    public BaseDao<T> getDao() {
        return taskIssueMapper;
    }

    @Override
    public List<IssueModel> getDataList(Object obj) {
        return taskIssueMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskIssueMapper.updateStatus(obj);
    }
}
