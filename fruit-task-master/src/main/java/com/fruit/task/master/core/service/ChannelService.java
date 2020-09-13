package com.fruit.task.master.core.service;

import com.fruit.task.master.core.common.service.BaseService;
import com.fruit.task.master.core.model.channel.ChannelModel;

/**
 * @Description 商户信息的Service层
 * @Author yoko
 * @Date 2020/9/8 20:53
 * @Version 1.0
 */
public interface ChannelService<T> extends BaseService<T> {

    /**
     * @Description: 根据条件查询商户数据
     * @param model - 查询条件
     * @param isCache - 是否通过缓存查询：0需要通过缓存查询，1直接查询数据库
     * @return
     * @author yoko
     * @date 2019/11/21 19:26
     */
    public ChannelModel getChannel(ChannelModel model, int isCache) throws Exception;
}
