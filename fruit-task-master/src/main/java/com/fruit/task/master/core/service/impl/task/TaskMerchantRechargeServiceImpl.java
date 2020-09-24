package com.fruit.task.master.core.service.impl.task;

import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.exception.ServiceException;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.mapper.IssueMapper;
import com.fruit.task.master.core.mapper.MerchantRechargeMapper;
import com.fruit.task.master.core.mapper.task.TaskMerchantRechargeMapper;
import com.fruit.task.master.core.model.issue.IssueModel;
import com.fruit.task.master.core.model.merchant.MerchantRechargeModel;
import com.fruit.task.master.core.service.task.TaskMerchantRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private MerchantRechargeMapper merchantRechargeMapper;

    @Autowired
    private IssueMapper issueMapper;



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

    @Transactional(rollbackFor=Exception.class)
    @Override
    public boolean handleOperateStatus(MerchantRechargeModel merchantRechargeModel, IssueModel issueModel) throws Exception {
        int num1 = 0;
        int num2 = 0;

        num1 = merchantRechargeMapper.updateOperateStatus(merchantRechargeModel);
        num2 = issueMapper.updateDistribution(issueModel);
        if (num1> 0 && num2 >0){
            return true;
        }else {
            throw new ServiceException("handleOperateStatus", "二个执行更新SQL其中有一个或者多个响应行为0");
//                throw new RuntimeException();
        }
    }
}
