package com.ood.myorange.controller;

import com.ood.myorange.config.sys.MailConfig;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.service.MailSenderService;
import com.ood.myorange.util.NamingUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Chen on 4/17/20.
 */
@RestController
@RequestMapping("/admin")
public class SysConfigController {

    @Autowired
    MailSenderService mailSenderService;

    @GetMapping("/mail")
    public MailConfig getMailConfig(){
        MailConfig mailConfig = mailSenderService.getMailConfig();
        return mailConfig == null ? new MailConfig() : mailConfig;
    }

    @PostMapping("/mail")
    public MailConfig updateMailConfig(@RequestBody MailConfig mailConfig){
        checkMailConfig(mailConfig);
        mailSenderService.updateMailConfig(mailConfig);
        return mailSenderService.getMailConfig();
    }

    @PostMapping("/mail/test")
    public void testSendingEmail(@RequestParam("addr") String address) {
        if (!NamingUtil.validEmailAddress(address)) {
            throw new InvalidRequestException("invalid target email address");
        }
        mailSenderService.sendTestMail(address);
    }

    private void checkMailConfig(MailConfig mailConfig) {
        if (mailConfig == null
                || StringUtils.isBlank(mailConfig.getHost())
                || StringUtils.isBlank(mailConfig.getPassword())
                || !NamingUtil.validEmailAddress(mailConfig.getMailAddress())) {
            throw new InvalidRequestException("invalid params for mail configuration.");
        }
        if(StringUtils.isBlank(mailConfig.getSender())){
            mailConfig.setSender(mailConfig.getMailAddress());
        }
    }


}
