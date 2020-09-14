package com.fruit.task.master.core.runner.task;

import com.fruit.task.master.core.common.utils.constant.CachedKeyUtils;
import com.fruit.task.master.core.common.utils.constant.ServerConstant;
import com.fruit.task.master.core.common.utils.constant.TkCacheKey;
import com.fruit.task.master.core.model.mobilecard.MobileCardModel;
import com.fruit.task.master.core.model.mobilecard.MobileCardShortMsgModel;
import com.fruit.task.master.core.model.shortmsg.ShortMsgStrategyModel;
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
        // 获取短信的类型定位策略
        ShortMsgStrategyModel shortMsgStrategyModel = TaskMethod.assembleShortMsgStrategyByTypeQuery(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
        List<ShortMsgStrategyModel> shortMsgStrategyList = ComponentUtil.shortMsgStrategyService.getShortMsgStrategyList(shortMsgStrategyModel, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);


        // 获取手机短信数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 0, 1, 0, 0);
        List<MobileCardShortMsgModel> synchroList = ComponentUtil.taskMobileCardShortMsgService.getDataList(statusQuery);
        for (MobileCardShortMsgModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_MOBILE_CARD_SHORT_MSG, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    int type = 0;// 筛选之后的最终类型
                    type = TaskMethod.screenMobileCardShortMsgType(data, shortMsgStrategyList);
                    if (type == 1){
                        // 其它短信
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 2, null);
                    }else if (type == 2){
                        // 欠费短信
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 3, null);
                    }else if (type == 3){
                        // 银行短信
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 4, null);
                    }
                    if (!StringUtils.isBlank(data.getPhoneNum())){
                        MobileCardModel mobileCardQuery = TaskMethod.assembleMobileCardQuery(0, data.getPhoneNum(), 0,0,0);
                        MobileCardModel mobileCardModel = ComponentUtil.mobileCardService.getMobileCard(mobileCardQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
                        if (mobileCardModel != null && mobileCardModel.getId() != null && mobileCardModel.getId() > 0){
                            // 更新原始数据的手机ID
                            MobileCardShortMsgModel mobileCardShortMsgUpdate = TaskMethod.assembleMobileCardShortMsgUpdateMobileCardId(data.getId(), mobileCardModel.getId());
                            ComponentUtil.mobileCardShortMsgService.update(mobileCardShortMsgUpdate);
                        }
                    }
                    // 更新状态
                    ComponentUtil.taskMobileCardShortMsgService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }


//                log.info("----------------------------------TaskBankShortMsg.analysisShortMsg()----end");
            }catch (Exception e){
                log.error(String.format("this TaskBankShortMsg.analysisShortMsg() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0,2, "异常失败try!");
                ComponentUtil.taskMobileCardShortMsgService.updateStatus(statusModel);
            }
        }

    }
}
