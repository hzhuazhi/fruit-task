package com.fruit.task.master.core.model.bank;


import com.fruit.task.master.core.protocol.page.BasePage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 银行/银行卡的实体属性Bean
 * @Author yoko
 * @Date 2020/9/11 15:36
 * @Version 1.0
 */
public class BankModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223201121L;

    public BankModel(){

    }

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 名称/别名
     */
    private String alias;

    /**
     * 归属手机号ID：对应表tb_fr_mobile_card的主键ID
     */
    private Long mobileCardId;

    /**
     * 银行卡归属类型：对应表tb_fr_bank_type的主键ID
     */
    private Long bankTypeId;

    /**
     * 归属卡商ID：对应表tb_hz_sys_account的主键ID，并且角色是卡商
     */
    private Long accountId;

    /**
     * 银行名称/归属开户行
     */
    private String bankName;

    /**
     * 银行卡账号/银行卡号
     */
    private String bankCard;

    /**
     * 银行支行/支行名称
     */
    private String subbranchName;

    /**
     * 开户名
     */
    private String accountName;

    /**
     * 银行码
     */
    private String bankCode;

    /**
     * 开户地省份/所在省份
     */
    private String province;

    /**
     * 开户地城市/所在城市
     */
    private String city;

    /**
     * 回调对照凭证短信来源:例如招商银行的是95555
     */
    private String smsNum;

    /**
     * 回调对照凭证银行卡尾号：收到短信后，短信内容尾号xxxx收款
     */
    private String lastNum;

    /**
     * 收款日限金额
     */
    private String inDayMoney;

    /**
     * 收款月限金额
     */
    private String inMonthMoney;

    /**
     * 转账日限金额
     */
    private String outDayMoney;

    /**
     * 转账月限金额
     */
    private String outMonthMoney;

    /**
     * 归属手机卡是否欠费：1未欠费，2欠费
     */
    private Integer isArrears;


    /**
     * 检测状态：1初始化正常，2不正常
     */
    private Integer checkStatus;

    /**
     * 检测被限制的原因:task跑日月总限制，如果被限制，连续给出订单失败会填充被限制的原因
     */
    private String dataExplain;

    /**
     * 是否测试通过：1未通过，2通过；收到银行卡短信，并且解析短信模板配置正确
     */
    private Integer isOk;

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
     * 手机号
     */
    private String phoneNum;





    /**
     * 归属银行卡ID：对应表tb_fr_bank的主键ID
     */
    private Long bankId;


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
     * 卡商ID集合-SQL
     */
    private List<Long> accountIdList;

    /**
     * 手机卡ID集合-SQL
     */
    private List<Long> mobileCardIdList;

    /**
     * 商户与银行卡绑定关系的银行卡ID集合-SQL
     */
    private List<Long> yesBankIdList;

    /**
     * 商户与银行卡绑定关系的银行卡ID集合-not in -SQL
     */
    private List<Long> noBankIdList;

    /**
     * 账号昵称
     */
    private String acName;

    /**
     * 账号联系人
     */
    private String acContacts;

    /**
     * 金额
     */
    private BigDecimal money;


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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(Long bankTypeId) {
        this.bankTypeId = bankTypeId;
    }


    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getSubbranchName() {
        return subbranchName;
    }

    public void setSubbranchName(String subbranchName) {
        this.subbranchName = subbranchName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSmsNum() {
        return smsNum;
    }

    public void setSmsNum(String smsNum) {
        this.smsNum = smsNum;
    }

    public String getLastNum() {
        return lastNum;
    }

    public void setLastNum(String lastNum) {
        this.lastNum = lastNum;
    }

    public String getInDayMoney() {
        return inDayMoney;
    }

    public void setInDayMoney(String inDayMoney) {
        this.inDayMoney = inDayMoney;
    }

    public String getInMonthMoney() {
        return inMonthMoney;
    }

    public void setInMonthMoney(String inMonthMoney) {
        this.inMonthMoney = inMonthMoney;
    }

    public String getOutDayMoney() {
        return outDayMoney;
    }

    public void setOutDayMoney(String outDayMoney) {
        this.outDayMoney = outDayMoney;
    }

    public String getOutMonthMoney() {
        return outMonthMoney;
    }

    public void setOutMonthMoney(String outMonthMoney) {
        this.outMonthMoney = outMonthMoney;
    }

    public Integer getIsArrears() {
        return isArrears;
    }

    public void setIsArrears(Integer isArrears) {
        this.isArrears = isArrears;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getDataExplain() {
        return dataExplain;
    }

    public void setDataExplain(String dataExplain) {
        this.dataExplain = dataExplain;
    }

    public Integer getIsOk() {
        return isOk;
    }

    public void setIsOk(Integer isOk) {
        this.isOk = isOk;
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

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
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

    public List<Long> getAccountIdList() {
        return accountIdList;
    }

    public void setAccountIdList(List<Long> accountIdList) {
        this.accountIdList = accountIdList;
    }

    public List<Long> getMobileCardIdList() {
        return mobileCardIdList;
    }

    public void setMobileCardIdList(List<Long> mobileCardIdList) {
        this.mobileCardIdList = mobileCardIdList;
    }

    public List<Long> getYesBankIdList() {
        return yesBankIdList;
    }

    public void setYesBankIdList(List<Long> yesBankIdList) {
        this.yesBankIdList = yesBankIdList;
    }

    public List<Long> getNoBankIdList() {
        return noBankIdList;
    }

    public void setNoBankIdList(List<Long> noBankIdList) {
        this.noBankIdList = noBankIdList;
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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
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
}
