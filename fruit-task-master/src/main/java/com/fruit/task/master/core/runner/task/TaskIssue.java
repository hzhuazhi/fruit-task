package com.fruit.task.master.core.runner.task;

import com.alibaba.fastjson.JSON;
import com.fruit.task.master.core.common.utils.DateUtil;
import com.fruit.task.master.core.common.utils.HttpSendUtils;
import com.fruit.task.master.core.common.utils.StringUtil;
import com.fruit.task.master.core.common.utils.constant.CacheKey;
import com.fruit.task.master.core.common.utils.constant.CachedKeyUtils;
import com.fruit.task.master.core.common.utils.constant.ServerConstant;
import com.fruit.task.master.core.common.utils.constant.TkCacheKey;
import com.fruit.task.master.core.model.issue.IssueModel;
import com.fruit.task.master.core.model.merchant.MerchantModel;
import com.fruit.task.master.core.model.merchant.MerchantRechargeModel;
import com.fruit.task.master.core.model.strategy.StrategyModel;
import com.fruit.task.master.util.ComponentUtil;
import com.fruit.task.master.util.TaskMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description task:下发
 * @Author yoko
 * @Date 2020/9/23 19:07
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskIssue {

    private final static Logger log = LoggerFactory.getLogger(TaskIssue.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: 检测未分配的数据进行分配
     * <p>
     *     每10秒执行运行一次
     *     1.查询未分配的下发数据。
     *     2.查询哪些卡商符合分配的条件
     *     3.如果轮询卡商不能分配则分配给平台
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "5 * * * * ?")
//    @Scheduled(fixedDelay = 1000) // 每1秒执行
    @Scheduled(fixedDelay = 10000) // 每10秒执行
    public void issueDistribution() throws Exception{
//        log.info("----------------------------------TaskIssue.issueDistribution()----start");
        // 策略：下发分配次数
        int issueDistributionNum = 0;
        StrategyModel strategyIssueDistributionNumQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.ISSUE_DISTRIBUTION_NUM.getStgType());
        StrategyModel strategyIssueDistributionNumModel = ComponentUtil.strategyService.getStrategyModel(strategyIssueDistributionNumQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        issueDistributionNum = strategyIssueDistributionNumModel.getStgNumValue();

        // 策略：下发分配后的操作时间
        int issueDistributionTime = 0;
        StrategyModel strategyIssueDistributionTimeQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.ISSUE_DISTRIBUTION_TIME.getStgType());
        StrategyModel strategyIssueDistributionTimeModel = ComponentUtil.strategyService.getStrategyModel(strategyIssueDistributionTimeQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        issueDistributionTime = strategyIssueDistributionTimeModel.getStgNumValue();

        // 获取未填充可爱猫回调店员绑定小微的数据
        IssueModel issueQuery = TaskMethod.assembleIssueQuery(limitNum,0,0, 0, 0, 1, 0,0,0);
        List<IssueModel> synchroList = ComponentUtil.taskIssueService.getDataList(issueQuery);
        for (IssueModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_ISSUE_DISTRIBUTION, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    IssueModel issueUpdate = null;

                    int ascriptionType = 0;// 订单分配归属类型：1归属卡商，2归属平台
                    List<Long> accountIdList = null;
                    // 查询是否有已经分配的纪录
                    MerchantRechargeModel merchantRechargeQuery = TaskMethod.assembleMerchantRechargeQuery(0,0,null, 3, data.getOrderNo(),
                            0, 0, 0,0,null,null, "1");
                    List<MerchantRechargeModel> merchantRechargeList = ComponentUtil.merchantRechargeService.findByCondition(merchantRechargeQuery);
                    if (merchantRechargeList != null && merchantRechargeList.size() > 0){
                        // 判断分配给卡商的次数是否大于等于部署的次数
                        if (merchantRechargeList.size() >= issueDistributionNum){
                            // 已经超过部署分配的次数：直接分配给平台
                            ascriptionType = 2;
                        }else{
                            accountIdList = merchantRechargeList.stream().map(MerchantRechargeModel::getAccountId).collect(Collectors.toList());
                        }
                    }

                    MerchantModel merchantData = null;
                    if (ascriptionType != 2){
                        // 表示没有超过分配次数：继续查询卡商用户
                        MerchantModel merchantQuery = TaskMethod.assembleMerchantQuery(0,0, data.getOrderMoney(),2, 1, accountIdList);
                        MerchantModel merchantModel = (MerchantModel)ComponentUtil.merchantService.findByObject(merchantQuery);
                        if (merchantModel != null && merchantModel.getId() != null && merchantModel.getId() > 0){
                            // 这里表示抛开之前分配的卡商，还有卡商符合分配
                            merchantData = merchantModel;
                            ascriptionType = 1;
                        }else {
                            // 这里表示抛开之前分配的卡商，没有卡商符合分配：直接分配给平台
                            ascriptionType = 2;
                        }
                    }

                    if (ascriptionType == 1){
                        // 分配给卡商
                        // 锁住这个卡商
                        String lockKey_accountId = CachedKeyUtils.getCacheKey(CacheKey.LOCK_MERCHANT_MONEY, merchantData.getAccountId());
                        boolean flagLock_accountId = ComponentUtil.redisIdService.lock(lockKey_accountId);
                        if (flagLock_accountId){
                            String orderNo = ComponentUtil.redisIdService.getNewId();
                            String invalidTime = DateUtil.addDateMinute(issueDistributionTime);
                            // 组装卡商充值订单信息
                            MerchantRechargeModel merchantRechargeAdd = TaskMethod.assembleMerchantRechargeAdd(merchantData.getAccountId(), orderNo, 3, data.getOrderNo(),
                                    data.getOrderMoney(), data.getBankName(), data.getBankCard(), data.getAccountName(), 2, invalidTime);
                            // 组装卡商金额更新
                            MerchantModel merchantUpdate = TaskMethod.assembleMerchantUpdateMoney(merchantData.getAccountId(), data.getOrderMoney());
                            boolean flag = ComponentUtil.taskIssueService.handleDistribution(merchantRechargeAdd, merchantUpdate);
                            if (flag){
                                // 正常执行：状态修改成分配成功，是分配给卡商的
                                issueUpdate = TaskMethod.assembleIssueUpdate(data.getId(), null, null, 0, null, null, 1, 2, 0, 0, null, null, 0);
                            }else {
                                // 事物有误，不做任何处理，等待下一次
                            }

                        }else{
                            // 其它地方正在使用：这里无需做任何动作，等待下一次
                        }
                    }else if (ascriptionType == 2){
                        // 分配给平台
                        issueUpdate = TaskMethod.assembleIssueUpdate(data.getId(), null, null, 0, null, null, 2, 2, 0, 0, null, null, 0);
                    }

                    if (issueUpdate != null){
                        ComponentUtil.taskIssueService.updateStatus(issueUpdate);
                    }

                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskIssue.issueDistribution()----end");
            }catch (Exception e){
                log.error(String.format("this TaskIssue.issueDistribution() is error , the dataId=%s !", data));
                e.printStackTrace();
            }
        }
    }




    /**
     * @Description: 检测下发归属平台订单的归集是否完成
     * <p>
     *     每5分钟执行运行一次
     *     1.查询订单状态是成功的，并且订单归属平台，订单属于审核完毕、归集状态属于未归集完毕的数据。
     *     2.根据查询的数据去比较卡商充值表中已经打款成功且审核状态是成功的订单金额是否与下发的订单金额相等，如果相等则归集完毕
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "5 * * * * ?")
//    @Scheduled(fixedDelay = 1000) // 每1秒执行
    @Scheduled(fixedDelay = 300000) // 每5分钟执行
    public void issueComplete() throws Exception{
//        log.info("----------------------------------TaskIssue.issueComplete()----start");

        // 获取未归集完毕的数据
        IssueModel issueQuery = TaskMethod.assembleIssueQuery(limitNum,0,0, 3, 2, 2, 1,3,0);
        List<IssueModel> synchroList = ComponentUtil.taskIssueService.getDataList(issueQuery);
        for (IssueModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_ISSUE_COMPLETE, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    IssueModel issueUpdate = null;

                    // 查询卡商充值表数据
                    MerchantRechargeModel merchantRechargeQuery = TaskMethod.assembleMerchantRechargeQuery(0,0,null,2, data.getOrderNo(),
                            3,0,0,3, null, null, null);
                    String money = ComponentUtil.merchantRechargeService.sumMoneyByOrder(merchantRechargeQuery);
                    if (!StringUtils.isBlank(money)){
                        String resMoney = StringUtil.getBigDecimalSubtractByStr(data.getOrderMoney(), money);
                        if (resMoney.equals("0")){
                            // 说明钱已归集完毕
                            issueUpdate = TaskMethod.assembleIssueUpdate(data.getId(), null, null, 0, null, null, 0, 0, 2, 0, null, null, 0);
                        }
                    }


                    if (issueUpdate != null){
                        ComponentUtil.taskIssueService.updateStatus(issueUpdate);
                    }

                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskIssue.issueComplete()----end");
            }catch (Exception e){
                log.error(String.format("this TaskIssue.issueComplete() is error , the dataId=%s !", data));
                e.printStackTrace();
            }
        }
    }




    /**
     * @Description: 检测已经完成的订单进行数据同步给下游
     * <p>
     *     每5秒钟执行运行一次
     *     1.查询订单状态是成功的.
     *     2.把数据进行同步下发。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "5 * * * * ?")
    @Scheduled(fixedDelay = 5000) // 每5秒执行
    public void issueSynchro() throws Exception{
//        log.info("----------------------------------TaskIssue.issueSynchro()----start");

        // 获取未同步下发给下游的数据
        IssueModel issueQuery = TaskMethod.assembleIssueQuery(limitNum,0,1, 3, 0, 0, 0,0,0);
        List<IssueModel> synchroList = ComponentUtil.taskIssueService.getDataList(issueQuery);
        for (IssueModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_ISSUE_SEND, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    IssueModel issueUpdate = null;


                    Map<String, Object> sendMap = new HashMap<>();
                    sendMap.put("total_amount", data.getOutTradeNo());
                    sendMap.put("pay_amount", data.getOrderMoney());
                    sendMap.put("out_trade_no", data.getOutTradeNo());
                    sendMap.put("trade_status", 1);
                    sendMap.put("trade_no", data.getOrderNo());
                    sendMap.put("trade_time", data.getCreateTime());
                    sendMap.put("picture_ads", data.getPictureAds());

                    String sendUrl = ComponentUtil.loadConstant.issueNotifyUrl;

//                    sendUrl = "http://localhost:8085/pay/data/fine";
//                    String resp = HttpSendUtils.sendGet(sendUrl + "?" + URLEncoder.encode(sendData,"UTF-8"), null, null);
//                    String resp = HttpSendUtils.sendGet(sendUrl + "?" + sendData, null, null);
                    String resp = HttpSendUtils.sendPostAppJson(sendUrl , JSON.toJSONString(sendMap));
                    if (resp.indexOf("ok") > -1){
                        // 成功
                        issueUpdate = TaskMethod.assembleIssueUpdateStatus(data.getId(), 0, 3, 0);
                    }else {
                        issueUpdate = TaskMethod.assembleIssueUpdateStatus(data.getId(), 0, 2, 0);
                    }
//                    issueUpdate = TaskMethod.assembleIssueUpdateStatus(data.getId(), 0, 3, 0);



                    if (issueUpdate != null){
                        ComponentUtil.taskIssueService.updateStatus(issueUpdate);
                    }

                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskIssue.issueSynchro()----end");
            }catch (Exception e){
                log.error(String.format("this TaskIssue.issueSynchro() is error , the dataId=%s !", data));
                e.printStackTrace();
                IssueModel issueUpdate = TaskMethod.assembleIssueUpdateStatus(data.getId(), 0, 2, 0);
                ComponentUtil.taskIssueService.updateStatus(issueUpdate);
            }
        }
    }

}
