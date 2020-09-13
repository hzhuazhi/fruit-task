package com.fruit.task.master.core.model.shortmsg;

import com.fruit.task.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 手机卡欠费短信的实体属性Bean
 * @Author yoko
 * @Date 2020/9/11 15:10
 * @Version 1.0
 */
public class ShortMsgArrearsModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223201111L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 归属手机号ID：对应表tb_fn_mobile_card的主键ID
     */
    private Long mobileCardId;

    /**
     * 回调数据的手机号
     */
    private String phoneNum;

    /**
     * 回调对照凭证短信来源:例如招商银行的是95555
     */
    private String smsNum;

    /**
     * 回调对照凭证内容：短信的全部内容
     *             例如：【南京银行】您尾号5298的账号与10月10日15时11份收到由无锡公司汇入的300.00元。
     *             这里的凭证内容：这里内容是多个内容，以英文逗号进行隔开。例如：【南京银行】您尾号5
     */
    private String smsContent;

    /**
     * 短信内容的类型：1欠费短信
     */
    private Integer dataType;

    /**
     * 涉及到的银行卡：存储银行卡主键ID，以英文逗号分割
     */
    private String involveBank;

    /**
     * 处理状态：1初始化/未处理，2已处理，3已处理并打开银行卡
     */
    private Integer handleType;

    /**
     * 处理人
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
     * 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功
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

    private Integer curdayStart;
    private Integer curdayEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMobileCardId() {
        return mobileCardId;
    }

    public void setMobileCardId(Long mobileCardId) {
        this.mobileCardId = mobileCardId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getSmsNum() {
        return smsNum;
    }

    public void setSmsNum(String smsNum) {
        this.smsNum = smsNum;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getInvolveBank() {
        return involveBank;
    }

    public void setInvolveBank(String involveBank) {
        this.involveBank = involveBank;
    }

    public Integer getHandleType() {
        return handleType;
    }

    public void setHandleType(Integer handleType) {
        this.handleType = handleType;
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

    public Integer getCurdayStart() {
        return curdayStart;
    }

    public void setCurdayStart(Integer curdayStart) {
        this.curdayStart = curdayStart;
    }

    public Integer getCurdayEnd() {
        return curdayEnd;
    }

    public void setCurdayEnd(Integer curdayEnd) {
        this.curdayEnd = curdayEnd;
    }
}
