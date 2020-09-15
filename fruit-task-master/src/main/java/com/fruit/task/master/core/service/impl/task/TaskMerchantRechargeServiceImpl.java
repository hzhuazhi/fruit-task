package com.fruit.task.master.core.service.impl.task;

import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.mapper.task.TaskMerchantRechargeMapper;
import com.fruit.task.master.core.model.merchant.MerchantRechargeModel;
import com.fruit.task.master.core.service.task.TaskMerchantRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:卡商充值记录的Service层的实现层
 * @Author yoko
 * @Date 2020/9/15 20:09
 * @Version 1.0
 */
@Service
public class TaskMerchantRechargeServiceImpl<T> extends BaseServiceImpl<T> implements TaskMerchantRechargeService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskMerchantRechargeMapper taskMerchantRechargeMapper;



    public BaseDao<T> getDao() {
        return taskMerchantRechargeMapper;
    }

    @Override
    public List<MerchantRechargeModel> getDataList(Object obj) {
        return taskMerchantRechargeMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskMerchantRechargeMapper.updateStatus(obj);
    }
}
