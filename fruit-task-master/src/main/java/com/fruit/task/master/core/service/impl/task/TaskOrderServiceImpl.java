package com.fruit.task.master.core.service.impl.task;

import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.exception.ServiceException;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.mapper.BankCollectionMapper;
import com.fruit.task.master.core.mapper.MerchantMapper;
import com.fruit.task.master.core.mapper.task.TaskOrderMapper;
import com.fruit.task.master.core.model.bank.BankCollectionModel;
import com.fruit.task.master.core.model.merchant.MerchantModel;
import com.fruit.task.master.core.model.order.OrderModel;
import com.fruit.task.master.core.service.task.TaskOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description task:订单的Service层的实现层
 * @Author yoko
 * @Date 2020/9/14 22:15
 * @Version 1.0
 */
@Service
public class TaskOrderServiceImpl<T> extends BaseServiceImpl<T> implements TaskOrderService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskOrderMapper taskOrderMapper;

    @Autowired
    private BankCollectionMapper bankCollectionMapper;

    @Autowired
    private MerchantMapper merchantMapper;



    public BaseDao<T> getDao() {
        return taskOrderMapper;
    }

    @Override
    public List<OrderModel> getDataList(Object obj) {
        return taskOrderMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskOrderMapper.updateStatus(obj);
    }

    @Override
    public List<OrderModel> getOrderNotifyList(Object obj) {
        return taskOrderMapper.getOrderNotifyList(obj);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public boolean handleSuccessOrder(BankCollectionModel bankCollectionModel, MerchantModel merchantUpdateMoney) throws Exception {
        int num1 = 0;
        int num2 = 0;

        num1 = bankCollectionMapper.add(bankCollectionModel);
        num2 = merchantMapper.updateMoney(merchantUpdateMoney);
        if (num1> 0 && num2 >0){
            return true;
        }else {
            throw new ServiceException("handleSuccessOrder", "二个执行更新SQL其中有一个或者多个响应行为0");
//                throw new RuntimeException();
        }
    }
}
