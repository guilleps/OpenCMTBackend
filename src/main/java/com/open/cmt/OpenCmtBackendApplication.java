package com.open.cmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class OpenCmtBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenCmtBackendApplication.class, args);
	}

}
