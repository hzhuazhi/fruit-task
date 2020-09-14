package com.fruit.task.master.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.common.utils.constant.CacheKey;
import com.fruit.task.master.core.common.utils.constant.CachedKeyUtils;
import com.fruit.task.master.core.common.utils.constant.ServerConstant;
import com.fruit.task.master.core.mapper.ShortMsgStrategyMapper;
import com.fruit.task.master.core.model.shortmsg.ShortMsgStrategyModel;
import com.fruit.task.master.core.service.ShortMsgStrategyService;
import com.fruit.task.master.util.ComponentUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description 短信的类型定位策略的Service层的实现层
 * @Author yoko
 * @Date 2020/9/11 14:58
 * @Version 1.0
 */
@Service
public class ShortMsgStrategyServiceImpl<T> extends BaseServiceImpl<T> implements ShortMsgStrategyService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private ShortMsgStrategyMapper shortMsgStrategyMapper;

    public BaseDao<T> getDao() {
        return shortMsgStrategyMapper;
    }


    @Override
    public List<ShortMsgStrategyModel> getShortMsgStrategyList(ShortMsgStrategyModel model, int isCache) throws Exception {
        List<ShortMsgStrategyModel> dataList = null;
        if (isCache == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.SHORT_MSG_STRATEGY_TWO, model.getShortMsgType());
            String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
            if (!StringUtils.isBlank(strCache)) {
                // 从缓存里面获取数据
                dataList = JSON.parseArray(strCache, ShortMsgStrategyModel.class);
            } else {
                //查询数据库
                dataList = (List<ShortMsgStrategyModel>) shortMsgStrategyMapper.findByCondition(model);
                if (dataList != null && dataList.size() != ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
                    // 把数据存入缓存
                    ComponentUtil.redisService.set(strKeyCache, JSON.toJSONString(dataList, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty), FIVE_MIN);
                }
            }
        }else {
            // 直接查数据库
            // 查询数据库
            dataList = (List<ShortMsgStrategyModel>) shortMsgStrategyMapper.findByCondition(model);
        }
        return dataList;
    }
}
