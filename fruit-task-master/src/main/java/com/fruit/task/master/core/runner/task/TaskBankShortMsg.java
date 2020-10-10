package com.fruit.task.master.core.runner.task;

import com.fruit.task.master.core.common.utils.DateUtil;
import com.fruit.task.master.core.common.utils.constant.CachedKeyUtils;
import com.fruit.task.master.core.common.utils.constant.ServerConstant;
import com.fruit.task.master.core.common.utils.constant.TkCacheKey;
import com.fruit.task.master.core.model.bank.BankModel;
import com.fruit.task.master.core.model.bank.BankShortMsgModel;
import com.fruit.task.master.core.model.bank.BankShortMsgStrategyModel;
import com.fruit.task.master.core.model.order.OrderModel;
import com.fruit.task.master.core.model.strategy.StrategyModel;
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

import java.util.List;

/**
 * @Description task:银行卡短信数据
 * @Author yoko
 * @Date 2020/9/14 16:07
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskBankShortMsg {


    private final static Logger log = LoggerFactory.getLogger(TaskBankShortMsg.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;

    /**
     * @Description: 补充解析银行短信信息
     * <p>
     *     每秒运行一次
     *     1.把短信数据进行解析补充全
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 1000) // 每1秒执行
    public void analysisShortMsg() throws Exception{
//        log.info("----------------------------------TaskBankShortMsg.analysisShortMsg()----start");
        // 策略：银行卡尾号起始关键字
        String lastNumKey = null;
        StrategyModel strategyQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.LAST_NUM_KEY.getStgType());
        StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        lastNumKey = strategyModel.getStgValue();

        // 获取银行短信数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 1, 0, 0, 0, 0, 0,null);
        List<BankShortMsgModel> synchroList = ComponentUtil.taskBankShortMsgService.getDataList(statusQuery);
        for (BankShortMsgModel data : synchroList){
            StatusModel statusModel = null;
            // 获取短信端口的解析方式@后续可以使用缓存
            BankShortMsgStrategyModel bankShortMsgStrategyQuery = TaskMethod.assembleBankShortMsgStrategyQuery(0,0, data.getSmsNum());
            List<BankShortMsgStrategyModel> bankShortMsgStrategyList = ComponentUtil.bankShortMsgStrategyService.findByCondition(bankShortMsgStrategyQuery);
            if (bankShortMsgStrategyList == null || bankShortMsgStrategyList.size() <= 0){
                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 2, 0, 0,0,"根据短信来源端口获取银行收款短信解析策略数据为空!" );
                // 更新状态
                ComponentUtil.taskBankShortMsgService.updateStatus(statusModel);
                continue;
            }

            // 获取端口号的银行卡数据
            BankModel bankQuery = TaskMethod.assembleBankQuery(0, data.getMobileCardId(), 0, 0, null, data.getSmsNum(), null);
            List<BankModel> bankList = ComponentUtil.bankService.findByCondition(bankQuery);
            if (bankList == null || bankList.size() <= 0){
                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 2, 0, 0,0,"根据手机卡ID、短信来源端口获取银行集合数据为空!");
                // 更新状态
                ComponentUtil.taskBankShortMsgService.updateStatus(statusModel);
                continue;
            }
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_BANK_SHORT_MSG, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    // 解析短信，获取收款金额
                    String money = TaskMethod.getBankMoney(bankShortMsgStrategyList, data.getSmsContent());
                    if (StringUtils.isBlank(money)){
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 2, 0, 0,0,"拆解金额失败：1.银行收款短信解析策略可能不完善。2.短信可能不是银行收款短信!");
                        // 更新状态
                        ComponentUtil.taskBankShortMsgService.updateStatus(statusModel);
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey);
                        continue;
                    }

                    // 解析短信，定位银行卡ID
                    BankModel bank_matching = TaskMethod.getBankIdBySmsContent(bankList, data.getSmsContent(), lastNumKey);
                    if (bank_matching == null || bank_matching.getId() == null || bank_matching.getId() <= 0){
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 2, 0, 0,0,"匹配银行卡尾号失败：没有匹配到相对应的银行卡尾号!");
                        // 更新状态
                        ComponentUtil.taskBankShortMsgService.updateStatus(statusModel);
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey);
                        continue;
                    }

                    // 更新银行卡短信信息的扩充数据
                    BankShortMsgModel bankShortMsgModelUpdate = TaskMethod.assembleBankShortMsgUpdate(data.getId(), null, bank_matching.getId(), bank_matching.getBankTypeId(), money, bank_matching.getLastNum());
                    int num = ComponentUtil.bankShortMsgService.update(bankShortMsgModelUpdate);
                    if (num > 0){
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 3, 0, 0,0,null);
                    }else {
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 1, 0, 0,0,"更新银行短信数据扩充响应行为0!");
                    }
                    // 更新状态
                    ComponentUtil.taskBankShortMsgService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }


//                log.info("----------------------------------TaskBankShortMsg.analysisShortMsg()----end");
            }catch (Exception e){
                log.error(String.format("this TaskBankShortMsg.analysisShortMsg() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0,2, 0,0,"异常失败try!");
                ComponentUtil.taskBankShortMsgService.updateStatus(statusModel);
            }
        }
    }



    /**
     * @Description: 匹配订单
     * <p>
     *     每秒运行一次
     *     1.根据银行卡，金额时间去匹配订单
     *     2.如果匹配上了，则修改订单状态
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 1000) // 每1秒执行
    public void handle() throws Exception{
//        log.info("----------------------------------TaskBankShortMsg.handle()----start");
        // 策略数据：订单的支付时间
        int invalidTimeNum = 0;
        StrategyModel strategyInvalidTimeNumQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.INVALID_TIME_NUM.getStgType());
        StrategyModel strategyInvalidTimeNumModel = ComponentUtil.strategyService.getStrategyModel(strategyInvalidTimeNumQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        invalidTimeNum = strategyInvalidTimeNumModel.getStgNumValue();

        // 获取银行短信数据-已经扩充完毕
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 3, 0, 0, 0,0,0,null);
        List<BankShortMsgModel> synchroList = ComponentUtil.taskBankShortMsgService.getDataList(statusQuery);
        for (BankShortMsgModel data : synchroList){
            StatusModel statusModel = null;
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_BANK_SHORT_MSG_WORK_TYPE, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    String startTime = DateUtil.addAndSubtractDateMinute(data.getCreateTime(), -invalidTimeNum);// 数据的创建时间减订单超时时间=特定时间的前几分中的时间
                    String endTime = data.getCreateTime();
                     // 查询订单
                    OrderModel orderQuery = TaskMethod.assembleOrderQuery(0, data.getBankId(), null,0,null, data.getMoney(), 1, null, 1, startTime, endTime);
                    List<OrderModel> orderList = ComponentUtil.orderService.findByCondition(orderQuery);
                    if (orderList == null || orderList.size() <= 0){
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"匹配订单失败：根据银行卡ID、金额、订单状态、创建时间未匹配到订单!");
                        // 更新状态
                        ComponentUtil.taskBankShortMsgService.updateStatus(statusModel);
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey);
                        continue;
                    }
                    if (orderList != null && orderList.size() > 1){
                        // 匹配到多个订单
                        // 把订单集合的订单号汇聚成一个字符串
                        String orderNoStr = TaskMethod.getOrderNoStr(orderList);
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"匹配订单失败：根据银行卡ID、金额、订单状态、创建时间匹配到多个订单! 订单号:" + orderNoStr);
                        // 更新状态
                        ComponentUtil.taskBankShortMsgService.updateStatus(statusModel);
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey);
                        continue;
                    }

                    // 只匹配到一个订单号
                    // 更新银行卡短信信息的扩充数据
                    BankShortMsgModel bankShortMsgModelUpdate = TaskMethod.assembleBankShortMsgUpdate(data.getId(), orderList.get(0).getOrderNo(), 0, 0, null, null);
                    ComponentUtil.bankShortMsgService.update(bankShortMsgModelUpdate);

                    // 更新订单号的状态
                    OrderModel orderUpdate = TaskMethod.assembleOrderUpdateStatus(orderList.get(0).getId(), 4);
                    int num = ComponentUtil.orderService.update(orderUpdate);
                    if (num > 0){
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);
                    }else {
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"更新订单号状态响应行为0!");
                    }
                    // 更新状态
                    ComponentUtil.taskBankShortMsgService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }


//                log.info("----------------------------------TaskBankShortMsg.handle()----end");
            }catch (Exception e){
                log.error(String.format("this TaskBankShortMsg.handle() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0,2, 0,0,"异常失败try!");
                ComponentUtil.taskBankShortMsgService.updateStatus(statusModel);
            }
        }
    }


}
