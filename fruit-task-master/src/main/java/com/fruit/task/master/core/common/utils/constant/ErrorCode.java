package com.fruit.task.master.core.common.utils.constant;

/**
 * @author df
 * @Description:异常状态码
 * @create 2018-07-27 11:13
 **/
public class ErrorCode {

    /**
     * 常量异常
     */
    public final class ERROR_CONSTANT {
        /**
         * 没有被捕捉到的异常
         * 默认系统异常状态码=255
         */
        public static final String DEFAULT_EXCEPTION_ERROR_CODE = "255";

        /**
         * 没有被捕捉到的异常
         * 默认系统异常错误信息=SYS_ERROR
         */
        public static final String DEFAULT_EXCEPTION_ERROR_MESSAGE = "ERROR";

        /**
         * 被捕捉到的异常，并且捕捉的异常错误码为空，则默认异常状态码
         * 默认捕捉的异常状态码=256
         */
        public static final String DEFAULT_SERVICE_EXCEPTION_ERROR_CODE = "256";

        /**
         * 被捕捉到的异常，但是错误信息为空，则默认异常信息提醒
         * 默认捕捉的异常信息提醒=错误
         */
        public static final String DEFAULT_SERVICE_EXCEPTION_ERROR_MESSAGE = "错误";

    }


    /**
     * 异常-枚举类
     */
    public enum ENUM_ERROR {

        /***********************************************
         * OR打头表示订单的错误
         **********************************************/
        OR00001("OR00001", "错误,请重试!", "派发订单时,所有数据都为空!"),
        OR00002("OR00002", "错误,请重试!", "派发订单时,金额数据为空!"),
        OR00003("OR00003", "错误,请重试!", "派发订单时,支付类型数据为空!"),
        OR00004("OR00004", "错误,请重试!", "派发订单时,没有筛选出有效的收款账号!"),
        OR00005("OR00005", "错误,请重试!", "派发订单时,有效的用户集合数据为空!"),
        OR00006("OR00006", "金额小数点只能有2位!", "派发订单时,金额带的小数点位数错误!"),
        OR00007("OR00007", "请填写正常的数字金额!", "派发订单时,金额填写有误!"),
        OR00008("OR00008", "商户秘钥为空!", "派发订单时,商户秘钥为空!"),
        OR00009("OR00009", "错误,请重试!", "派发订单时,商户数据为空!"),
        OR00010("OR00010", "错误,请重试!", "派发订单时,卡商数据为空!"),
        OR00011("OR00011", "错误,请重试!", "派发订单时,手机卡数据为空!"),
        OR00012("OR00012", "错误,请重试!", "派发订单时,卡商的银行卡以及银行卡放量策略数据为空!"),
        OR00013("OR00013", "错误,请重试!", "派发订单时,未筛选出可用的银行卡!"),
        OR00014("OR00014", "错误,请重试!", "派发订单时,添加订单数据响应行为0!"),

        OR00015("OR00015", "错误,请重试!", "获取派单数据-详情-返回码的接口时,所有数据都为空!"),
        OR00016("OR00016", "错误,请重试!", "获取派单数据-详情-返回码的接口时,订单号数据都为空!"),
        OR00017("OR00017", "错误,请重试!", "获取派单数据-详情-返回码的接口时,获取短链API数据为空!"),



        /***********************************************
         * S打头表示策略数据的错误
         **********************************************/
        S00001("S00001", "错误,请重试!", "出码开关策略数据为空!"),
        S00002("S00002", "目前出码处于关闭状态!", "目前出码处于关闭状态!"),
        S00003("S00003", "错误,请重试!", "出码开关时间具体值策略数据为空!"),
        S00004("S00004", "目前不在出码时间范围内!", "出码开关时间,目前不在出码时间范围内!"),

        S00005("S00005", "订单金额不符合出码金额范围!", "出码时,订单金额不符合出码金额范围内!"),


        /***********************************************
         * ST打头表示统计的错误
         **********************************************/
        ST00001("ST00001", "错误,请重试!", "添加支付用户点击支付页统计时,所有数据都为空!"),
        ST00002("ST00002", "错误,请重试!", "添加支付用户点击支付页统计时,标识值数据为空!"),
        ST00003("ST00003", "错误,请重试!", "添加支付用户点击支付页统计时,数据来源类型数据为空!"),




        ;

        /**
         * 错误码
         */
        private String eCode;
        /**
         * 给客户端看的错误信息
         */
        private String eDesc;
        /**
         * 插入数据库的错误信息
         */
        private String dbDesc;




        private ENUM_ERROR(String eCode, String eDesc,String dbDesc) {
            this.eCode = eCode;
            this.eDesc = eDesc;
            this.dbDesc  = dbDesc;
        }

        public String geteCode() {
            return eCode;
        }

        public void seteCode(String eCode) {
            this.eCode = eCode;
        }

        public String geteDesc() {
            return eDesc;
        }

        public void seteDesc(String eDesc) {
            this.eDesc = eDesc;
        }

        public String getDbDesc() {
            return dbDesc;
        }

        public void setDbDesc(String dbDesc) {
            this.dbDesc = dbDesc;
        }
    }
}
