package com.fruit.task.master.core.runner.task;

import com.fruit.task.master.core.common.utils.constant.CachedKeyUtils;
import com.fruit.task.master.core.common.utils.constant.TkCacheKey;
import com.fruit.task.master.core.model.bank.BankStrategyModel;
import com.fruit.task.master.core.model.order.OrderModel;
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
 * @Description task:订单
 * @Author yoko
 * @Date 2020/9/14 21:56
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskOrder {

    private final static Logger log = LoggerFactory.getLogger(TaskOrder.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: 处理时效订单
     * <p>
     *     每秒运行一次
     *     1.查询订单属于初始化状态并且失效时间已经小于当前系统时间的订单
     *     2.更新订单状态：更新成失效状态
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 1000) // 每1秒执行
    public void handleInvalid() throws Exception{
//        log.info("----------------------------------TaskOrder.handleInvalid()----start");
        // 获取已失效订单数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 0, 0, 0, 0,0,1,"1");
        List<OrderModel> synchroList = ComponentUtil.taskOrderService.getDataList(statusQuery);
        for (OrderModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_ORDER_INVALID, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 4, 0,2,null);
                    // 更新状态
                    ComponentUtil.taskOrderService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }
//                log.info("----------------------------------TaskOrder.handleInvalid()----end");
            }catch (Exception e){
                log.error(String.format("this TaskOrder.handleInvalid() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0,0, 0,0,"异常失败try!");
                ComponentUtil.taskOrderService.updateStatus(statusModel);
            }
        }
    }


    /**
     * @Description: 处理成功订单
     * <p>
     *     每秒运行一次
     *     1.查询成功订单。
     *     2.释放redis缓存：银行卡的金额(补单类型的订单不进行redis释放)
     *     3.计算银行卡的限制
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 1000) // 每1秒执行
    public void success() throws Exception{
//        log.info("----------------------------------TaskOrder.success()----start");
        // 获取已失效订单数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 0, 0, 0,0,4,null);
        List<OrderModel> synchroList = ComponentUtil.taskOrderService.getDataList(statusQuery);
        for (OrderModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_ORDER_INVALID, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;

                    // 获取此银行卡的放量策略
                    BankStrategyModel bankStrategyQuery = TaskMethod.assembleBankStrategyQuery(0, data.getBankId(), 1);
                    BankStrategyModel bankStrategyModel = (BankStrategyModel)ComponentUtil.bankStrategyService.findByObject(bankStrategyQuery);
                    if (bankStrategyModel != null && bankStrategyModel.getId() != null && bankStrategyModel.getId() > 0){
                        // 计算银行卡的放量限制

                    }


                    statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 4, 0,0,null);
                    // 更新状态
                    ComponentUtil.taskOrderService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }
//                log.info("----------------------------------TaskOrder.success()----end");
            }catch (Exception e){
                log.error(String.format("this TaskOrder.success() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,0, 0,0,"异常失败try!");
                ComponentUtil.taskOrderService.updateStatus(statusModel);
            }
        }
    }

}
