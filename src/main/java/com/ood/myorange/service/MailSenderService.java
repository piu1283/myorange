package com.ood.myorange.service;

import com.ood.myorange.config.sys.MailConfig;

/**
 * Created by Chen on 4/16/20.
 */
public interface MailSenderService {
    /**
     * change mail config, can be used for add / delete / update
     * @param config
     */
    void updateMailConfig(MailConfig config);

    /**
     * get mail Config Class
     * @return
     */
    MailConfig getMailConfig();

    /**
     * send user mail for user register
     * @param toAddress
     */
    void sendAddUserMail(String toAddress, String password);

    /**
     * send test mail to target address
     * for testing the mail configuration
     *
     * @param toAddress
     */
    void sendTestMail(String toAddress);
}
