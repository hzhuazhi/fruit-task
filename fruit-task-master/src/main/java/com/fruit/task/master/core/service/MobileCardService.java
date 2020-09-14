package com.fruit.task.master.core.service;

import com.fruit.task.master.core.common.service.BaseService;
import com.fruit.task.master.core.model.mobilecard.MobileCardModel;

/**
 * @Description 手机卡的Service层
 * @Author yoko
 * @Date 2020/5/18 17:22
 * @Version 1.0
 */
public interface MobileCardService<T> extends BaseService<T> {

    /**
     * @Description: 根据条件查询策略数据
     * @param model - 查询条件
     * @param isCache - 是否通过缓存查询：0需要通过缓存查询，1直接查询数据库
     * @return
     * @author yoko
     * @date 2019/11/21 19:26
     */
    public MobileCardModel getMobileCard(MobileCardModel model, int isCache) throws Exception;
}
