package com.fruit.task.master.core.mapper.task;

import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.model.bank.BankShortMsgModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task:银行卡短信数据的Dao层
 * @Author yoko
 * @Date 2020/9/14 15:28
 * @Version 1.0
 */
@Mapper
public interface TaskBankShortMsgMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询未跑的银行卡短信信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<BankShortMsgModel> getDataList(Object obj);

    /**
     * @Description: 更新银行卡短信信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
