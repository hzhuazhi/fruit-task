package com.fruit.task.master.core.runner.task;

import com.fruit.task.master.core.common.utils.constant.CacheKey;
import com.fruit.task.master.core.common.utils.constant.CachedKeyUtils;
import com.fruit.task.master.core.common.utils.constant.TkCacheKey;
import com.fruit.task.master.core.model.issue.IssueModel;
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
        // 获取充值数据
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
        // 获取充值数据
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



    /**
     * @Description: 处理卡商充值-订单类型等于：下发订单（只更新卡商金额）
     * <p>
     *     每5秒运行一次
     *     1.查询未跑的充值信息
     *     2.根据充值中的归属卡商ID，更新卡商的金额（这里的金额是跑量金额，更新是扣减跑量金额用于抵消）
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 5000) // 每5秒执行
    public void handleRechargeByMoney() throws Exception{
//        log.info("----------------------------------TaskMerchantRecharge.handleRechargeByMoney()----start");
        // 获取充值数据
        MerchantRechargeModel merchantRechargeQuery = TaskMethod.assembleMerchantRechargeByTaskQuery(limitNum, 1, 0, 3, 3, 0,
                null,0,3, null);
        List<MerchantRechargeModel> synchroList = ComponentUtil.taskMerchantRechargeService.getDataList(merchantRechargeQuery);
        for (MerchantRechargeModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_MERCHANT_RECHARGE_MONEY, data.getId());
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
                                log.info("");
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
//                log.info("----------------------------------TaskMerchantRecharge.handleRechargeByMoney()----end");
            }catch (Exception e){
                log.error(String.format("this TaskMerchantRecharge.handleRechargeByMoney() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,0, 0,0,"异常失败try!");
                ComponentUtil.taskMerchantRechargeService.updateStatus(statusModel);
            }
        }
    }



    /**
     * @Description: 处理卡商充值-订单类型等于：下发订单（同步下发充值成功数据）
     * <p>
     *     每5秒运行一次
     *     1.查询未跑的充值信息
     *     2.把成功订单进行数据同步下发
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 5000) // 每5秒执行
    public void handleRechargeByIssue() throws Exception{
//        log.info("----------------------------------TaskMerchantRecharge.handleRechargeByIssue()----start");
        // 获取充值数据
        MerchantRechargeModel merchantRechargeQuery = TaskMethod.assembleMerchantRechargeByTaskQuery(limitNum, 0, 1, 3, 3, 0,
                null,0,0, null);
        List<MerchantRechargeModel> synchroList = ComponentUtil.taskMerchantRechargeService.getDataList(merchantRechargeQuery);
        for (MerchantRechargeModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_MERCHANT_RECHARGE_ISSUE, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;

                    // 根据订单号查询下发表的数据
                    IssueModel issueQuery = TaskMethod.assembleIssueByOrderQuery(data.getIssueOrderNo());
                    IssueModel issueModel = (IssueModel)ComponentUtil.issueService.findByObject(issueQuery);
                    if (issueModel == null || issueModel.getId() == null || issueModel.getId() <= 0){
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,"根据下发订单号未查到下发信息");
                        // 更新状态
                        ComponentUtil.taskMerchantRechargeService.updateStatus(statusModel);
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey);
                        continue;
                    }

                    // 更新下发信息
                    IssueModel issueUpdate = TaskMethod.assembleIssueUpdate(issueModel.getId(), null, null, 3, data.getPictureAds(),
                            null,0,0,0,0,null,null,0);
                    int num = ComponentUtil.issueService.update(issueUpdate);
                    if (num > 0){
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 3,0,null);
                    }else{
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,"更新卡商余额响应行为0");
                    }

                    if (statusModel != null){
                        ComponentUtil.taskMerchantRechargeService.updateStatus(statusModel);
                    }
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }
//                log.info("----------------------------------TaskMerchantRecharge.handleRechargeByIssue()----end");
            }catch (Exception e){
                log.error(String.format("this TaskMerchantRecharge.handleRechargeByIssue() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0,0, 2,0,"异常失败try!");
                ComponentUtil.taskMerchantRechargeService.updateStatus(statusModel);
            }
        }
    }



    /**
     * @Description: 处理卡商充值-订单类型等于：下发订单-运算超时订单
     * <p>
     *     每5秒运行一次
     *     1.查询订单状态属于初始化，订单类型属于下发订单，订单操作状态等于1，并且系统运算自动放弃时间小于系统时间的数据查询出来
     *     2.把以上符合条件的数据进行更新操作状态，更新成系统放弃状态
     *     3.把下发表的数据更新成未分配状态
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 5000) // 每5秒执行
    public void handleOperate() throws Exception{
//        log.info("----------------------------------TaskMerchantRecharge.handleOperate()----start");
        // 获取充值数据
        MerchantRechargeModel merchantRechargeQuery = TaskMethod.assembleMerchantRechargeByTaskQuery(limitNum, 0, 0, 3, 1, 1,
                null,0,0, "1");
        List<MerchantRechargeModel> synchroList = ComponentUtil.taskMerchantRechargeService.getDataList(merchantRechargeQuery);
        for (MerchantRechargeModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_MERCHANT_RECHARGE_OPERATE, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    // 根据订单号查询下发表的数据
                    IssueModel issueQuery = TaskMethod.assembleIssueByOrderQuery(data.getIssueOrderNo());
                    IssueModel issueModel = (IssueModel)ComponentUtil.issueService.findByObject(issueQuery);
                    if (issueModel == null || issueModel.getId() == null || issueModel.getId() <= 0){
                        // 更新状态
                        StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 0,0,"运算超时订单：根据下发订单号未查到下发信息");
                        ComponentUtil.taskMerchantRechargeService.updateStatus(statusModel);
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey);
                        continue;
                    }
                    MerchantRechargeModel merchantRechargeUpdate = TaskMethod.assembleMerchantRechargeUpdateOperate(data.getId(), 2, 1);
                    IssueModel issueUpdate = TaskMethod.assembleIssueUpdateDistribution(issueModel.getId(), 1, 2);
                    ComponentUtil.taskMerchantRechargeService.handleOperateStatus(merchantRechargeUpdate, issueUpdate);

                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }
//                log.info("----------------------------------TaskMerchantRecharge.handleOperate()----end");
            }catch (Exception e){
                log.error(String.format("this TaskMerchantRecharge.handleOperate() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0,0, 0,0,"异常失败try!");
                ComponentUtil.taskMerchantRechargeService.updateStatus(statusModel);
            }
        }
    }


}
