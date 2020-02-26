package com.ood.myorange;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.RoundingMode;

@SpringBootApplication
@EnableTransactionManagement
@PropertySource("classpath:application.yml")
public class MyorangeApplication implements WebMvcConfigurer {

	public static void main(String[] args) { SpringApplication.run(MyorangeApplication.class, args); }

}
