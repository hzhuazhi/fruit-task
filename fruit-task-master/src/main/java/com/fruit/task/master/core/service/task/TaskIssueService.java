package com.fruit.task.master.core.service.task;

import com.fruit.task.master.core.common.service.BaseService;
import com.fruit.task.master.core.model.issue.IssueModel;
import com.fruit.task.master.core.model.merchant.MerchantModel;
import com.fruit.task.master.core.model.merchant.MerchantRechargeModel;

import java.util.List;

/**
 * @Description task:下发的Service层
 * @Author yoko
 * @Date 2020/9/23 18:44
 * @Version 1.0
 */
public interface TaskIssueService<T> extends BaseService<T> {

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

    /**
     * @Description: 处理下发订单分配给卡商的逻辑
     * <p>
     *     1.添加卡商充值信息
     *     2.更新卡商的已跑量金额
     * </p>
     * @param merchantRechargeAdd - 卡商充值信息
     * @param merchantUpdateMoney - 卡商金额更新
     * @return
     * @author yoko
     * @date 2020/9/23 21:40
    */
    public boolean handleDistribution(MerchantRechargeModel merchantRechargeAdd, MerchantModel merchantUpdateMoney) throws Exception;
}
