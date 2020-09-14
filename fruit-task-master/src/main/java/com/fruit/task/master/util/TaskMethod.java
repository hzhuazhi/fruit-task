package com.fruit.task.master.util;
import com.fruit.task.master.core.common.utils.constant.ServerConstant;
import com.fruit.task.master.core.model.mobilecard.MobileCardShortMsgModel;
import com.fruit.task.master.core.model.shortmsg.ShortMsgStrategyModel;
import com.fruit.task.master.core.model.task.base.StatusModel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * @Description 定时任务的公共类
 * @Author yoko
 * @Date 2020/1/11 16:20
 * @Version 1.0
 */
public class TaskMethod {
    private static Logger log = LoggerFactory.getLogger(TaskMethod.class);



    /**
     * @Description: 组装查询定时任务的查询条件
     * @param limitNum - 多少条数据
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
     */
    public static StatusModel assembleTaskStatusQuery(int limitNum, int runType, int workType, int dataType){
        StatusModel resBean = new StatusModel();
        if (runType > 0){
            resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
            resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        }
        if (workType > 0){
            resBean.setWorkType(workType);
        }
        if (dataType > 0){
            resBean.setDataType(dataType);
        }
        resBean.setLimitNum(limitNum);
        return resBean;
    }

    /**
     * @Description: 组装更改运行状态的数据
     * @param id - 主键ID
     * @param runStatus - 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功
     * @param workType - 补充数据的类型：1初始化，2补充数据失败，3补充数据成功
     * @param dataType - 数据类型
     * @param info - 解析说明
     * @return StatusModel
     * @author yoko
     * @date 2019/12/10 10:42
     */
    public static StatusModel assembleTaskUpdateStatus(long id, int runStatus, int workType, int dataType, String info){
        StatusModel resBean = new StatusModel();
        resBean.setId(id);
        if (runStatus > 0){
            resBean.setRunStatus(runStatus);
            if (runStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
                // 表示失败：失败则需要运行次数加一
                resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            }
        }
        if (workType > 0){
            resBean.setWorkType(workType);
        }
        if (dataType > 0){
            resBean.setDataType(dataType);
        }
        if (!StringUtils.isBlank(info)){
            resBean.setInfo(info);
        }
        return resBean;
    }


    /**
     * @Description: 根据短信解析类型查询
     * @param shortMsgType
     * @return
     * @author yoko
     * @date 2020/9/13 22:07
    */
    public static ShortMsgStrategyModel assembleShortMsgStrategyByTypeQuery(int shortMsgType){
        ShortMsgStrategyModel resBean = new ShortMsgStrategyModel();
        resBean.setShortMsgTypeStr(shortMsgType);
        return resBean;
    }



    /**
     * @Description: 归类短信归属类型
     * <p>
     *     1其它短信，2欠费短信，3银行短信
     * </p>
     * @param mobileCardShortMsgModel - 短信信息
     * @param shortMsgStrategyList - 策略：检查手机短信类型的规则
     * @return
     * @author yoko
     * @date 2020/6/3 16:23
     */
    public static int screenMobileCardShortMsgType(MobileCardShortMsgModel mobileCardShortMsgModel, List<ShortMsgStrategyModel> shortMsgStrategyList){
        int type = 0;// 筛选之后的最终类型-短信内容的类型：1其它短信，2欠费短信，3银行短信
        for (ShortMsgStrategyModel shortMsgStrategyModel : shortMsgStrategyList){
            int keyType = shortMsgStrategyModel.getShortMsgType();// 策略：短信定义的类型
            String[] keyArray = shortMsgStrategyModel.getKeyValue().split("#");// 策略：短信分类的关键字
            int keyNum = shortMsgStrategyModel.getKeyNum();// 短信分类需要满足几个关键字的符合
            int countKeyNum = 0;// 计算已经满足了几个关键字的符合
            // 循环关键字匹配
            if (keyType == 1){
                // 只需要匹配一个关键字：欠费短信
                countKeyNum  = countAccordWithKey(mobileCardShortMsgModel.getSmsContent(), keyArray);// 具体筛选
                if (countKeyNum >= keyNum){
                    type = 2;// 欠费短息
                    break;
                }
            }else if(keyType == 2){
                // 银行短信
                // 需要匹配：银行短信
                countKeyNum  = countAccordWithKey(mobileCardShortMsgModel.getSmsContent(), keyArray);// 具体筛选
                if (countKeyNum >= keyNum){
                    type = 3;// 属于银行短信
                    break;
                }
            }
        }
        if (type <= 0){
            // 其它短信
            type = 1;
        }
        return type;
    }


    /**
     * @Description: 计算满足了几个关键字
     * @param content - 短信类容
     * @param keyArray - 关键字
     * @return
     * @author yoko
     * @date 2020/6/3 15:38
     */
    public static int countAccordWithKey(String content, String[] keyArray){
        int count = 0;// 计算已经满足了几个关键字的符合
        for (String str : keyArray){
            if (content.indexOf(str) > -1){
                count ++;
            }
        }
        return count;
    }

}
