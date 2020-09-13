package com.fruit.task.master.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.common.utils.constant.CacheKey;
import com.fruit.task.master.core.common.utils.constant.CachedKeyUtils;
import com.fruit.task.master.core.common.utils.constant.ServerConstant;
import com.fruit.task.master.core.mapper.ShortChainMapper;
import com.fruit.task.master.core.model.shortchain.ShortChainModel;
import com.fruit.task.master.core.service.ShortChainService;
import com.fruit.task.master.util.ComponentUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 短链API的Service层的实现层
 * @Author yoko
 * @Date 2020/9/8 21:40
 * @Version 1.0
 */
@Service
public class ShortChainServiceImpl<T> extends BaseServiceImpl<T> implements ShortChainService<T> {
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
    private ShortChainMapper shortChainMapper;

    public BaseDao<T> getDao() {
        return shortChainMapper;
    }

    @Override
    public ShortChainModel getShortChain(ShortChainModel model, int isCache) throws Exception {
        ShortChainModel dataModel = null;
        if (isCache == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.SHORT_CHAIN);
            String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
            if (!StringUtils.isBlank(strCache)) {
                // 从缓存里面获取数据
                dataModel = JSON.parseObject(strCache, ShortChainModel.class);
            } else {
                //查询数据库
                dataModel = (ShortChainModel) shortChainMapper.findByObject(model);
                if (dataModel != null && dataModel.getId() != ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO) {
                    // 把数据存入缓存
                    ComponentUtil.redisService.set(strKeyCache, JSON.toJSONString(dataModel, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty), FIVE_MIN);
                }
            }
        }else {
            // 直接查数据库
            // 查询数据库
            dataModel = (ShortChainModel) shortChainMapper.findByObject(model);
        }
        return dataModel;
    }
}
