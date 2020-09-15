package com.fruit.task.master.core.runner.task;

import com.fruit.task.master.core.common.utils.constant.CachedKeyUtils;
import com.fruit.task.master.core.common.utils.constant.TkCacheKey;
import com.fruit.task.master.core.model.bank.BankModel;
import com.fruit.task.master.core.model.mobilecard.MobileCardModel;
import com.fruit.task.master.core.model.order.OrderModel;
import com.fruit.task.master.core.model.order.OrderReplenishModel;
import com.fruit.task.master.core.model.shortmsg.ShortMsgArrearsModel;
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
 * @Description task:手机卡欠费短信的
 * @Author yoko
 * @Date 2020/9/15 19:04
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskShortMsgArrears {

    private final static Logger log = LoggerFactory.getLogger(TaskShortMsgArrears.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: 处理手机欠费短信
     * <p>
     *     每秒运行一次
     *     1.查询未跑的欠费短信信息
     *     2.根据欠费短信更新手机的状态
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 2000) // 每2秒执行
    public void handleArrears() throws Exception{
//        log.info("----------------------------------TaskShortMsgArrears.handleArrears()----start");
        // 获取手机欠费短信数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 0, 0, 0,0,3,null);
        List<ShortMsgArrearsModel> synchroList = ComponentUtil.taskShortMsgArrearsService.getDataList(statusQuery);
        for (ShortMsgArrearsModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_SHORT_MSG_ARREARS, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;

                    // 获取手机卡信息
                    MobileCardModel mobileCardModel = (MobileCardModel)ComponentUtil.mobileCardService.findById(data.getMobileCardId());
                    if (mobileCardModel == null || mobileCardModel.getId() == null || mobileCardModel.getId() <= 0){
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"根据手机主键ID未查询到手机卡信息");
                        // 更新状态
                        ComponentUtil.taskShortMsgArrearsService.updateStatus(statusModel);
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey);
                        continue;
                    }

                    if (mobileCardModel != null && mobileCardModel.getId() != null && mobileCardModel.getId() > 0){
                        if (mobileCardModel.getIsArrears() == 2){
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"手机卡已经是欠费状态");
                            // 更新状态
                            ComponentUtil.taskShortMsgArrearsService.updateStatus(statusModel);
                            // 解锁
                            ComponentUtil.redisIdService.delLock(lockKey);
                            continue;
                        }

                        // 正式更新手机卡状态
                        MobileCardModel mobileCardUpdate = TaskMethod.assembleMobileCardUpdateArrears(mobileCardModel.getId(), 2);
                        int num = ComponentUtil.mobileCardService.update(mobileCardUpdate);
                        if (num > 0){
                            // 查询涉及到的银行卡
                            BankModel bankQuery = TaskMethod.assembleBankQuery(0, mobileCardModel.getId(), 0, 0,null,null,null);
                            List<BankModel> bankList = ComponentUtil.bankService.findByCondition(bankQuery);
                            String involveBank = TaskMethod.assembleInvolveBank(bankList);

                            // 更新涉及到的银行卡
                            if (!StringUtils.isBlank(involveBank)){
                                ShortMsgArrearsModel shortMsgArrearsUpdate = TaskMethod.assembleShortMsgArrearsUpdateBank(data.getId(), involveBank);
                                ComponentUtil.shortMsgArrearsService.update(shortMsgArrearsUpdate);
                            }

                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);
                        }else{
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"更新手机欠费状态响应行为0");
                        }

                    }

                    // 更新状态
                    ComponentUtil.taskShortMsgArrearsService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }
//                log.info("----------------------------------TaskShortMsgArrears.handleArrears()----end");
            }catch (Exception e){
                log.error(String.format("this TaskShortMsgArrears.handleArrears() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,0, 0,0,"异常失败try!");
                ComponentUtil.taskShortMsgArrearsService.updateStatus(statusModel);
            }
        }
    }
}
