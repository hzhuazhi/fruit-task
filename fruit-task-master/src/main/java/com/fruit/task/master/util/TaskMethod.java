package com.fruit.task.master.util;
import com.fruit.task.master.core.common.utils.constant.ServerConstant;
import com.fruit.task.master.core.model.task.base.StatusModel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    public static StatusModel assembleTaskStatusQuery(int limitNum){
        StatusModel resBean = new StatusModel();
        resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
        resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        resBean.setLimitNum(limitNum);
        return resBean;
    }


    /**
     * @Description: 组装更改运行状态的数据
     * @param id - 主键ID
     * @param runStatus - 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功
     * @return StatusModel
     * @author yoko
     * @date 2019/12/10 10:42
     */
    public static StatusModel assembleTaskUpdateStatusModel(long id, int runStatus){
        StatusModel resBean = new StatusModel();
        resBean.setId(id);
        resBean.setRunStatus(runStatus);
        if (runStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
            // 表示失败：失败则需要运行次数加一
            resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
        }
        return resBean;
    }



    /**
     * @Description: 组装更改运行状态的数据
     * @param id - 主键ID
     * @param runStatus - 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功
     * @param info - 纪录失败的原因
     * @return StatusModel
     * @author yoko
     * @date 2019/12/10 10:42
     */
    public static StatusModel assembleUpdateStatusByInfo(long id, int runStatus, String info){
        StatusModel resBean = new StatusModel();
        resBean.setRunStatus(runStatus);
        resBean.setId(id);
        if (runStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
            // 表示失败：失败则需要运行次数加一
            resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
        }
        if (!StringUtils.isBlank(info)){
            resBean.setInfo(info);
        }
        return resBean;
    }


    /**
     * @Description: 组装查询定时-填充的查询条件
     * @param limitNum - 多少条数据
     * @param workType - 补充数据的类型：1初始化，2补充失败，3补充数据成功
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
     */
    public static StatusModel assembleTaskByWorkTypeQuery(int limitNum, int workType){
        StatusModel resBean = new StatusModel();
        resBean.setWorkType(workType);
        resBean.setLimitNum(limitNum);
        return resBean;
    }

    /**
     * @Description: 组装查询定时任务数据-填充的-未runStatus过的查询条件
     * @param limitNum - 多少条数据
     * @param workType - 补充数据的类型：1初始化，2补充数据失败（未匹配到银行卡的数据），3补充数据成功
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
     */
    public static StatusModel assembleTaskByWorkTypeAndRunStatusQuery(int limitNum, int workType){
        StatusModel resBean = new StatusModel();
        resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
        resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        resBean.setWorkType(workType);
        resBean.setLimitNum(limitNum);
        return resBean;
    }




    /**
     * @Description: 组装查询定时任务手机短信的查询条件
     * @param limitNum - 多少条数据
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
     */
    public static StatusModel assembleTaskByMobileCardDataQuery(int limitNum){
        StatusModel resBean = new StatusModel();
        resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
        resBean.setLimitNum(limitNum);
        return resBean;
    }


    /**
     * @Description: 组装查询定时任务银行卡数据填充的查询条件
     * @param limitNum - 多少条数据
     * @param workType - 补充数据的类型：1初始化，2补充数据失败（未匹配到银行卡的数据），3补充数据成功
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
     */
    public static StatusModel assembleTaskBankCollectionDataByWorkTypeQuery(int limitNum, int workType){
        StatusModel resBean = new StatusModel();
//        resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
//        resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
        resBean.setWorkType(workType);
        resBean.setLimitNum(limitNum);
        return resBean;
    }



    /**
     * @Description: 组装更改运行状态的数据-手机卡短信类容
     * @param id - 主键ID
     * @param runStatus - 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功
     * @return StatusModel
     * @author yoko
     * @date 2019/12/10 10:42
     */
    public static StatusModel assembleUpdateStatusByMobileCardDataModel(long id, int runStatus, int dataType){
        StatusModel resBean = new StatusModel();
        resBean.setId(id);
        resBean.setRunStatus(runStatus);
        if (runStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
            // 表示失败：失败则需要运行次数加一
            resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
        }
        resBean.setDataType(dataType);
        return resBean;
    }


    /**
     * @Description: 组装更改运行状态的数据-数据填充
     * @param id - 主键ID
     * @param workType - 补充数据的类型：1初始化，2补充数据失败（），3补充数据成功
     * @param info - 纪录失败的原因
     * @return StatusModel
     * @author yoko
     * @date 2019/12/10 10:42
     */
    public static StatusModel assembleUpdateStatusByWorkType(long id, int workType, String info){
        StatusModel resBean = new StatusModel();
        resBean.setId(id);
        if (!StringUtils.isBlank(info)){
            resBean.setInfo(info);
        }
        resBean.setWorkType(workType);
        return resBean;
    }


    /**
     * @Description: 组装查询定时任务根据订单状态-失效订单、成功订单等。
     * @param limitNum - 多少条数据
     * @param orderStatus - 订单状态：1初始化，2超时/失败，3有质疑，4成功
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
     */
    public static StatusModel assembleTaskByOrderStatusQuery(int limitNum, int orderStatus){
        StatusModel resBean = new StatusModel();
        resBean.setOrderStatus(orderStatus);
        resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
        resBean.setLimitNum(limitNum);
        return resBean;
    }

}
