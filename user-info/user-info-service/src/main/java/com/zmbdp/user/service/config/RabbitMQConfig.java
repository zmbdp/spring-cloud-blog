package com.zmbdp.user.service.config;

import com.zmbdp.common.constant.UserConstants;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    /**
     * 声明用户相关操作的队列
     */
    @Bean("registerQueue")
    public Queue registerQueue() {
        // durable: 是否持久化 默认是 false, 里面的 name 是队列的名字
        return QueueBuilder.durable(UserConstants.REGISTER_QUEUE_NAME) // 声明邮件队列名称
                .build();
    }

    /**
     * 声明验证码队列
     */
    @Bean("captchaQueue")
    public Queue captchaQueue() {
        return QueueBuilder.durable(UserConstants.CAPTCHA_QUEUE_NAME).build(); // 声明验证码队列名称
    }

    /**
     * 声明邮件相关操作的交换机
     */
    @Bean("userExchange")
    public FanoutExchange userExchange() {
        // durable: 是否持久化 默认是 false,  Exchange 的名字
        return ExchangeBuilder.fanoutExchange(UserConstants.USER_EXCHANGE_NAME) // 声明一个交换机
                .durable(true) // 是否持久化
                .build();
    }

    /**
     * 绑定邮件队列和交换机
     */
    @Bean("userBinding")
    public Binding userBinding(@Qualifier("registerQueue") Queue userQueue, @Qualifier("userExchange") FanoutExchange userExchange) {
        return BindingBuilder.bind(userQueue).to(userExchange);
    }

    /**
     * 绑定验证码队列和交换机
     */
    @Bean("captchaBinding")
    public Binding captchaBinding(@Qualifier("captchaQueue") Queue captchaQueue, @Qualifier("userExchange") FanoutExchange userExchange) {
        return BindingBuilder.bind(captchaQueue).to(userExchange);
    }

    /**
     * ------------------------------------------------------------------------------------------------------------------
     * ------------------------------------------------     延时队列     ------------------------------------------------
     * ------------------------------------------------------------------------------------------------------------------
     */

//    /**
//     * 声明延迟队列（绑定死信队列）
//     */
//    @Bean("userDelayQueue")
//    public Queue userDelayQueue() {
//        return QueueBuilder.durable(UserConstants.USER_DELAY_QUEUE_NAME)
//                .withArgument("x-dead-letter-exchange", UserConstants.USER_DELAY_DLX_EXCHANGE_NAME)
//                .withArgument("x-dead-letter-routing-key", UserConstants.USER_DELAY_DLX_ROUTING_KEY)
//                .build();
//    }
//
//    /**
//     * 声明延迟交换机（x-delayed-message 类型）
//     */
//    @Bean("userDelayExchange")
//    public CustomExchange userDelayExchange() {
//        Map<String, Object> args = new HashMap<>();
//        args.put("x-delayed-type", "direct");
//        return new CustomExchange(
//                UserConstants.USER_DELAY_EXCHANGE_NAME,
//                "x-delayed-message",
//                true,
//                false,
//                args
//        );
//    }
//
//    /**
//     * 将延迟队列绑定到延迟交换机
//     */
//    @Bean("userDelayBinding")
//    public Binding userDelayBinding(@Qualifier("userDelayQueue") Queue userDelayQueue, @Qualifier("userDelayExchange") CustomExchange userDelayExchange) {
//        return BindingBuilder.bind(userDelayQueue)
//                .to(userDelayExchange)
//                .with(UserConstants.USER_DELAY_ROUTING_KEY)
//                .noargs();
//    }
//
//    /**
//     * ------------------------------------------------------------------------------------------------------------------
//     * ------------------------------------------------     死信队列     ------------------------------------------------
//     * ------------------------------------------------------------------------------------------------------------------
//     */
//
//    /**
//     * 声明死信队列（DLQ）
//     */
//    @Bean("userDelayDlxQueue")
//    public Queue userDelayDlxQueue() {
//        return QueueBuilder.durable(UserConstants.USER_DELAY_DLX_QUEUE_NAME).build();
//    }
//
//    /**
//     * 声明死信交换机（DLX）
//     */
//    @Bean("userDelayDlxExchange")
//    public DirectExchange userDelayDlxExchange() {
//        return new DirectExchange(UserConstants.USER_DELAY_DLX_EXCHANGE_NAME, true, false);
//    }
//
//    /**
//     * 将死信队列绑定到死信交换机
//     */
//    @Bean("userDelayDlxBinding")
//    public Binding userDelayDlxBinding(@Qualifier("userDelayDlxQueue") Queue userDelayDlxQueue, @Qualifier("userDelayDlxExchange") DirectExchange userDelayDlxExchange) {
//        return BindingBuilder.bind(userDelayDlxQueue)
//                .to(userDelayDlxExchange)
//                .with(UserConstants.USER_DELAY_DLX_ROUTING_KEY);
//    }

    /**
     * ------------------------------------------------------------------------------------------------------------------
     * ------------------------------------------------     消费者代码     ------------------------------------------------
     * ------------------------------------------------------------------------------------------------------------------
     */
//
//    /**
//     * 监听主延迟队列
//     */
//    @RabbitListener(queues = UserConstants.USER_DELAY_QUEUE_NAME)
//    public void handleDelayMessage(Map<String, Object> message, Channel channel,
//                                   @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
//        try {
//            String userId = (String) message.get("userId");
//
//            // 业务处理逻辑（例如更新用户信息）
//            if (updateUserInfo(userId)) {
//                // 处理成功，确认消息
//                channel.basicAck(deliveryTag, false);
//            } else {
//                // 处理失败，拒绝消息（不重新入队）
//                channel.basicReject(deliveryTag, false);
//            }
//        } catch (Exception e) {
//            // 记录错误日志
//            log.error("处理延迟消息失败", e);
//            try {
//                // 拒绝消息（不重新入队），将进入死信队列
//                channel.basicReject(deliveryTag, false);
//            } catch (IOException ex) {
//                log.error("拒绝消息失败", ex);
//            }
//        }
//    }
//
//    /**
//     * 监听死信队列（使用新名称）
//     */
//    @RabbitListener(queues = UserConstants.USER_DELAY_DLX_QUEUE_NAME)
//    public void handleDlxMessage(Map<String, Object> message) {
//        String userId = (String) message.get("userId");
//        log.error("收到死信消息: userId={}", userId);
//
//        // 死信消息处理逻辑（例如发送告警、人工干预）
//        sendAlertToAdmin("用户信息更新失败", "userId: " + userId);
//    }


    /**
     * ------------------------------------------------------------------------------------------------------------------
     * ------------------------------------------------     生产者代码     ------------------------------------------------
     * ------------------------------------------------------------------------------------------------------------------
     */

//    /**
//     * 发送用户信息更新延迟消息
//     *
//     * @param userId 用户ID
//     * @param delayMillis 延迟时间（毫秒）
//     */
//    public void sendUserDelayMessage(String userId, long delayMillis) {
//        // 构建消息内容
//        Map<String, Object> data = new HashMap<>();
//        data.put("userId", userId);
//        data.put("sendTime", System.currentTimeMillis());
//        data.put("expectedProcessTime", System.currentTimeMillis() + delayMillis);
//
//        // 发送消息到延时队列
//        rabbitTemplate.convertAndSend(
//                UserConstants.USER_DELAY_EXCHANGE_NAME, // 交换机名称
//                UserConstants.USER_DELAY_ROUTING_KEY,    // 路由键
//                data,
//                message -> {
//                    // 设置延迟时间（毫秒）
//                    message.getMessageProperties().setDelay((int) delayMillis);
//
//                    // 设置消息持久化（确保重启不丢失）
//                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
//
//                    return message;
//                }
//        );
//    }
}
