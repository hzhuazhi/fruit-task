package com.fruit.task.master.core.service.impl;
import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.mapper.MerchantMapper;
import com.fruit.task.master.core.model.merchant.MerchantModel;
import com.fruit.task.master.core.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 卡商扩充数据的Service层的实现层
 * @Author yoko
 * @Date 2020/9/8 17:06
 * @Version 1.0
 */
@Service
public class MerchantServiceImpl<T> extends BaseServiceImpl<T> implements MerchantService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 3分钟
     */
    public long THREE_MIN = 180;

    /**
     * 11分钟.
     */
    public long ELEVEN_MIN = 660;

    public long TWO_HOUR = 2;



    @Autowired
    private MerchantMapper merchantMapper;

    public BaseDao<T> getDao() {
        return merchantMapper;
    }

    @Override
    public int updateUseStatus(MerchantModel model) {
        return merchantMapper.updateUseStatus(model);
    }

    @Override
    public int updateBalance(MerchantModel model) {
        return merchantMapper.updateBalance(model);
    }

    @Override
    public int updateMoney(MerchantModel model) {
        return merchantMapper.updateMoney(model);
    }
}
