package com.fruit.task.master.core.mapper;
import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.model.bank.BankModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 银行的Dao层
 * @Author yoko
 * @Date 2020/5/18 19:04
 * @Version 1.0
 */
@Mapper
public interface BankMapper<T> extends BaseDao<T> {

    /**
     * @Description: 获取银行卡以及银行卡的放量策略数据
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/12 16:24
    */
    public List<BankModel> getBankAndStrategy(BankModel model);
}
