package com.ood.myorange.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by Chen on 2/21/20.
 *
 * this just an example for how to load configuration into an object from yml file
 */
@Data
@ToString
@ConfigurationProperties("person")
@PropertySource("classpath:/config/person.yml")
@Component
public class Person {
    private String familyName;
    private String address;
    private Integer age;
    private boolean hasDog;
}
