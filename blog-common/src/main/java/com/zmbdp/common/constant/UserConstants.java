package com.zmbdp.common.constant;

public class UserConstants {
    /**
     * 注册队列名称
     */
    public static final String REGISTER_QUEUE_NAME = "register.queue";
    /**
     * 验证码队列名称
     */
    public static final String CAPTCHA_QUEUE_NAME = "captcha.queue";

    /**
     * 用户交换机名称
     */
    public static final String USER_EXCHANGE_NAME = "user.exchange";
/*==========================================      延时队列      ==========================================*/
    /**
     * 延迟队列名称
     */
    public static final String USER_DELAY_QUEUE_NAME = "user.delay.queue";

    /**
     * 延迟交换机名称
     */
    public static final String USER_DELAY_EXCHANGE_NAME = "user.delay.exchange";

    /**
     * 延迟消息的路由键
     */
    public static final String USER_DELAY_ROUTING_KEY = "update.userInfo";

    /**
     * 死信交换机 DLX 名称
     */
    public static final String USER_DELAY_DLX_EXCHANGE_NAME = "user.delay.dlx.exchange";
    /**
     * 死信队列 DLX 队列名称
     */
    public static final String USER_DELAY_DLX_QUEUE_NAME = "user.delay.dlx.queue";

    /**
     * 死信消息 DLX 路由键
     */
    public static final String USER_DELAY_DLX_ROUTING_KEY = "dlx.userInfo";


}
