package com.fruit.task.master.core.service.impl;
import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.common.utils.constant.CacheKey;
import com.fruit.task.master.core.common.utils.constant.CachedKeyUtils;
import com.fruit.task.master.core.mapper.OrderMapper;
import com.fruit.task.master.core.model.bank.BankModel;
import com.fruit.task.master.core.model.order.OrderModel;
import com.fruit.task.master.core.service.OrderService;
import com.fruit.task.master.util.ComponentUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description 任务订单的Service层的实现层
 * @Author yoko
 * @Date 2020/5/21 19:35
 * @Version 1.0
 */
@Service
public class OrderServiceImpl<T> extends BaseServiceImpl<T> implements OrderService<T> {

    private static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

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
    private OrderMapper orderMapper;



    public BaseDao<T> getDao() {
        return orderMapper;
    }




}
