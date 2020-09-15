package com.fruit.task.master.core.common.utils.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description 加载的配置文件
 * @Author yoko
 * @Date 2019/12/30 12:37
 * @Version 1.0
 */
@Component
public class LoadConstant {

    /**
     * 开发者应用公钥证书路径
     */
    @Value("${alipay.cert.path}")
    public String certPath;

    /**
     * 支付宝公钥证书文件路径
     */

    @Value("${alipay.public.cert.path}")
    public String alipayPublicCertPath;

    /**
     * 支付宝CA根证书文件路径
     */
    @Value("${alipay.root.cert.path}")
    public String rootCertPath;

    /**
     * 支付宝的支付默认金额
     */
    @Value("${alipay.total.amount}")
    public String totalAmount;

    /**
     * 支付地址-支付宝转卡
     */
    @Value("${zfb.qrCode.url}")
    public String zfbQrCodeUrl;

    /**
     * 支付地址-卡转卡
     */
    @Value("${card.qrCode.url}")
    public String cardQrCodeUrl;

    /**
     * 默认数据同步地址
     */
    @Value("${default.notify.url}")
    public String defaultNotifyUrl;




}
