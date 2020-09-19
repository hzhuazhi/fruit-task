package com.fruit.task.master.core.runner.task;

import com.fruit.task.master.core.common.utils.DateUtil;
import com.fruit.task.master.core.common.utils.constant.CachedKeyUtils;
import com.fruit.task.master.core.common.utils.constant.ServerConstant;
import com.fruit.task.master.core.common.utils.constant.TkCacheKey;
import com.fruit.task.master.core.model.bank.BankModel;
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

/**
 * @Description task:监控
 * @Author yoko
 * @Date 2020/9/15 15:50
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskMonitor {
    private final static Logger log = LoggerFactory.getLogger(TaskMonitor.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: 检测派发的订单中的银行卡收款是否异常
     * <p>
     *     每30秒执行运行一次
     *     1.查询今日所有给出去重复后的银行卡ID集合
     *     2.for循环查询每个银行卡给出的次数
     *     3.根据策略部署数据，进行次数比较，如果给码次数已经超过部署的策略的给码次数，则修改银行卡的状态
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "5 * * * * ?")
//    @Scheduled(fixedDelay = 1000) // 每1分钟执行
    @Scheduled(fixedDelay = 30000) // 每30秒执行
    public void monitorBank() throws Exception{
//        log.info("----------------------------------TaskMonitor.monitorBank()----start");
        int curday = DateUtil.getDayNumber(new Date());
        // 策略：检测银行卡连续给出失败次数
        StrategyModel strategyQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.MONITOR_BANK_NUM.getStgType());
        StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        String [] strategyDataArr = strategyModel.getStgValue().split("#");

        // 获取未填充可爱猫回调店员绑定小微的数据
        OrderModel orderByBankQuery = TaskMethod.assembleOrderByMonitorQuery(0,0,null, curday,0);
        List<Long> synchroList = ComponentUtil.taskMonitorService.getBankIdListByOrder(orderByBankQuery);
        for (Long data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_MONITOR_BANK, data);
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    for (String str : strategyDataArr){
                        String [] strArr = str.split("-");
                        int payType = Integer.parseInt(strArr[0]);// 支付类型
                        int failNum = Integer.parseInt(strArr[1]);// 可连续失败的次数

                        // 获取订单信息
                        OrderModel orderQuery = TaskMethod.assembleOrderByMonitorQuery(data, payType,"2", curday, failNum);
                        List<OrderModel> orderList = ComponentUtil.taskMonitorService.getOrderList(orderQuery);
                        int checkNum = 0;
                        if (orderList != null && orderList.size() > 0){
                            if (orderList.size() == failNum){
                                for (OrderModel dataModel : orderList){
                                    if (dataModel.getOrderStatus() == 4){
                                        break;
                                    }else{
                                        checkNum ++;
                                    }
                                }
                            }
                        }

                        if (checkNum != 0 && checkNum >= failNum) {
                            // 更新银行卡的状态信息
                            String dataExplain = "检测：" + DateUtil.getNowPlusTime() + ",支付类型：" + payType + ",连续给出：" + failNum + "次，没有一次成功!";
                            BankModel bankUpdate = TaskMethod.assembleBankUpdate(data, 2, 0, dataExplain);
                            ComponentUtil.bankService.update(bankUpdate);
                            break;
                        }


                    }
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskMonitor.monitorBank()----end");
            }catch (Exception e){
                log.error(String.format("this TaskMonitor.monitorBank() is error , the dataId=%s !", data));
                e.printStackTrace();
            }
        }
    }

}
