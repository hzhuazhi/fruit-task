package com.fruit.task.master.core.runner.task;

import com.alibaba.fastjson.JSON;
import com.fruit.task.master.core.common.utils.DateUtil;
import com.fruit.task.master.core.common.utils.HttpSendUtils;
import com.fruit.task.master.core.common.utils.constant.CacheKey;
import com.fruit.task.master.core.common.utils.constant.CachedKeyUtils;
import com.fruit.task.master.core.common.utils.constant.TkCacheKey;
import com.fruit.task.master.core.model.bank.BankCollectionModel;
import com.fruit.task.master.core.model.bank.BankStrategyModel;
import com.fruit.task.master.core.model.merchant.MerchantModel;
import com.fruit.task.master.core.model.order.OrderModel;
import com.fruit.task.master.core.model.task.base.StatusModel;
import com.fruit.task.master.util.ComponentUtil;
import com.fruit.task.master.util.TaskMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        int curday = DateUtil.getDayNumber(new Date());// 当天
        int curdayStart = DateUtil.getMinMonthDate();// 月初
        int curdayEnd = DateUtil.getMaxMonthDate();// 月末
        // 获取已成功订单数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 0, 0, 0,0,4,null);
        List<OrderModel> synchroList = ComponentUtil.taskOrderService.getDataList(statusQuery);
        for (OrderModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_ORDER_SUCCESS, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;

                    // 获取此银行卡的放量策略
                    BankStrategyModel bankStrategyQuery = TaskMethod.assembleBankStrategyQuery(0, data.getBankId(), 1);
                    BankStrategyModel bankStrategyModel = (BankStrategyModel)ComponentUtil.bankStrategyService.findByObject(bankStrategyQuery);
                    if (bankStrategyModel != null && bankStrategyModel.getId() != null && bankStrategyModel.getId() > 0){
                        // 计算银行卡的放量限制

                        // 获取日成功给出的次数
                        OrderModel orderByDayNumQuery = TaskMethod.assembleOrderByLimitQuery(data.getBankId(), data.getOrderType(), 4, curday, 0, 0);
                        int dayNum = ComponentUtil.orderService.countOrder(orderByDayNumQuery);

                        // 获取日成功给出的金额
                        OrderModel orderByDayMoneyQuery = TaskMethod.assembleOrderByLimitQuery(data.getBankId(), data.getOrderType(), 4, curday, 0, 0);
                        String dayMoney = ComponentUtil.orderService.sumOrderMoney(orderByDayMoneyQuery);

                        // 获取月成功给出的金额
                        OrderModel orderByMonthMoneyQuery = TaskMethod.assembleOrderByLimitQuery(data.getBankId(), data.getOrderType(), 4, 0, curdayStart, curdayEnd);
                        String monthMoney = ComponentUtil.orderService.sumOrderMoney(orderByMonthMoneyQuery);

                        // check银行卡的放量限制：如果超过策略的放量，则会存redis缓存
                        ComponentUtil.bankStrategyService.bankStrategyLimit(bankStrategyModel, data.getOrderType(), dayNum, dayMoney, monthMoney);
                    }


                    // 组装银行收款信息
                    BankCollectionModel bankCollectionAdd = TaskMethod.assembleBankCollectionAdd(data.getBankId(), data.getOrderNo(), data.getOrderMoney());

                    // 组装卡商金额更新
                    MerchantModel merchantUpdate = TaskMethod.assembleMerchantUpdateMoney(data.getAccountId(), data.getOrderMoney());
                    // 锁住这个卡商
                    String lockKey_accountId = CachedKeyUtils.getCacheKey(CacheKey.LOCK_MERCHANT_MONEY, data.getAccountId());
                    boolean flagLock_accountId = ComponentUtil.redisIdService.lock(lockKey_accountId);
                    if (flagLock_accountId){

                        // 执行订单成功的逻辑
                        boolean flag_handle = ComponentUtil.taskOrderService.handleSuccessOrder(bankCollectionAdd, merchantUpdate);
                        if (flag_handle){

                            // 判断是否是补单，不是补单则需要释放银行卡的挂单金额
                            if (data.getReplenishType() == 1){
                                // 删除redis：删除银行卡此金额的挂单
                                String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.BANK_ORDER_MONEY, data.getBankId(), data.getOrderMoney());
                                ComponentUtil.redisService.remove(strKeyCache);
                            }

                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);
                        }else {
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"事务处理出错!");
                        }
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey_accountId);
                    }else{
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"卡商被其它任务锁住!");
                    }



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



    /**
     * @Description: task：执行派单成功订单的数据同步
     * <p>
     *     每1每秒运行一次
     *     1.查询出已处理的派单成功的订单数据数据。
     *     2.根据同步地址进行数据同步。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(fixedDelay = 1000) // 每秒执行
    public void orderNotify() throws Exception{
//        log.info("----------------------------------TaskOrder.orderNotify()----start");

        // 获取已成功的订单数据，并且为同步给下游的数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 0, 0, 0, 0,1,0,null);
        List<OrderModel> synchroList = ComponentUtil.taskOrderService.getOrderNotifyList(statusQuery);
        for (OrderModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_ORDER_SEND, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    // 进行数据同步

//                    String sendData = "total_amount=" + data.getOrderMoney() + "&" + "out_trade_no=" + data.getOutTradeNo() + "&" + "trade_status=" + 1
//                            + "&" + "trade_no=" + data.getOrderNo() + "&" + "trade_time=" + data.getCreateTime();
                    Map<String, Object> sendMap = new HashMap<>();
                    sendMap.put("total_amount", data.getOrderMoney());
                    sendMap.put("pay_amount", data.getOrderMoney());
                    sendMap.put("out_trade_no", data.getOutTradeNo());
                    sendMap.put("trade_status", 1);
                    sendMap.put("trade_no", data.getOrderNo());
                    sendMap.put("trade_time", data.getCreateTime());

                    String sendUrl = "";
                    if (!StringUtils.isBlank(data.getNotifyUrl())){
                        sendUrl = data.getNotifyUrl();
                    }else {
                        sendUrl = ComponentUtil.loadConstant.defaultNotifyUrl;
                    }
//                    sendUrl = "http://localhost:8085/pay/data/fine";
//                    String resp = HttpSendUtils.sendGet(sendUrl + "?" + URLEncoder.encode(sendData,"UTF-8"), null, null);
//                    String resp = HttpSendUtils.sendGet(sendUrl + "?" + sendData, null, null);
                    String resp = HttpSendUtils.sendPostAppJson(sendUrl , JSON.toJSONString(sendMap));
                    if (resp.indexOf("ok") > -1){
                        // 成功
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 3,0,null);
                    }else {
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,null);
                    }


                    // 更新状态
                    ComponentUtil.taskOrderService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskOrder.orderNotify()----end");
            }catch (Exception e){
                log.error(String.format("this TaskOrder.orderNotify() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为ERROR
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,null);
                ComponentUtil.taskOrderService.updateStatus(statusModel);
            }
        }
    }

}
