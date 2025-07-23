package com.zmbdp.captch.service.listener;

import com.rabbitmq.client.Channel;
import com.zmbdp.common.constant.UserConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class CaptchaQueueListener {
    /**
     * 在 rabbitMQ 中监听验证码队列
     * @param message 消息
     * @param channel 通道
     * @throws IOException 抛出
     */
    @RabbitListener(queues = UserConstants.CAPTCHA_QUEUE_NAME)
    public void handleCaptchaQueue(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String body = Arrays.toString(message.getBody());
            log.info("收到验证码信息：{}", body);
            // 发送验证码
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            log.error("验证码发送失败：{}", e.getMessage());
            channel.basicNack(deliveryTag, true, true);
        }
    }
}
