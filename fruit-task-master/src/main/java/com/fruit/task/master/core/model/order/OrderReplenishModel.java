package com.fruit.task.master.core.model.order;
import com.fruit.task.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 订单补单的实体属性Bean
 * @Author yoko
 * @Date 2020/9/11 21:43
 * @Version 1.0
 */
public class OrderReplenishModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223201133L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 商家订单号
     */
    private String outTradeNo;

    /**
     * 订单状态：1初始化，2超时/失败，3成功
     */
    private Integer orderStatus;

    /**
     * 处理人：操作人
     */
    private String handlePeople;

    /**
     * 数据说明：做解说用的
     */
    private String dataExplain;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建日期：存的日期格式20160530
     */
    private Integer curday;

    /**
     * 创建所属小时：24小时制
     */
    private Integer curhour;

    /**
     * 创建所属分钟：60分钟制
     */
    private Integer curminute;

    /**
     *运行计算次数
     */
    private Integer runNum;

    /**
     * 运行计算状态：0初始化，1锁定，2计算失败，3计算成功
     */
    private Integer runStatus;


    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 是否有效：0有效，1无效/删除
     */
    private Integer yn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getHandlePeople() {
        return handlePeople;
    }

    public void setHandlePeople(String handlePeople) {
        this.handlePeople = handlePeople;
    }

    public String getDataExplain() {
        return dataExplain;
    }

    public void setDataExplain(String dataExplain) {
        this.dataExplain = dataExplain;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getCurday() {
        return curday;
    }

    public void setCurday(Integer curday) {
        this.curday = curday;
    }

    public Integer getCurhour() {
        return curhour;
    }

    public void setCurhour(Integer curhour) {
        this.curhour = curhour;
    }

    public Integer getCurminute() {
        return curminute;
    }

    public void setCurminute(Integer curminute) {
        this.curminute = curminute;
    }

    public Integer getRunNum() {
        return runNum;
    }

    public void setRunNum(Integer runNum) {
        this.runNum = runNum;
    }

    public Integer getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(Integer runStatus) {
        this.runStatus = runStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }
}
