package com.fruit.task.master.core.protocol.response.order;

import java.io.Serializable;

/**
 * @Description协议：派单信息
 * @Author yoko
 * @Date 2020/5/29 17:16
 * @Version 1.0
 */
public class Order implements Serializable {
    private static final long   serialVersionUID = 1233023331049L;

    /**
     * 订单号
     */
    public String orderNo;

    /**
     * 支付二维码地址
     */
    public String qrCode;

    /**
     * 订单金额
     */
    public String orderMoney;

    /**
     * 订单状态：1初始化，2超时/失败，3有质疑，4成功
     */
    public Integer orderStatus;

    /**
     * 失效时间
     */
    public String invalidTime;

    /**
     * 失效时间-秒
     */
    public String invalidSecond;

    /**
     * 银行名称/归属开户行
     */
    public String bankName;

    /**
     * 银行卡账号/银行卡号
     */
    public String bankCard;

    /**
     * 开户名
     */
    public String accountName;

    /**
     * 银行简码
     */
    public String bankCode;

    /**
     * 跳转的支付页
     */
    public String qrCodeUrl;

    /**
     * 短链
     */
    public String shortChain;

    public Order(){

    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getInvalidSecond() {
        return invalidSecond;
    }

    public void setInvalidSecond(String invalidSecond) {
        this.invalidSecond = invalidSecond;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getShortChain() {
        return shortChain;
    }

    public void setShortChain(String shortChain) {
        this.shortChain = shortChain;
    }
}
