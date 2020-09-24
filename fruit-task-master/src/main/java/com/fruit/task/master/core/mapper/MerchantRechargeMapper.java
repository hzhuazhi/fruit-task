package com.fruit.task.master.core.mapper;
import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.model.merchant.MerchantRechargeModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 卡商充值记录的Dao层
 * @Author yoko
 * @Date 2020/9/8 18:00
 * @Version 1.0
 */
@Mapper
public interface MerchantRechargeMapper<T> extends BaseDao<T> {

    /**
     * @Description: 根据条件查询求和订单金额
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/24 15:09
    */
    public String sumMoneyByOrder(MerchantRechargeModel model);

    /**
     * @Description: 更新操作状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/24 21:46
     */
    public int updateOperateStatus(MerchantRechargeModel model);
}
