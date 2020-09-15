package com.fruit.task.master.core.runner.task;

import com.fruit.task.master.core.common.utils.constant.CachedKeyUtils;
import com.fruit.task.master.core.common.utils.constant.TkCacheKey;
import com.fruit.task.master.core.model.order.OrderModel;
import com.fruit.task.master.core.model.order.OrderReplenishModel;
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
 * @Description task:订单补单
 * @Author yoko
 * @Date 2020/9/15 17:37
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskOrderReplenish {

    private final static Logger log = LoggerFactory.getLogger(TaskOrderReplenish.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: 处理订单补单
     * <p>
     *     每秒运行一次
     *     1.查询未跑的订单补单信息
     *     2.根据补单中的订单号查询派单信息，然后更新派单信息的订单状态
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 3000) // 每3秒执行
    public void handleReplenish() throws Exception{
//        log.info("----------------------------------TaskOrderReplenish.handleReplenish()----start");
        // 获取订单补单数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 0, 0, 0,0,3,null);
        List<OrderReplenishModel> synchroList = ComponentUtil.taskOrderReplenishService.getDataList(statusQuery);
        for (OrderReplenishModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_ORDER_REPLENISH, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;

                    // 获取派单信息
                    OrderModel orderQuery = TaskMethod.assembleOrderQuery(0,0, data.getOrderNo(),0,null,0,null,0,null,null);
                    OrderModel orderModel = (OrderModel)ComponentUtil.orderService.findByObject(orderQuery);
                    if (orderModel == null || orderModel.getId() == null || orderModel.getId() <= 0){
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"根据订单号未查到派单信息");
                        // 更新状态
                        ComponentUtil.taskOrderReplenishService.updateStatus(statusModel);
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey);
                        continue;
                    }

                    if (orderModel != null && orderModel.getId() != null && orderModel.getId() > 0){
                        if (orderModel.getOrderStatus() == 4){
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"派单状态已经是成功状态");
                            // 更新状态
                            ComponentUtil.taskOrderReplenishService.updateStatus(statusModel);
                            // 解锁
                            ComponentUtil.redisIdService.delLock(lockKey);
                            continue;
                        }

                        // 正式更新派单状态
                        OrderModel orderUpdate = TaskMethod.assembleOrderUpdateStatusAndReplenish(orderModel.getId(), 4, 2);
                        int num = ComponentUtil.orderService.update(orderUpdate);
                        if (num > 0){
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);
                        }else{
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"更新订单状态响应行为0");
                        }

                    }

                    // 更新状态
                    ComponentUtil.taskOrderReplenishService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }
//                log.info("----------------------------------TaskOrderReplenish.handleReplenish()----end");
            }catch (Exception e){
                log.error(String.format("this TaskOrderReplenish.handleReplenish() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,0, 0,0,"异常失败try!");
                ComponentUtil.taskOrderReplenishService.updateStatus(statusModel);
            }
        }
    }
}
