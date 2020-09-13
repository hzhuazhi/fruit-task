package com.fruit.task.master.core.model.bank;


import com.fruit.task.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 银行收款短信解析策略的实体属性Bean
 * @Author yoko
 * @Date 2020/9/11 16:49
 * @Version 1.0
 */
public class BankShortMsgStrategyModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223201130L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 银行卡归属类型：对应表tb_fr_bank_type的主键ID
     */
    private Long bankTypeId;

    /**
     * 名称/别名
     */
    private String alias;

    /**
     * 回调对照凭证内容：短信的全部内容
     *             例如：【南京银行】您尾号5298的账号与10月10日15时11份收到由无锡公司汇入的300.00元。
     *             短信内容模板案例：每一次新没见过的短信的所有内容都存放于此字段，以便后续参考
     */
    private String smsContent;

    /**
     * 回调对照凭证短信来源:例如招商银行的是95555
     */
    private String smsNum;

    /**
     * 金额截取开始：开始截取金额的关键字
     */
    private String startMoney;

    /**
     * 金额截取结束：结束截取金额的关键字
     */
    private String endMoney;

    /**
     * 截取银行卡尾号开始
     */
    private String startLastNum;

    /**
     * 截取银行卡尾号结束
     */
    private String endLastNum;

    /**
     * 截取银行短信余额开始
     */
    private String startBalance;

    /**
     * 截取银行短信余额结束
     */
    private String endBalance;

    /**
     * 备注
     */
    private String remark;

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

    public Long getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(Long bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public String getSmsNum() {
        return smsNum;
    }

    public void setSmsNum(String smsNum) {
        this.smsNum = smsNum;
    }

    public String getStartMoney() {
        return startMoney;
    }

    public void setStartMoney(String startMoney) {
        this.startMoney = startMoney;
    }

    public String getEndMoney() {
        return endMoney;
    }

    public void setEndMoney(String endMoney) {
        this.endMoney = endMoney;
    }

    public String getStartLastNum() {
        return startLastNum;
    }

    public void setStartLastNum(String startLastNum) {
        this.startLastNum = startLastNum;
    }

    public String getEndLastNum() {
        return endLastNum;
    }

    public void setEndLastNum(String endLastNum) {
        this.endLastNum = endLastNum;
    }

    public String getStartBalance() {
        return startBalance;
    }

    public void setStartBalance(String startBalance) {
        this.startBalance = startBalance;
    }

    public String getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(String endBalance) {
        this.endBalance = endBalance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
