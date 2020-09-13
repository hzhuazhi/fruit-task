package com.fruit.task.master.core.model.shortmsg;
import com.fruit.task.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 短信的类型定位策略的实体属性Bean
 * @Author yoko
 * @Date 2020/9/11 14:51
 * @Version 1.0
 */
public class ShortMsgStrategyModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301148L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 类型名称/别名
     */
    private String alias;

    /**
     * 短信类型：1欠费短信，2银行短信，3银行转出短信，4银行验证码短信
     */
    private Integer shortMsgType;

    /**
     * 端口号集合：银行类型的短信不适用此字段，只有手机欠费等短信才会使用，以英文逗号分割
     */
    private String smsNum;

    /**
     * 关键字规则：支持多个，以#号隔开
     */
    private String keyValue;

    /**
     * 关键字个数：符合多少个关键字
     */
    private Integer keyNum;

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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getShortMsgType() {
        return shortMsgType;
    }

    public void setShortMsgType(Integer shortMsgType) {
        this.shortMsgType = shortMsgType;
    }

    public String getSmsNum() {
        return smsNum;
    }

    public void setSmsNum(String smsNum) {
        this.smsNum = smsNum;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public Integer getKeyNum() {
        return keyNum;
    }

    public void setKeyNum(Integer keyNum) {
        this.keyNum = keyNum;
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
