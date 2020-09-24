package com.fruit.task.master.core.service.task;

import com.fruit.task.master.core.common.service.BaseService;
import com.fruit.task.master.core.model.issue.IssueModel;
import com.fruit.task.master.core.model.merchant.MerchantRechargeModel;

import java.util.List;

/**
 * @Description task:卡商充值记录的Service层
 * @Author yoko
 * @Date 2020/9/15 20:09
 * @Version 1.0
 */
public interface TaskMerchantRechargeService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的卡商充值信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<MerchantRechargeModel> getDataList(Object obj);

    /**
     * @Description: 更新卡商充值信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);

    /**
     * @Description: 处理操作状态的方法
     * <p>
     *     1.更新操作状态：更新成放弃
     *     2.更新下发的分配状态：更新成未分配
     * </p>
     * @param merchantRechargeModel - 充值订单
     * @param issueModel - 下发信息
     * @return
     * @author yoko
     * @date 2020/9/24 21:59
    */
    public boolean handleOperateStatus(MerchantRechargeModel merchantRechargeModel, IssueModel issueModel) throws Exception;
}
