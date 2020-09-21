package com.fruit.task.master.util;
import com.fruit.task.master.core.common.utils.DateUtil;
import com.fruit.task.master.core.common.utils.StringUtil;
import com.fruit.task.master.core.common.utils.constant.ServerConstant;
import com.fruit.task.master.core.model.bank.*;
import com.fruit.task.master.core.model.merchant.MerchantModel;
import com.fruit.task.master.core.model.mobilecard.MobileCardModel;
import com.fruit.task.master.core.model.mobilecard.MobileCardShortMsgModel;
import com.fruit.task.master.core.model.order.OrderModel;
import com.fruit.task.master.core.model.shortmsg.ShortMsgArrearsModel;
import com.fruit.task.master.core.model.shortmsg.ShortMsgStrategyModel;
import com.fruit.task.master.core.model.strategy.StrategyModel;
import com.fruit.task.master.core.model.task.base.StatusModel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;
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
     * @param runType - 运行类型
     * @param workType - 运算类型
     * @param dataType - 数据类型
     * @param greaterThan - 大于
     * @param lessThan - 小于
     * @param sendType - 发送类型
     * @param orderStatus - 订单状态
     * @param invalidTime - 失效时间
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
     */
    public static StatusModel assembleTaskStatusQuery(int limitNum, int runType, int workType, int dataType, int greaterThan, int lessThan, int sendType, int orderStatus, String invalidTime){
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
        if (greaterThan > 0){
            resBean.setGreaterThan(greaterThan);
        }
        if (lessThan > 0){
            resBean.setLessThan(lessThan);
        }
        if (sendType > 0){
            resBean.setSendStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
            resBean.setSendNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (!StringUtils.isBlank(invalidTime)){
            resBean.setInvalidTime(invalidTime);
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
     * @param sendStatus - 发送状态：0初始化，1锁定，2计算失败，3计算成功
     * @param orderStatus - 订单状态
     * @param info - 解析说明
     * @return StatusModel
     * @author yoko
     * @date 2019/12/10 10:42
     */
    public static StatusModel assembleTaskUpdateStatus(long id, int runStatus, int workType, int dataType,int sendStatus,int orderStatus, String info){
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
        if (sendStatus > 0){
            resBean.setSendStatus(sendStatus);
            if (sendStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
                // 表示失败：失败则需要运行次数加一
                resBean.setSendNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            }
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
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

    /**
     * @Description: 组装查询手机号的查询方法
     * @param id - 主键ID
     * @param phoneNum - 手机号
     * @param isArrears - 是否欠费：1未欠费，2欠费
     * @param heartbeatStatus - 心跳状态：1初始化异常，2正常
     * @param useStatus - 使用状态:1初始化有效正常使用，2无效暂停使用
     * @return com.hz.fruit.master.core.model.mobilecard.MobileCardModel
     * @author yoko
     * @date 2020/9/12 14:53
     */
    public static MobileCardModel assembleMobileCardQuery(long id, String phoneNum, int isArrears, int heartbeatStatus, int useStatus){
        MobileCardModel resBean = new MobileCardModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (!StringUtils.isBlank(phoneNum)){
            resBean.setPhoneNum(phoneNum);
        }
        if (isArrears > 0){
            resBean.setIsArrears(isArrears);
        }
        if (heartbeatStatus > 0){
            resBean.setHeartbeatStatus(heartbeatStatus);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }

    /**
     * @Description: 更新所有短信的手机ID
     * @param id - 短信的主键ID
     * @param mobileCardId - 手机卡的主键ID
     * @return
     * @author yoko
     * @date 2020/9/14 14:09
    */
    public static MobileCardShortMsgModel assembleMobileCardShortMsgUpdateMobileCardId(long id, long mobileCardId){
        MobileCardShortMsgModel resBean = new MobileCardShortMsgModel();
        resBean.setId(id);
        resBean.setMobileCardId(mobileCardId);
        return resBean;
    }


    /**
     * @Description: 组装添加欠费短信
     * @param mobileCardShortMsgModel
     * @return
     * @author yoko
     * @date 2020/9/14 15:03
    */
    public static ShortMsgArrearsModel assembleShortMsgArrearsAdd(MobileCardShortMsgModel mobileCardShortMsgModel){
        ShortMsgArrearsModel resBean = new ShortMsgArrearsModel();
        if (mobileCardShortMsgModel.getMobileCardId() != null && mobileCardShortMsgModel.getMobileCardId() > 0){
            resBean.setMobileCardId(mobileCardShortMsgModel.getMobileCardId());
        }else {
            return null;
        }
        if (!StringUtils.isBlank(mobileCardShortMsgModel.getPhoneNum())){
            resBean.setPhoneNum(mobileCardShortMsgModel.getPhoneNum());
        }else {
            return null;
        }
        if (!StringUtils.isBlank(mobileCardShortMsgModel.getSmsNum())){
            resBean.setSmsNum(mobileCardShortMsgModel.getSmsNum());
        }else {
            return null;
        }
        if (!StringUtils.isBlank(mobileCardShortMsgModel.getSmsContent())){
            resBean.setSmsContent(mobileCardShortMsgModel.getSmsContent());
        }else {
            return null;
        }

        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }

    /**
     * @Description: 组装添加银行短信
     * @param mobileCardShortMsgModel
     * @return
     * @author yoko
     * @date 2020/9/14 15:03
     */
    public static BankShortMsgModel assembleBankShortMsgAdd(MobileCardShortMsgModel mobileCardShortMsgModel){
        BankShortMsgModel resBean = new BankShortMsgModel();
        if (mobileCardShortMsgModel.getMobileCardId() != null && mobileCardShortMsgModel.getMobileCardId() > 0){
            resBean.setMobileCardId(mobileCardShortMsgModel.getMobileCardId());
        }else {
            return null;
        }
        if (!StringUtils.isBlank(mobileCardShortMsgModel.getPhoneNum())){
            resBean.setPhoneNum(mobileCardShortMsgModel.getPhoneNum());
        }else {
            return null;
        }
        if (!StringUtils.isBlank(mobileCardShortMsgModel.getSmsNum())){
            resBean.setSmsNum(mobileCardShortMsgModel.getSmsNum());
        }else {
            return null;
        }
        if (!StringUtils.isBlank(mobileCardShortMsgModel.getSmsContent())){
            resBean.setSmsContent(mobileCardShortMsgModel.getSmsContent());
        }else {
            return null;
        }

        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }


    /**
     * @Description: 组装查询银行短信解析的策略数据
     * @param id - 主键ID
     * @param bankTypeId - 银行类型ID
     * @param smsNum - 短信来源端口号
     * @return com.fruit.task.master.core.model.bank.BankShortMsgStrategyModel
     * @author yoko
     * @date 2020/9/14 17:12
     */
    public static BankShortMsgStrategyModel assembleBankShortMsgStrategyQuery(long id, long bankTypeId, String smsNum){
        BankShortMsgStrategyModel resBean = new BankShortMsgStrategyModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (bankTypeId > 0){
            resBean.setBankTypeId(bankTypeId);
        }
        if (!StringUtils.isBlank(smsNum)){
            resBean.setSmsNum(smsNum);
        }
        return resBean;
    }

    /**
     * @Description: 组装查询银行卡的查询条件
     * @param id - 主键ID
     * @param mobileCardId - 手机卡ID
     * @param bankTypeId - 银行类型ID
     * @param accountId - 卡商ID
     * @param bankCard - 银行卡卡号
     * @param smsNum - 短信来源端口号
     * @param lastNum - 银行卡尾号
     * @return com.fruit.task.master.core.model.bank.BankModel
     * @author yoko
     * @date 2020/9/14 17:19
     */
    public static BankModel assembleBankQuery(long id, long mobileCardId, long bankTypeId, long accountId, String bankCard, String smsNum, String lastNum){
        BankModel resBean = new BankModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (mobileCardId > 0){
            resBean.setMobileCardId(mobileCardId);
        }
        if (bankTypeId > 0){
            resBean.setBankTypeId(bankTypeId);
        }
        if (accountId > 0){
            resBean.setAccountId(accountId);
        }
        if (!StringUtils.isBlank(bankCard)){
            resBean.setBankCard(bankCard);
        }
        if (!StringUtils.isBlank(smsNum)){
            resBean.setSmsNum(smsNum);
        }
        if (!StringUtils.isBlank(lastNum)){
            resBean.setLastNum(lastNum);
        }
        return resBean;
    }


    /**
     * @Description: 截取短信内容中的金额
     * <p>
     *     截取银行收款短信的收款金额；
     *     判断最终截取的字符串是否是金额
     * </p>
     * @param bankShortMsgStrategyList - 截取银行短信金额的数据
     * @param smsContent - 银行短信
     * @return java.lang.String
     * @author yoko
     * @date 2020/9/14 17:48
     */
    public static String getBankMoney(List<BankShortMsgStrategyModel> bankShortMsgStrategyList, String smsContent){
        String str = null;
        int startIndex = 0;
        int endIndex = 0;
        for (BankShortMsgStrategyModel bankShortMsgStrategyModel : bankShortMsgStrategyList){
            startIndex = getIndexOfByStr(smsContent, bankShortMsgStrategyModel.getStartMoney());
            if (startIndex > 0){
                startIndex = startIndex + bankShortMsgStrategyModel.getStartMoney().length();
            }else {
                continue;
            }

            endIndex = getIndexOfByStr(smsContent, bankShortMsgStrategyModel.getEndMoney());
            if (endIndex > 0){
            }else {
                continue;
            }

            if (startIndex <= 0 || endIndex <= 0){
                continue;
            }

            String money = smsContent.substring(startIndex, endIndex).replaceAll(",","");
            if (StringUtils.isBlank(money)){
                continue;
            }

            // 判断是否是金额
            boolean flag = StringUtil.isNumberByMoney(money);
            if (flag){
                str = money;
            }
        }
        return str;
    }

    /**
     * @Description: 获取key在内容中的下标位
     * @param content - 类容
     * @param key - 匹配的关键字
     * @return
     * @author yoko
     * @date 2020/6/4 11:18
     */
    public static int getIndexOfByStr(String content, String key){
        if (content.indexOf(key) > -1){
            return content.indexOf(key);
        }else {
            return 0;
        }
    }


    /**
     * @Description: 组装查询策略数据条件的方法
     * @return com.pf.play.rule.core.model.strategy.StrategyModel
     * @author yoko
     * @date 2020/5/19 17:12
     */
    public static StrategyModel assembleStrategyQuery(int stgType){
        StrategyModel resBean = new StrategyModel();
        resBean.setStgType(stgType);
        return resBean;
    }


    /**
     * @Description: 解析短信获取银行卡
     * <p>
     *     解析短信，根据短信的尾号匹配银行卡的尾号；
     *     如果可以匹配到银行卡的尾号，则返回
     * </p>
     * @param bankList - 银行卡集合
     * @param smsContent - 短信内容
     * @param lastNumKey - 尾号开始位的关键字
     * @return long
     * @author yoko
     * @date 2020/9/14 19:13
     */
    public static BankModel getBankIdBySmsContent(List<BankModel> bankList, String smsContent, String lastNumKey){
        String [] lastNumKeyArr = lastNumKey.split("#");
        for (BankModel bankModel : bankList){
            for (String str : lastNumKeyArr){
                int start = 0;
                int end = 0;
                if (smsContent.indexOf(str) > -1){
                    start = smsContent.indexOf(str) + str.length();
                    end = start + bankModel.getLastNum().length();
                    // 从短信内容中截取银行卡尾号
                    String sms_lastNum = smsContent.substring(start, end);
                    if (!StringUtils.isBlank(sms_lastNum)){
                        if (sms_lastNum.equals(bankModel.getLastNum())){
                            return bankModel;
                        }
                    }
                }
            }
        }
        return null;
    }


    /**
     * @Description: TODO
     * @param id - 主键ID
     * @param orderNo - 订单号
     * @param bankId - 银行卡主键ID
     * @param bankTypeId - 银行卡类型
     * @param money - 收款金额
     * @param lastNum - 银行卡尾号
     * @return com.fruit.task.master.core.model.bank.BankShortMsgStrategyModel
     * @author yoko
     * @date 2020/9/14 19:59
     */
    public static BankShortMsgModel assembleBankShortMsgUpdate(long id, String orderNo, long bankId, long bankTypeId, String money, String lastNum){
        BankShortMsgModel resBean = new BankShortMsgModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (!StringUtils.isBlank(orderNo)){
            resBean.setOrderNo(orderNo);
        }
        if (bankId > 0){
            resBean.setBankId(bankId);
        }
        if (bankTypeId > 0){
            resBean.setBankTypeId(bankTypeId);
        }
        if (!StringUtils.isBlank(money)){
            resBean.setMoney(money);
        }
        if (!StringUtils.isBlank(lastNum)){
            resBean.setLastNum(lastNum);
        }
        return resBean;
    }


    /**
     * @Description: 组装查询订单信息的查询条件
     * @param id - 订单主键ID
     * @param bankId - 银行卡ID
     * @param orderNo - 订单号
     * @param orderType - 订单类型
     * @param orderMoney - 订单金额
     * @param orderStatus - 订单状态
     * @param orderStatusStr - 订单大于等于状态
     * @param replenishType - 补单类型
     * @param startTime - 创建时间的开始时间
     * @param endTime - 创建时间的结束时间
     * @return com.fruit.task.master.core.model.order.OrderModel
     * @author yoko
     * @date 2020/9/14 20:54
     */
    public static OrderModel assembleOrderQuery(long id, long bankId, String orderNo, int orderType, String orderMoney, int orderStatus,
                                                      String orderStatusStr, int replenishType, String startTime, String endTime){
        OrderModel resBean = new OrderModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (bankId > 0){
            resBean.setBankId(bankId);
        }
        if (!StringUtils.isBlank(orderNo)){
            resBean.setOrderNo(orderNo);
        }
        if (orderType > 0){
            resBean.setOrderType(orderType);
        }
        if (!StringUtils.isBlank(orderMoney)){
            resBean.setOrderMoney(orderMoney);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (!StringUtils.isBlank(orderStatusStr)){
            resBean.setOrderStatusStr(orderStatusStr);
        }
        if (replenishType > 0){
            resBean.setReplenishType(replenishType);
        }
        if (!StringUtils.isBlank(startTime) && !StringUtils.isBlank(endTime)){
            resBean.setStartTime(startTime);
            resBean.setEndTime(endTime);
        }
        return resBean;
    }

    /**
     * @Description: 把订单集合的订单号汇聚成一个字符串
     * @param orderList
     * @return
     * @author yoko
     * @date 2020/9/14 21:21
    */
    public static String getOrderNoStr(List<OrderModel> orderList){
        String str = "";
        for (OrderModel orderModel : orderList){
            str += orderModel.getOrderNo() + ",";
        }
        return str;
    }

    /**
     * @Description: 更新订单号状态
     * @param id - 主键ID
     * @param orderStatus - 订单号状态
     * @return com.fruit.task.master.core.model.order.OrderModel
     * @author yoko
     * @date 2020/9/14 21:26
     */
    public static OrderModel assembleOrderUpdateStatus(long id, int orderStatus){
        OrderModel resBean = new OrderModel();
        resBean.setId(id);
        resBean.setOrderStatus(orderStatus);
        return resBean;
    }


    /**
     * @Description: 组装查询银行放量策略的查询条件
     * @param id - 主键ID
     * @param bankId - 银行卡ID
     * @param useStatus - 使用状态
     * @return com.fruit.task.master.core.model.bank.BankStrategyModel
     * @author yoko
     * @date 2020/9/15 10:29
     */
    public static BankStrategyModel assembleBankStrategyQuery(long id, long bankId, int useStatus){
        BankStrategyModel resBean = new BankStrategyModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (bankId > 0){
            resBean.setBankId(bankId);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }


    /**
     * @Description: 组装查询订单信息来限制银行卡的查询条件
     * @param bankId - 银行卡ID
     * @param orderType - 支付类型
     * @param orderStatus - 订单状态
     * @param curday - 创建日期
     * @param curdayStart - 开始日期
     * @param curdayEnd - 结束日期
     * @return com.fruit.task.master.core.model.order.OrderModel
     * @author yoko
     * @date 2020/9/15 10:58
     */
    public static OrderModel assembleOrderByLimitQuery(long bankId, int orderType, int orderStatus, int curday, int curdayStart, int curdayEnd){
        OrderModel resBean = new OrderModel();
        if (bankId > 0){
            resBean.setBankId(bankId);
        }
        if (orderType > 0){
            resBean.setOrderType(orderType);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (curday > 0){
            resBean.setCurday(curday);
        }
        if (curdayStart > 0){
            resBean.setCurdayStart(curdayStart);
        }
        if (curdayEnd > 0){
            resBean.setCurdayEnd(curdayEnd);
        }
        return resBean;
    }

    /**
     * @Description: 组装监控要查询订单的查询条件
     * @param bankId - 银行卡ID
     * @param orderType - 订单的支付类型
     * @param orderStatusStr - 订单状态
     * @param curday - 创建日期
     * @param limitNum - 查询的条数
     * @return com.fruit.task.master.core.model.order.OrderModel
     * @author yoko
     * @date 2020/9/15 16:36
     */
    public static OrderModel assembleOrderByMonitorQuery(long bankId, int orderType, String orderStatusStr, int curday, int limitNum){
        OrderModel resBean = new OrderModel();
        if (bankId > 0){
            resBean.setBankId(bankId);
        }
        if (orderType > 0){
            resBean.setOrderType(orderType);
        }
        if (!StringUtils.isBlank(orderStatusStr)){
            resBean.setOrderStatusStr(orderStatusStr);
        }
        if (curday > 0){
            resBean.setCurday(curday);
        }
        if (limitNum > 0){
            resBean.setLimitNum(limitNum);
        }
        return resBean;
    }


    /**
     * @Description: 更新银行卡信息
     * @param id - 主键ID
     * @param checkStatus - 检测状态：1初始化正常，2不正常
     * @param isArrears - 归属手机卡是否欠费：1未欠费，2欠费
     * @param dataExplain - 数据说明：检测被限制的原因:task跑日月总限制，如果被限制，连续给出订单失败会填充被限制的原因
     * @return com.fruit.task.master.core.model.bank.BankModel
     * @author yoko
     * @date 2020/9/15 16:57
     */
    public static BankModel assembleBankUpdate(long id, int checkStatus,int isArrears, String dataExplain){
        BankModel resBean = new BankModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (checkStatus > 0){
            resBean.setCheckStatus(checkStatus);
        }
        if (isArrears > 0){
            resBean.setIsArrears(isArrears);
        }
        if (!StringUtils.isBlank(dataExplain)){
            resBean.setDataExplain(dataExplain);
        }
        return resBean;
    }

    /**
     * @Description: 组装添加银行收款纪录
     * @param bankId - 银行卡主键ID
     * @param orderNo - 订单号
     * @param money - 订单金额
     * @return com.fruit.task.master.core.model.bank.BankCollectionModel
     * @author yoko
     * @date 2020/9/15 17:25
     */
    public static BankCollectionModel assembleBankCollectionAdd(long bankId, String orderNo, String money){
        BankCollectionModel resBean = new BankCollectionModel();
        resBean.setBankId(bankId);
        resBean.setOrderNo(orderNo);
        resBean.setMoney(money);
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }

    /**
     * @Description: 更新订单状态以及补单类型
     * @param id - 主键ID
     * @param orderStatus - 订单状态
     * @param replenishType - 是否是补单：1初始化不是补单，2是补单
     * @return com.fruit.task.master.core.model.order.OrderModel
     * @author yoko
     * @date 2020/9/14 21:26
     */
    public static OrderModel assembleOrderUpdateStatusAndReplenish(long id, int orderStatus, int replenishType){
        OrderModel resBean = new OrderModel();
        resBean.setId(id);
        resBean.setOrderStatus(orderStatus);
        resBean.setReplenishType(replenishType);
        return resBean;
    }

    /**
     * @Description: 跟新手机卡欠费状态
     * @param id - 主键ID
     * @param isArrears - 是否欠费：1未欠费，2欠费
     * @return com.fruit.task.master.core.model.mobilecard.MobileCardModel
     * @author yoko
     * @date 2020/9/15 19:26
     */
    public static MobileCardModel assembleMobileCardUpdateArrears(long id, int isArrears){
        MobileCardModel resBean = new MobileCardModel();
        resBean.setId(id);
        resBean.setIsArrears(isArrears);
        return resBean;
    }

    /**
     * @Description: 手机欠费更新要涉及的银行卡
     * @param id - 主键ID
     * @param involveBank - 更新涉及到的银行卡
     * @return com.fruit.task.master.core.model.shortmsg.ShortMsgArrearsModel
     * @author yoko
     * @date 2020/9/15 19:35
     */
    public static ShortMsgArrearsModel assembleShortMsgArrearsUpdateBank(long id, String involveBank){
        ShortMsgArrearsModel resBean = new ShortMsgArrearsModel();
        resBean.setId(id);
        resBean.setInvolveBank(involveBank);
        return resBean;
    }

    /**
     * @Description: 银行卡ID组合成字符串
     * @param bankList
     * @return
     * @author yoko
     * @date 2020/9/15 19:41
    */
    public static String assembleInvolveBank(List<BankModel> bankList){
        String str = "";
        if (bankList != null && bankList.size() > 0){
            for (BankModel bankModel : bankList){
                str += bankModel.getId() + ",";
            }
        }
        return str;
    }

    /**
     * @Description: 组装查询卡商的扩展信息
     * @param id - 主键ID
     * @param accountId - 卡商的账号ID
     * @param useStatus - 使用状态
     * @return com.fruit.task.master.core.model.merchant.MerchantModel
     * @author yoko
     * @date 2020/9/15 20:28
     */
    public static MerchantModel assembleMerchantQuery(long id, long accountId, int useStatus){
        MerchantModel resBean = new MerchantModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (accountId > 0){
            resBean.setAccountId(accountId);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }

    /**
     * @Description: 组装更新卡商的余额的方法
     * @param id - 主键ID
     * @param orderMoney - 订单金额
     * @return com.fruit.task.master.core.model.merchant.MerchantModel
     * @author yoko
     * @date 2020/9/15 20:41
     */
    public static MerchantModel assembleMerchantUpdateBalance(long id, String orderMoney){
        MerchantModel resBean = new MerchantModel();
        resBean.setId(id);
        BigDecimal bd = new BigDecimal(orderMoney);
        resBean.setMoney(bd);
        return resBean;
    }

    /**
     * @Description: 组装更新卡商的金额的方法
     * @param accountId - 卡商账号ID
     * @param orderMoney - 订单金额
     * @return com.fruit.task.master.core.model.merchant.MerchantModel
     * @author yoko
     * @date 2020/9/15 20:41
     */
    public static MerchantModel assembleMerchantUpdateMoney(long accountId, String orderMoney){
        MerchantModel resBean = new MerchantModel();
        resBean.setAccountId(accountId);
        BigDecimal bd = new BigDecimal(orderMoney);
        resBean.setMoney(bd);
        return resBean;
    }

}
