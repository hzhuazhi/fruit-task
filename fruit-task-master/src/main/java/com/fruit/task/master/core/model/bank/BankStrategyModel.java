package com.fruit.task.master.core.model.bank;


import com.fruit.task.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 银行卡放量策略的实体属性Bean
 * @Author yoko
 * @Date 2020/9/11 16:39
 * @Version 1.0
 */
public class BankStrategyModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223201105L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 归属银行卡ID：对应表tb_fr_bank的主键ID
     */
    private Long bankId;

    /**
     * 名称/别名
     */
    private String alias;

    /**
     * 优先级：就是填入数字，数字越小的优先级越靠前
     */
    private Integer priority;

    /**
     * 放量时间段：支持多个时间段，以#号分割
     */
    private String openTimeSlot;

    /**
     * 微信日收款限量金额
     */
    private String wxInDayMoney;

    /**
     * 微信月收款限量金额
     */
    private String wxInMonthMoney;

    /**
     * 微信日收款限量次数
     */
    private Integer wxInDayNum;

    /**
     * 支付宝日收款限量金额
     */
    private String zfbInDayMoney;

    /**
     * 支付宝月收款限量金额
     */
    private String zfbInMonthMoney;

    /**
     * 支付宝日收款限量次数
     */
    private Integer zfbInDayNum;

    /**
     * 卡日收款限量金额
     */
    private String cardInDayMoney;

    /**
     * 卡月收款限量金额
     */
    private String cardInMonthMoney;

    /**
     * 卡日收款限量次数
     */
    private Integer cardInDayNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 使用状态:1初始化有效正常使用，2无效暂停使用
     */
    private Integer useStatus;

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

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getOpenTimeSlot() {
        return openTimeSlot;
    }

    public void setOpenTimeSlot(String openTimeSlot) {
        this.openTimeSlot = openTimeSlot;
    }

    public String getWxInDayMoney() {
        return wxInDayMoney;
    }

    public void setWxInDayMoney(String wxInDayMoney) {
        this.wxInDayMoney = wxInDayMoney;
    }

    public String getWxInMonthMoney() {
        return wxInMonthMoney;
    }

    public void setWxInMonthMoney(String wxInMonthMoney) {
        this.wxInMonthMoney = wxInMonthMoney;
    }

    public Integer getWxInDayNum() {
        return wxInDayNum;
    }

    public void setWxInDayNum(Integer wxInDayNum) {
        this.wxInDayNum = wxInDayNum;
    }

    public String getZfbInDayMoney() {
        return zfbInDayMoney;
    }

    public void setZfbInDayMoney(String zfbInDayMoney) {
        this.zfbInDayMoney = zfbInDayMoney;
    }

    public String getZfbInMonthMoney() {
        return zfbInMonthMoney;
    }

    public void setZfbInMonthMoney(String zfbInMonthMoney) {
        this.zfbInMonthMoney = zfbInMonthMoney;
    }

    public Integer getZfbInDayNum() {
        return zfbInDayNum;
    }

    public void setZfbInDayNum(Integer zfbInDayNum) {
        this.zfbInDayNum = zfbInDayNum;
    }

    public String getCardInDayMoney() {
        return cardInDayMoney;
    }

    public void setCardInDayMoney(String cardInDayMoney) {
        this.cardInDayMoney = cardInDayMoney;
    }

    public String getCardInMonthMoney() {
        return cardInMonthMoney;
    }

    public void setCardInMonthMoney(String cardInMonthMoney) {
        this.cardInMonthMoney = cardInMonthMoney;
    }

    public Integer getCardInDayNum() {
        return cardInDayNum;
    }

    public void setCardInDayNum(Integer cardInDayNum) {
        this.cardInDayNum = cardInDayNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
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
