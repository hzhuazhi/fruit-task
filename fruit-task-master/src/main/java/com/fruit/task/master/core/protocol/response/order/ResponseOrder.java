package com.fruit.task.master.core.protocol.response.order;

import com.fruit.task.master.core.protocol.base.BaseResponse;

import java.io.Serializable;

/**
 * @Description 协议：任务订单（平台派发订单）
 * @Author yoko
 * @Date 2020/5/22 11:04
 * @Version 1.0
 */
public class ResponseOrder extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233023131150L;

    public Order order;// 正式派单成功的数据
    public Integer rowCount;



    public ResponseOrder(){

    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public Integer getRowCount() {
        return rowCount;
    }

    @Override
    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }
}
