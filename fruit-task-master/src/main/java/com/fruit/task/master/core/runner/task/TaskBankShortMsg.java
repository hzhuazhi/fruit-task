package com.fruit.task.master.core.runner.task;

import com.fruit.task.master.core.common.utils.constant.CachedKeyUtils;
import com.fruit.task.master.core.common.utils.constant.ServerConstant;
import com.fruit.task.master.core.common.utils.constant.TkCacheKey;
import com.fruit.task.master.core.model.bank.BankModel;
import com.fruit.task.master.core.model.bank.BankShortMsgModel;
import com.fruit.task.master.core.model.bank.BankShortMsgStrategyModel;
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
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 1, 0, 0, 0);
        List<BankShortMsgModel> synchroList = ComponentUtil.taskBankShortMsgService.getDataList(statusQuery);
        for (BankShortMsgModel data : synchroList){
            StatusModel statusModel = null;
            // 获取短信端口的解析方式@后续可以使用缓存
            BankShortMsgStrategyModel bankShortMsgStrategyQuery = TaskMethod.assembleBankShortMsgStrategyQuery(0,0, data.getSmsNum());
            List<BankShortMsgStrategyModel> bankShortMsgStrategyList = ComponentUtil.bankShortMsgStrategyService.findByCondition(bankShortMsgStrategyQuery);
            if (bankShortMsgStrategyList == null || bankShortMsgStrategyList.size() <= 0){
                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 2, 0, "根据短信来源端口获取银行收款短信解析策略数据为空!");
                // 更新状态
                ComponentUtil.taskBankShortMsgService.updateStatus(statusModel);
                continue;
            }

            // 获取端口号的银行卡数据
            BankModel bankQuery = TaskMethod.assembleBankQuery(0, data.getMobileCardId(), 0, 0, null, data.getSmsNum(), null);
            List<BankModel> bankList = ComponentUtil.bankService.findByCondition(bankQuery);
            if (bankList == null || bankList.size() <= 0){
                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 2, 0, "根据手机卡ID、短信来源端口获取银行集合数据为空!");
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
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 2, 0, "拆解金额失败：1.银行收款短信解析策略可能不完善。2.短信可能不是银行收款短信!");
                        // 更新状态
                        ComponentUtil.taskBankShortMsgService.updateStatus(statusModel);
                        continue;
                    }

                    // 解析短信，定位银行卡ID
                    BankModel bank_matching = TaskMethod.getBankIdBySmsContent(bankList, data.getSmsContent(), lastNumKey);
                    if (bank_matching == null || bank_matching.getId() == null || bank_matching.getId() <= 0){
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 2, 0, "匹配银行卡尾号失败：没有匹配到相对应的银行卡尾号!");
                        // 更新状态
                        ComponentUtil.taskBankShortMsgService.updateStatus(statusModel);
                        continue;
                    }

                    // 更新银行卡短信信息的扩充数据
                    BankShortMsgModel bankShortMsgModelUpdate = TaskMethod.assembleBankShortMsgUpdate(data.getId(), null, bank_matching.getId(), bank_matching.getBankTypeId(), money, bank_matching.getLastNum());
                    int num = ComponentUtil.bankShortMsgService.update(bankShortMsgModelUpdate);
                    if (num > 0){
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 3, 0, null);
                    }else {
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 1, 0, "更新银行短信数据扩充响应行为0!");
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
                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0,2, "异常失败try!");
                ComponentUtil.taskBankShortMsgService.updateStatus(statusModel);
            }
        }

    }
}
