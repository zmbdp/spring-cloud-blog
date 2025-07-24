package com.zmbdp.user.service.listener;

import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.Channel;
import com.zmbdp.common.constant.RabbitMqConstants;
import com.zmbdp.common.utils.MailUtil;
import com.zmbdp.user.api.pojo.response.RegisterQueueResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RefreshScope
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


    // String senderEmail, String senderName, String subject, String content
    @Value("${email.senderEmail}")
    private String senderEmail; // 发件人邮件

    @Value("${email.senderName}")
    private String senderName; // 发件人名字

    @Autowired
    private EmailRegisterProperties emailRegisterProperties;

    @Autowired
    private MailUtil mailUtil;

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
            RegisterQueueResponse registerUserInfo = JSON.parseObject(body, RegisterQueueResponse.class);
            String recipientEmail = registerUserInfo.getEmail();
            Random random = new Random();
            List<String> subject = emailRegisterProperties.getSubject();
            List<String> content = emailRegisterProperties.getContent();
            int index = random.nextInt(subject.size());

            // 替换邮件内容中的 email 占位符
            String processedMessage = content.get(index).replace("{email}", recipientEmail);

            // 发送邮件
            mailUtil.sendMail(senderEmail, recipientEmail, senderName, subject.get(index), processedMessage);

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
