package com.kovic.employee_scheduler;

import com.kovic.employee_scheduler.config.DemoSessionProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(DemoSessionProperties.class)
public class EmployeeSchedulerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EmployeeSchedulerApplication.class, args);
	}
}
