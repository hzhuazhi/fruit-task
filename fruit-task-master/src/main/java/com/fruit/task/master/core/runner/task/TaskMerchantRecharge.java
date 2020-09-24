package com.fruit.task.master.core.runner.task;

import com.fruit.task.master.core.common.utils.constant.CacheKey;
import com.fruit.task.master.core.common.utils.constant.CachedKeyUtils;
import com.fruit.task.master.core.common.utils.constant.TkCacheKey;
import com.fruit.task.master.core.model.merchant.MerchantModel;
import com.fruit.task.master.core.model.merchant.MerchantRechargeModel;
import com.fruit.task.master.core.model.task.base.StatusModel;
import com.fruit.task.master.util.ComponentUtil;
import com.fruit.task.master.util.TaskMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description task:卡商充值记录
 * @Author yoko
 * @Date 2020/9/15 20:06
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskMerchantRecharge {

    private final static Logger log = LoggerFactory.getLogger(TaskMerchantRecharge.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: 处理卡商充值-订单类型等于：预付款订单
     * <p>
     *     每5秒运行一次
     *     1.查询未跑的充值信息
     *     2.根据充值中的归属卡商ID，更新卡商的金额（这里的金额是预付款金额，保证金）
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 5000) // 每5秒执行
    public void handleRecharge() throws Exception{
//        log.info("----------------------------------TaskMerchantRecharge.handleRecharge()----start");
        // 获取订单补单数据
        MerchantRechargeModel merchantRechargeQuery = TaskMethod.assembleMerchantRechargeByTaskQuery(limitNum, 1, 0, 1, 3, 0,
                null,0,3, null);
        List<MerchantRechargeModel> synchroList = ComponentUtil.taskMerchantRechargeService.getDataList(merchantRechargeQuery);
        for (MerchantRechargeModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_MERCHANT_RECHARGE, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;

                    // 获取卡商扩展信息
                    MerchantModel merchantQuery = TaskMethod.assembleMerchantQuery(0, data.getAccountId(), 0);
                    List<MerchantModel> merchantList = ComponentUtil.merchantService.findByCondition(merchantQuery);
                    if (merchantList == null || merchantList.size() <= 0){
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"根据卡商账号ID未查到卡商扩展信息");
                        // 更新状态
                        ComponentUtil.taskMerchantRechargeService.updateStatus(statusModel);
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey);
                        continue;
                    }

                    if (merchantList != null && merchantList.size() > 0){
                        if (merchantList.size() > 1){
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"根据卡商账号ID未查到多条卡商的扩展信息");
                            // 更新状态
                            ComponentUtil.taskMerchantRechargeService.updateStatus(statusModel);
                            // 解锁
                            ComponentUtil.redisIdService.delLock(lockKey);
                            continue;
                        }



                        // 正式更新卡商扩展信息的余额
                        MerchantModel merchantUpdate = TaskMethod.assembleMerchantUpdateMoney(data.getAccountId(), data.getOrderMoney());

                        // 锁住此卡商账号ID

                        String lockKey_account = CachedKeyUtils.getCacheKeyTask(CacheKey.LOCK_ACCOUNT_ID, data.getAccountId());
                        boolean flagLock_account = ComponentUtil.redisIdService.lock(lockKey_account);
                        if (flagLock_account){
                            int num = ComponentUtil.merchantService.updateLeastMoney(merchantUpdate);
                            if (num > 0){
                                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);
                            }else{
                                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"更新卡商余额响应行为0");
                            }
                            // 解锁
                            ComponentUtil.redisIdService.delLock(lockKey_account);
                            // 更新状态
                            ComponentUtil.taskMerchantRechargeService.updateStatus(statusModel);
                        }
                    }

                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }
//                log.info("----------------------------------TaskMerchantRecharge.handleRecharge()----end");
            }catch (Exception e){
                log.error(String.format("this TaskMerchantRecharge.handleRecharge() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,0, 0,0,"异常失败try!");
                ComponentUtil.taskMerchantRechargeService.updateStatus(statusModel);
            }
        }
    }



    /**
     * @Description: 处理卡商充值-订单类型等于：平台发起订单
     * <p>
     *     每5秒运行一次
     *     1.查询未跑的充值信息
     *     2.根据充值中的归属卡商ID，更新卡商的金额（这里的金额是跑量金额，更新是扣减跑量金额用于抵消）
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 5000) // 每5秒执行
    public void handleRechargeByBalance() throws Exception{
//        log.info("----------------------------------TaskMerchantRecharge.handleRechargeByBalance()----start");
        // 获取订单补单数据
        MerchantRechargeModel merchantRechargeQuery = TaskMethod.assembleMerchantRechargeByTaskQuery(limitNum, 1, 0, 2, 3, 0,
                null,0,3, null);
        List<MerchantRechargeModel> synchroList = ComponentUtil.taskMerchantRechargeService.getDataList(merchantRechargeQuery);
        for (MerchantRechargeModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_MERCHANT_RECHARGE_BALANCE, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;

                    // 获取卡商扩展信息
                    MerchantModel merchantQuery = TaskMethod.assembleMerchantQuery(0, data.getAccountId(), 0);
                    List<MerchantModel> merchantList = ComponentUtil.merchantService.findByCondition(merchantQuery);
                    if (merchantList == null || merchantList.size() <= 0){
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"根据卡商账号ID未查到卡商扩展信息");
                        // 更新状态
                        ComponentUtil.taskMerchantRechargeService.updateStatus(statusModel);
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey);
                        continue;
                    }

                    if (merchantList != null && merchantList.size() > 0){
                        if (merchantList.size() > 1){
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"根据卡商账号ID未查到多条卡商的扩展信息");
                            // 更新状态
                            ComponentUtil.taskMerchantRechargeService.updateStatus(statusModel);
                            // 解锁
                            ComponentUtil.redisIdService.delLock(lockKey);
                            continue;
                        }



                        // 正式更新卡商扩展信息的余额
                        MerchantModel merchantUpdate = TaskMethod.assembleMerchantUpdateMoney(data.getAccountId(), data.getOrderMoney());

                        // 锁住此卡商
                        String lockKey_account = CachedKeyUtils.getCacheKeyTask(CacheKey.LOCK_MERCHANT_MONEY, data.getAccountId());
                        boolean flagLock_account = ComponentUtil.redisIdService.lock(lockKey_account);
                        if (flagLock_account){
                            int num = ComponentUtil.merchantService.updateDeductMoney(merchantUpdate);
                            if (num > 0){
                                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);
                            }else{
                                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"更新卡商余额响应行为0");
                            }
                            // 解锁
                            ComponentUtil.redisIdService.delLock(lockKey_account);
                            // 更新状态
                            ComponentUtil.taskMerchantRechargeService.updateStatus(statusModel);
                        }
                    }

                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }
//                log.info("----------------------------------TaskMerchantRecharge.handleRechargeByBalance()----end");
            }catch (Exception e){
                log.error(String.format("this TaskMerchantRecharge.handleRechargeByBalance() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,0, 0,0,"异常失败try!");
                ComponentUtil.taskMerchantRechargeService.updateStatus(statusModel);
            }
        }
    }


}
