package com.zmbdp.captch.service.listener;

import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.Channel;
import com.zmbdp.captcha.api.pojo.response.CaptchaQueueResponse;
import com.zmbdp.common.constant.RabbitMqConstants;
import com.zmbdp.common.utils.MailUtil;
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

    // String senderEmail, String senderName, String subject, String content
    @Value("${email.senderEmail}")
    private String senderEmail; // 发件人邮件

    @Value("${email.senderName}")
    private String senderName; // 发件人名字

    @Autowired
    private EmailCaptchaProperties emailCaptchaProperties;

    @Autowired
    private MailUtil mailUtil;

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
            log.info("收到验证码信息：{}", body);
            // 发送验证码
            CaptchaQueueResponse captchaUserInfo = JSON.parseObject(body, CaptchaQueueResponse.class);
            String recipientEmail = captchaUserInfo.getEmail();
            Random random = new Random();
            List<String> subject = emailCaptchaProperties.getSubject();
            List<String> content = emailCaptchaProperties.getContent();
            int index = random.nextInt(subject.size());
            // 替换邮件内容中的 email 占位符
            String processedMessage = content.get(index).replace("{email}", recipientEmail);
            processedMessage = processedMessage.replace("{code}", captchaUserInfo.getCaptcha());
            // 发送邮件
            mailUtil.sendMail(senderEmail, recipientEmail, senderName, subject.get(index), processedMessage);
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            log.error("验证码发送失败：{}", e.getMessage());
            channel.basicNack(deliveryTag, true, true);
        }
    }
}
