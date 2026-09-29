package com.kovic.employee_scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmployeeSchedulerApplication {

	public static void main(String[] args) {

		SpringApplication.run(EmployeeSchedulerApplication.class, args);
	}

}
