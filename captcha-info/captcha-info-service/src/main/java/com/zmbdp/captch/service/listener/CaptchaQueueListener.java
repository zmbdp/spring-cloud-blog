package com.zmbdp.captch.service.listener;

import com.rabbitmq.client.Channel;
import com.zmbdp.common.constant.RabbitMqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CaptchaQueueListener {
//    /**
//     * 在 rabbitMQ 中监听验证码队列
//     *
//     * @param message 消息
//     * @param channel 通道
//     * @throws IOException 抛出
//     */
//    @RabbitListener(queues = RabbitMqConstants.CAPTCHA_QUEUE_NAME)
//    public void handleCaptchaQueue(Message message, Channel channel) throws IOException {
//        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        try {
//            String body = new String(message.getBody());
//            log.info("收到验证码信息：{}", body);
//            // 发送验证码
//            channel.basicAck(deliveryTag, true);
//        } catch (Exception e) {
//            log.error("验证码发送失败：{}", e.getMessage());
//            channel.basicNack(deliveryTag, true, true);
//        }
//    }

    // 直接在这里绑定交换机和队列
    /**
     * 在 rabbitMQ 中监听验证码队列
     *
     * @param message 消息
     * @param channel 通道
     * @throws IOException 抛出
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMqConstants.CAPTCHA_QUEUE_NAME, durable = "true"), // 绑定队列, durable = "true" 表示持久化队列
            exchange = @Exchange(value = RabbitMqConstants.USER_EXCHANGE_NAME, type = ExchangeTypes.DIRECT) // 绑定交换机，
            ,
            key = "captcha.queue"
    ))
    public void handleCaptchaQueue(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String body = new String(message.getBody());
            log.info("收到验证码信息111：{}", body);
            // 发送验证码
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            log.error("验证码发送失败：{}", e.getMessage());
            channel.basicNack(deliveryTag, true, true);
        }
    }
}
