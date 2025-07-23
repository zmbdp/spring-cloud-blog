package com.zmbdp.user.service.listener;

import com.zmbdp.common.constant.UserConstants;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class RegisterQueueListener {
    /**
     * 在 rabbitMQ 中监听邮件队列
     * @param message 消息
     * @param channel 通道
     * @throws IOException 抛出
     */
    @RabbitListener(queues = UserConstants.REGISTER_QUEUE_NAME)
    public void handleRegisterQueue(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag(); // 消息的标识，在队列中唯一, 就是 id
        // 确认收到消息
        try {
            String body = Arrays.toString(message.getBody());
            log.info("收到用户信息：{}", body);
            // 然后发送邮件
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            log.error("邮件发送失败：{}", e.getMessage());
            //                            是否批量确认？是否重新发送？
            channel.basicNack(deliveryTag, true, true);
        }
    }
}
