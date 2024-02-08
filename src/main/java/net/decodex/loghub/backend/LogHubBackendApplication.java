package net.decodex.loghub.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@ConditionalOnProperty(name= "scheduler.enabled", matchIfMissing = true)
@SpringBootApplication
public class LogHubBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogHubBackendApplication.class, args);
	}

}
