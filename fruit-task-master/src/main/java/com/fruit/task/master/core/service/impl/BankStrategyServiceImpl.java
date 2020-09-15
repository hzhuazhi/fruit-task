package com.fruit.task.master.core.service.impl;
import com.fruit.task.master.core.common.dao.BaseDao;
import com.fruit.task.master.core.common.service.impl.BaseServiceImpl;
import com.fruit.task.master.core.common.utils.DateUtil;
import com.fruit.task.master.core.common.utils.StringUtil;
import com.fruit.task.master.core.common.utils.constant.CacheKey;
import com.fruit.task.master.core.common.utils.constant.CachedKeyUtils;
import com.fruit.task.master.core.mapper.BankStrategyMapper;
import com.fruit.task.master.core.model.bank.BankStrategyModel;
import com.fruit.task.master.core.service.BankStrategyService;
import com.fruit.task.master.util.ComponentUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Description 银行卡放量策略的Service层的实现层
 * @Author yoko
 * @Date 2020/9/11 16:41
 * @Version 1.0
 */
@Service
public class BankStrategyServiceImpl<T> extends BaseServiceImpl<T> implements BankStrategyService<T> {
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
    private BankStrategyMapper bankStrategyMapper;

    public BaseDao<T> getDao() {
        return bankStrategyMapper;
    }

    @Override
    public void bankStrategyLimit(BankStrategyModel bankStrategyModel, int payType, int dayNum, String dayMoney, String monthMoney) {
        if (payType == 1){
            String dayMoney_redis = getRedisDataByKey(CacheKey.WX_IN_DAY_MONEY, bankStrategyModel.getBankId());
            if (StringUtils.isBlank(dayMoney_redis)){
                if (!StringUtils.isBlank(bankStrategyModel.getWxInDayMoney())){
                    // 判断日金额是否超过规定金额
                    boolean flag = StringUtil.getBigDecimalSubtract(bankStrategyModel.getWxInDayMoney(), dayMoney);
                    if (!flag){
                        // 超过限量额度，redis存储
                        // 缓存设置：失效时间当日的凌晨零点
                        long time = DateUtil.getTomorrowMinute();
                        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.WX_IN_DAY_MONEY, bankStrategyModel.getBankId());
                        ComponentUtil.redisService.set(strKeyCache, dayMoney, time);
                    }
                }
            }
            String monthMoney_redis = getRedisDataByKey(CacheKey.WX_IN_MONTH_MONEY, bankStrategyModel.getBankId());
            if (StringUtils.isBlank(monthMoney_redis)){
                if (!StringUtils.isBlank(bankStrategyModel.getWxInMonthMoney())){
                    // 判断月金额是否超过规定金额
                    boolean flag = StringUtil.getBigDecimalSubtract(bankStrategyModel.getWxInMonthMoney(), monthMoney);
                    if (!flag){
                        // 超过限量额度，redis存储
                        // 缓存设置：失效时间当前时间到月底的天数
                        int time = DateUtil.differByEndMonth();
                        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.WX_IN_MONTH_MONEY, bankStrategyModel.getBankId());
                        ComponentUtil.redisService.set(strKeyCache, monthMoney, time, TimeUnit.DAYS);
                    }
                }
            }
            String dayNum_redis = getRedisDataByKey(CacheKey.WX_IN_DAY_NUM, bankStrategyModel.getBankId());
            if (StringUtils.isBlank(dayNum_redis)){
                if (bankStrategyModel.getWxInDayNum() != null && bankStrategyModel.getWxInDayNum() > 0){
                    // 判断日日数是否超过规定次数
                    if (dayNum >= bankStrategyModel.getWxInDayNum()){
                        // 缓存设置：失效时间当日的凌晨零点
                        long time = DateUtil.getTomorrowMinute();
                        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.WX_IN_DAY_NUM, bankStrategyModel.getBankId());
                        ComponentUtil.redisService.set(strKeyCache, String.valueOf(dayNum), time);
                    }
                }
            }

        }else if (payType == 2){
            String dayMoney_redis = getRedisDataByKey(CacheKey.ZFB_IN_DAY_MONEY, bankStrategyModel.getBankId());
            if (StringUtils.isBlank(dayMoney_redis)){
                if (!StringUtils.isBlank(bankStrategyModel.getZfbInDayMoney())){
                    // 判断日金额是否超过规定金额
                    boolean flag = StringUtil.getBigDecimalSubtract(bankStrategyModel.getZfbInDayMoney(), dayMoney);
                    if (!flag){
                        // 超过限量额度，redis存储
                        // 缓存设置：失效时间当日的凌晨零点
                        long time = DateUtil.getTomorrowMinute();
                        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.ZFB_IN_DAY_MONEY, bankStrategyModel.getBankId());
                        ComponentUtil.redisService.set(strKeyCache, dayMoney, time);
                    }
                }
            }
            String monthMoney_redis = getRedisDataByKey(CacheKey.ZFB_IN_MONTH_MONEY, bankStrategyModel.getBankId());
            if (StringUtils.isBlank(monthMoney_redis)){
                if (!StringUtils.isBlank(bankStrategyModel.getZfbInMonthMoney())){
                    // 判断月金额是否超过规定金额
                    boolean flag = StringUtil.getBigDecimalSubtract(bankStrategyModel.getZfbInMonthMoney(), monthMoney);
                    if (!flag){
                        // 超过限量额度，redis存储
                        // 缓存设置：失效时间当前时间到月底的天数
                        int time = DateUtil.differByEndMonth();
                        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.ZFB_IN_MONTH_MONEY, bankStrategyModel.getBankId());
                        ComponentUtil.redisService.set(strKeyCache, monthMoney, time, TimeUnit.DAYS);
                    }
                }
            }
            String dayNum_redis = getRedisDataByKey(CacheKey.ZFB_IN_DAY_NUM, bankStrategyModel.getBankId());
            if (StringUtils.isBlank(dayNum_redis)){
                if (bankStrategyModel.getZfbInDayNum() != null && bankStrategyModel.getZfbInDayNum() > 0){
                    // 判断日日数是否超过规定次数
                    if (dayNum >= bankStrategyModel.getZfbInDayNum()){
                        // 缓存设置：失效时间当日的凌晨零点
                        long time = DateUtil.getTomorrowMinute();
                        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.ZFB_IN_DAY_NUM, bankStrategyModel.getBankId());
                        ComponentUtil.redisService.set(strKeyCache, String.valueOf(dayNum), time);
                    }
                }
            }
        }else if (payType == 3){
            String dayMoney_redis = getRedisDataByKey(CacheKey.CARD_IN_DAY_MONEY, bankStrategyModel.getBankId());
            if (StringUtils.isBlank(dayMoney_redis)){
                if (!StringUtils.isBlank(bankStrategyModel.getCardInDayMoney())){
                    // 判断日金额是否超过规定金额
                    boolean flag = StringUtil.getBigDecimalSubtract(bankStrategyModel.getCardInDayMoney(), dayMoney);
                    if (!flag){
                        // 超过限量额度，redis存储
                        // 缓存设置：失效时间当日的凌晨零点
                        long time = DateUtil.getTomorrowMinute();
                        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.CARD_IN_DAY_MONEY, bankStrategyModel.getBankId());
                        ComponentUtil.redisService.set(strKeyCache, dayMoney, time);
                    }
                }
            }
            String monthMoney_redis = getRedisDataByKey(CacheKey.CARD_IN_MONTH_MONEY, bankStrategyModel.getBankId());
            if (StringUtils.isBlank(monthMoney_redis)){
                if (!StringUtils.isBlank(bankStrategyModel.getCardInMonthMoney())){
                    // 判断月金额是否超过规定金额
                    boolean flag = StringUtil.getBigDecimalSubtract(bankStrategyModel.getCardInMonthMoney(), monthMoney);
                    if (!flag){
                        // 超过限量额度，redis存储
                        // 缓存设置：失效时间当前时间到月底的天数
                        int time = DateUtil.differByEndMonth();
                        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.CARD_IN_MONTH_MONEY, bankStrategyModel.getBankId());
                        ComponentUtil.redisService.set(strKeyCache, monthMoney, time, TimeUnit.DAYS);
                    }
                }
            }
            String dayNum_redis = getRedisDataByKey(CacheKey.CARD_IN_DAY_NUM, bankStrategyModel.getBankId());
            if (StringUtils.isBlank(dayNum_redis)){
                if (bankStrategyModel.getCardInDayNum() != null && bankStrategyModel.getCardInDayNum() > 0){
                    // 判断日日数是否超过规定次数
                    if (dayNum >= bankStrategyModel.getCardInDayNum()){
                        // 缓存设置：失效时间当日的凌晨零点
                        long time = DateUtil.getTomorrowMinute();
                        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.CARD_IN_DAY_NUM, bankStrategyModel.getBankId());
                        ComponentUtil.redisService.set(strKeyCache, String.valueOf(dayNum), time);
                    }
                }
            }

        }
    }



    /**
     * @Description: 组装缓存key查询缓存中存在的数据
     * @param cacheKey - 缓存的类型key
     * @param obj - 数据的ID
     * @return
     * @author yoko
     * @date 2020/5/20 14:59
     */
    public String getRedisDataByKey(String cacheKey, Object obj){
        String str = null;
        String strKeyCache = CachedKeyUtils.getCacheKey(cacheKey, obj);
        String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
        if (StringUtils.isBlank(strCache)){
            return str;
        }else{
            str = strCache;
            return str;
        }
    }
}
