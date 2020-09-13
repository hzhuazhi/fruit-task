package com.fruit.task.master.core.model.shortchain;
import com.fruit.task.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 短链API的实体属性Bean
 * @Author yoko
 * @Date 2020/9/8 21:34
 * @Version 1.0
 */
public class ShortChainModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223201112L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 名称/别名
     */
    private String alias;


    /**
     * 接口地址
     */
    private String interfaceAds;

    /**
     * 顺序/位置
     */
    private Integer seat;

    /**
     * 比例
     */
    private Integer ratio;

    /**
     * 到期时间/失效时间
     */
    private String invalidTime;

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

    public String getInterfaceAds() {
        return interfaceAds;
    }

    public void setInterfaceAds(String interfaceAds) {
        this.interfaceAds = interfaceAds;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    public Integer getRatio() {
        return ratio;
    }

    public void setRatio(Integer ratio) {
        this.ratio = ratio;
    }

    public String getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        this.invalidTime = invalidTime;
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
