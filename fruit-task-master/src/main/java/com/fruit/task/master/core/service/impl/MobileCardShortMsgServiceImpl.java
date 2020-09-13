package com.fruit.task.master.core.service.impl;
import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.mapper.MobileCardShortMsgMapper;
import com.fruit.task.master.core.service.MobileCardShortMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 手机卡所有短信内容数据的Service层的实现层
 * @Author yoko
 * @Date 2020/9/11 11:57
 * @Version 1.0
 */
@Service
public class MobileCardShortMsgServiceImpl<T> extends BaseServiceImpl<T> implements MobileCardShortMsgService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private MobileCardShortMsgMapper mobileCardShortMsgMapper;

    public BaseDao<T> getDao() {
        return mobileCardShortMsgMapper;
    }
}
