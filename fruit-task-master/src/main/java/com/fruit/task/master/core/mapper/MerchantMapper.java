package com.fruit.task.master.core.mapper;
import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.model.merchant.MerchantModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 卡商扩充数据的Dao层
 * @Author yoko
 * @Date 2020/9/8 17:05
 * @Version 1.0
 */
@Mapper
public interface MerchantMapper<T> extends BaseDao<T> {

    /**
     * @Description: 更新卡商的使用状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/5 19:21
     */
    public int updateUseStatus(MerchantModel model);

    /**
     * @Description: 更新卡商的余额
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/15 20:36
    */
    public int updateBalance(MerchantModel model);

    /**
     * @Description: 更新用户的总金额以及余额
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/21 18:47
    */
    public int updateMoney(MerchantModel model);

    /**
     * @Description: 扣除卡商的已跑量金额
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/23 21:51
    */
    public int updateDeductMoney(MerchantModel model);
}
