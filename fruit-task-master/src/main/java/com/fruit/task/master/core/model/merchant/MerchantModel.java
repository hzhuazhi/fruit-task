package com.fruit.task.master.core.model.merchant;
import com.fruit.task.master.core.protocol.page.BasePage;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 卡商扩充数据的实体属性Bean
 * @Author yoko
 * @Date 2020/9/8 17:03
 * @Version 1.0
 */
public class MerchantModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223201103L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 归属的账号ID：对应表tb_hz_sys_account的主键ID，并且角色类型是卡商
     */
    private Long accountId;

    /**
     * 名称/别名
     */
    private String alias;

    /**
     * 总额：总共跑量的金额
     */
    private String totalMoney;

    /**
     * 保底金额：卡商在我放至少要留有多少钱，才放量：保证金
     */
    private String leastMoney;

    /**
     * 余额：跑量的金额累加，渠道提现的金额扣减
     */
    private String balance;

    /**
     * 锁定金额
     */
    private String lockMoney;

    /**
     * 卡商类型：1我方卡商，2第三方卡商
     */
    private Integer merchantType;

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

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 账号昵称
     */
    private String acName;

    /**
     * 账号联系人
     */
    private String acContacts;

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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getLeastMoney() {
        return leastMoney;
    }

    public void setLeastMoney(String leastMoney) {
        this.leastMoney = leastMoney;
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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Integer getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(Integer merchantType) {
        this.merchantType = merchantType;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public String getAcContacts() {
        return acContacts;
    }

    public void setAcContacts(String acContacts) {
        this.acContacts = acContacts;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getLockMoney() {
        return lockMoney;
    }

    public void setLockMoney(String lockMoney) {
        this.lockMoney = lockMoney;
    }
}
