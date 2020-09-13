package com.fruit.task.master.core.protocol.request.order;



import com.fruit.task.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description 协议：任务订单（平台派发订单）-内部调用
 * @Author yoko
 * @Date 2020/5/22 10:54
 * @Version 1.0
 */
public class RequestOrder extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233283332513L;

    /**
     * 订单号
     */
    public String orderNo;

    public RequestOrder(){

    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
