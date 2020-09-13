package com.fruit.task.master.core.model.bank;


import com.fruit.task.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 银行卡类型的实体属性Bean
 * @Author yoko
 * @Date 2020/9/8 19:24
 * @Version 1.0
 */
public class BankTypeModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223201107L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 名称/别名
     */
    private String alias;


    /**
     * 银行名称/归属开户行
     */
    private String bankName;

    /**
     * 银行码/银行简码
     */
    private String bankCode;

    /**
     * 回调对照凭证短信来源:例如招商银行的是95555
     */
    private String smsNum;

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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getSmsNum() {
        return smsNum;
    }

    public void setSmsNum(String smsNum) {
        this.smsNum = smsNum;
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
