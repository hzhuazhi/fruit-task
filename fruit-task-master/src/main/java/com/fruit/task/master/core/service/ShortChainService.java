package com.fruit.task.master.core.service;

import com.fruit.task.master.core.common.service.BaseService;
import com.fruit.task.master.core.model.shortchain.ShortChainModel;

/**
 * @Description 短链API的Service层
 * @Author yoko
 * @Date 2020/9/8 21:39
 * @Version 1.0
 */
public interface ShortChainService<T> extends BaseService<T> {

    /**
     * @Description: 根据条件查询短代API数据
     * @param model - 查询条件
     * @param isCache - 是否通过缓存查询：0需要通过缓存查询，1直接查询数据库
     * @return
     * @author yoko
     * @date 2019/11/21 19:26
     */
    public ShortChainModel getShortChain(ShortChainModel model, int isCache) throws Exception;
}
