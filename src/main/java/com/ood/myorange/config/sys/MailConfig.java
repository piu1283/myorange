package com.ood.myorange.config.sys;

import lombok.Data;
import lombok.ToString;

/**
 * Created by Chen on 4/14/20.
 */
@Data
@ToString
public class MailConfig {
    private String mailAddress;
    private String host;
    private int port;
    private String password;
    private String sender;
}
