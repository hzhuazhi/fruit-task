package com.fruit.task.master.core.service.impl;
import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.mapper.OrderReplenishMapper;
import com.fruit.task.master.core.service.OrderReplenishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 订单补单的Service层的实现层
 * @Author yoko
 * @Date 2020/9/11 21:57
 * @Version 1.0
 */
@Service
public class OrderReplenishServiceImpl<T> extends BaseServiceImpl<T> implements OrderReplenishService<T> {

    private static Logger log = LoggerFactory.getLogger(OrderReplenishServiceImpl.class);

    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 15分钟.
     */
    public long FIFTEEN_MIN = 900;

    public long TWO_HOUR = 2;

    @Autowired
    private OrderReplenishMapper orderReplenishMapper;



    public BaseDao<T> getDao() {
        return orderReplenishMapper;
    }
}
