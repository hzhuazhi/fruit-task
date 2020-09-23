package com.fruit.task.master.core.runner.task;

import com.fruit.task.master.core.common.utils.DateUtil;
import com.fruit.task.master.core.common.utils.constant.CachedKeyUtils;
import com.fruit.task.master.core.common.utils.constant.ServerConstant;
import com.fruit.task.master.core.common.utils.constant.TkCacheKey;
import com.fruit.task.master.core.model.bank.BankModel;
import com.fruit.task.master.core.model.issue.IssueModel;
import com.fruit.task.master.core.model.merchant.MerchantModel;
import com.fruit.task.master.core.model.merchant.MerchantRechargeModel;
import com.fruit.task.master.core.model.order.OrderModel;
import com.fruit.task.master.core.model.strategy.StrategyModel;
import com.fruit.task.master.util.ComponentUtil;
import com.fruit.task.master.util.TaskMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
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
     *     每30秒执行运行一次
     *     1.查询未分配的下发数据。
     *     2.查询哪些卡商符合分配的条件
     *     3.如果轮询卡商不能分配则分配给平台
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "5 * * * * ?")
//    @Scheduled(fixedDelay = 1000) // 每1分钟执行
    @Scheduled(fixedDelay = 30000) // 每30秒执行
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
                    int ascriptionType = 0;// 订单分配归属类型：1归属卡商，2归属平台
                    List<Long> accountIdList = null;
                    // 查询是否有已经分配的纪录
                    MerchantRechargeModel merchantRechargeQuery = TaskMethod.assembleMerchantRechargeQuery(0,0,null, 3, data.getOrderNo(),
                            0, 3, 0,0,null,null, "1");
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
                    }else if (ascriptionType == 2){
                        // 分配给平台
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

}
