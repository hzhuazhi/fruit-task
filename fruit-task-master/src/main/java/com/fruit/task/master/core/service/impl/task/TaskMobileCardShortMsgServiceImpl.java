package com.fruit.task.master.core.service.impl.task;

import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.mapper.task.TaskMobileCardShortMsgMapper;
import com.fruit.task.master.core.model.mobilecard.MobileCardShortMsgModel;
import com.fruit.task.master.core.service.task.TaskMobileCardShortMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:手机卡所有短信内容数据的Service层的实现层
 * @Author yoko
 * @Date 2020/9/13 21:44
 * @Version 1.0
 */
@Service
public class TaskMobileCardShortMsgServiceImpl<T> extends BaseServiceImpl<T> implements TaskMobileCardShortMsgService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskMobileCardShortMsgMapper taskMobileCardShortMsgMapper;



    public BaseDao<T> getDao() {
        return taskMobileCardShortMsgMapper;
    }


    @Override
    public List<MobileCardShortMsgModel> getDataList(Object obj) {
        return taskMobileCardShortMsgMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskMobileCardShortMsgMapper.updateStatus(obj);
    }
}
