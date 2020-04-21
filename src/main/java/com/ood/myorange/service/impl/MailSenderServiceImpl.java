package com.ood.myorange.service.impl;

import com.ood.myorange.config.sys.MailConfig;
import com.ood.myorange.exception.ResourceNotFoundException;
import com.ood.myorange.service.MailSenderService;
import com.ood.myorange.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * Created by Chen on 4/15/20.
 */

@Component
@Slf4j
public class MailSenderServiceImpl extends SysConfigService implements MailSenderService {
    private static JavaMailSenderImpl sender = new JavaMailSenderImpl();


    @PostConstruct
    public void initSender () {
        log.info("Start init EMail config.....");
        MailConfig config = getSysConfig(MailConfig.class);
        if (config == null) {
            return;
        }
        sender.setDefaultEncoding("utf-8");
        sender.setHost(config.getHost());
        sender.setPassword(config.getPassword());
        sender.setUsername(config.getMailAddress());
        sender.setPort(config.getPort());
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout","1000");
        p.setProperty("mail.smtp.auth","true");
        p.setProperty("mail.smtp.starttls.enable", "true");
        p.setProperty("mail.smtp.starttls.required", "true");
        sender.setJavaMailProperties(p);
        log.info("EMail config init success.");
    }

    private MailConfig checkConfig() {
        MailConfig config = getMailConfig();
        if (config == null || StringUtils.isBlank(config.getHost()) || StringUtils.isBlank(config.getMailAddress()) || StringUtils.isBlank(config.getPassword())) {
            throw new ResourceNotFoundException("You did not config email. Please config email information.");
        }
        return config;
    }

    @Override
    public void updateMailConfig(MailConfig config) {
        updateSysConfig(config);
        initSender();
    }

    @Override
    public MailConfig getMailConfig() {
        return getSysConfig(MailConfig.class);
    }

    @Override
    public void sendAddUserMail(String toAddress, String password) {
        MailConfig config = checkConfig();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(config.getHost());
        message.setTo(toAddress);
        message.setSubject("Invitation from " + config.getSender() + "");
        message.setText(
                "Hi there, \n" +
                        "This is an invitation email from My Orange Net Disk. \n" +
                        "You have been invited to join " + config.getSender() + "'s net disk. \n" +
                        "You can use your email and password : [" + password + "] to login in. \n"
        );
        sender.send(message);
    }

    @Override
    public void sendTestMail(String toAddress) {
        MailConfig config = checkConfig();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(config.getHost());
        message.setTo(toAddress);
        message.setSubject("This is a test mail from MyOrange");
        message.setText("This is a test email from MyOrange\n" +
                "If you receive this email, it means that the configuration of your email account of MyOrange system is worked.");
        sender.send(message);
    }
}
