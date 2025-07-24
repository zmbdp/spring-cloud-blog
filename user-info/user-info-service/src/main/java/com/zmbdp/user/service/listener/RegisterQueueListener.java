package com.zmbdp.user.service.listener;

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
public class RegisterQueueListener {
//    /**
//     * 在 rabbitMQ 中监听注册队列
//     *
//     * @param message 消息
//     * @param channel 通道
//     * @throws IOException 抛出
//     */
//    @RabbitListener(queues = RabbitMqConstants.REGISTER_QUEUE_NAME)
//    public void handleRegisterQueue(Message message, Channel channel) throws IOException {
//        long deliveryTag = message.getMessageProperties().getDeliveryTag(); // 消息的标识，在队列中唯一, 就是 id
//        // 确认收到消息
//        try {
//            String body = new String(message.getBody());
//            log.info("收到用户信息：{}", body);
//            // 然后发送邮件
//            channel.basicAck(deliveryTag, true);
//        } catch (Exception e) {
//            log.error("邮件发送失败：{}", e.getMessage());
//            //                            是否批量确认？是否重新发送？
//            channel.basicNack(deliveryTag, true, true);
//        }
//    }

    // 可以直接在这里声明绑定关系

    /**
     * 在 rabbitMQ 中监听注册队列
     *
     * @param message 消息
     * @param channel 通道
     * @throws IOException 抛出
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMqConstants.REGISTER_QUEUE_NAME, durable = "true"),
            exchange = @Exchange(value = RabbitMqConstants.USER_EXCHANGE_NAME, type = ExchangeTypes.DIRECT),
            key = "register.queue"
    ))
    public void handleRegisterQueue(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag(); // 消息的标识，在队列中唯一, 就是 id
        // 确认收到消息
        try {
            String body = new String(message.getBody());
            log.info("收到用户信息：{}", body);
            // 然后发送邮件
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            log.error("邮件发送失败：{}", e.getMessage());
            //                            是否批量确认？是否重新发送？
            channel.basicNack(deliveryTag, true, true);
        }
    }

    /**
     * 在 rabbitMQ 中监听更新用户队列
     * @param message 消息
     * @param channel 通道
     * @throws IOException 抛出
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMqConstants.UPDATE_USER_QUEUE_NAME, durable = "true"),
            exchange = @Exchange(value = RabbitMqConstants.USER_EXCHANGE_NAME, type = ExchangeTypes.DIRECT),
            key = "updateUser.queue"
    ))
    public void handleUpdateUserQueue(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag(); // 消息的标识，在队列中唯一, 就是 id
        // 确认收到消息
        try {
            String body = new String(message.getBody());
            log.info("收到用户信息消息：{}", body);
            // 然后发送邮件
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            log.error("接收更新用户信息消息失败：{}", e.getMessage());
            channel.basicNack(deliveryTag, true, true);
        }
    }
}
