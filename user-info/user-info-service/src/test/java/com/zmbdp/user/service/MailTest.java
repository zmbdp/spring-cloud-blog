package com.zmbdp.user.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@SpringBootTest
public class MailTest {

    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    public void testSendMail() throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);

        // 设置邮件内容，需要根据实际情况修改
        helper.setFrom("zmbdp@qq.com", "码上博客平台");  // 发件人
        helper.setTo("javafh@163.com");  // 收件人
        helper.setSubject("测试邮件");  // 邮件主题
        helper.setText("<h2>🎉 欢迎加入码上博客平台！</h2>" +
                "<p>亲爱的小伙伴，小博已经收到你的注册信息啦~</p>" +
                "<p>你的账号已经成功激活，现在可以开始创作之旅了哦！(◕‿◕✿)</p>" +
                "<p>快登录 <a href='https://blog.zmbdp.com'>码上博客</a> 发布你的第一篇博客吧~</p>" +
                "<p>遇到问题随时找小博，我一直都在呢！(๑˃ᴗ˂)ﻭ</p>" +
                "<p style='color:#888;margin-top:20px;'>—— 你的贴心小伙伴 小博</p>", true);  // 邮件正文

        // 发送邮件
        javaMailSender.send(mimeMessage);
        System.out.println("邮件发送成功！");
    }
}
