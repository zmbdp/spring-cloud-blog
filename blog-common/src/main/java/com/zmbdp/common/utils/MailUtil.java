package com.zmbdp.common.utils;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Optional;

public class MailUtil {

    private JavaMailSender javaMailSender;

    public MailUtil(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String senderEmail, String recipientEmail, String senderName, String subject, String content) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);

        // 设置邮件内容，需要根据实际情况修改
        // 如果说发件人是空，那就直接默认的
        senderEmail = Optional.ofNullable(senderEmail).orElse("zmbdp@qq.com");
        senderName = Optional.ofNullable(senderName).orElse("码上博客平台");
        helper.setFrom(senderEmail, senderName);  // 发件人
        helper.setTo(recipientEmail);  // 收件人
        subject = Optional.ofNullable(subject).orElse("小博的博客平台");
        helper.setSubject(subject);  // 邮件主题
        content = Optional.ofNullable(content).orElse("<h1>小博的博客平台</h1>");
        helper.setText(content, true);  // 邮件正文

        // 发送邮件
        javaMailSender.send(mimeMessage);
        System.out.println("邮件发送成功！");
    }
}
