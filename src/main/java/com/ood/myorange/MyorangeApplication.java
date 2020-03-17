package com.ood.myorange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.HashSet;

@SpringBootApplication
@EnableTransactionManagement
@PropertySource("classpath:application.yml")
public class MyorangeApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		ArrayList<Integer> array = new ArrayList<>(new HashSet<>());

		SpringApplication.run(MyorangeApplication.class, args);
	}
}
