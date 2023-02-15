package me.prince.sqsdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.ListQueuesResponse;

@SpringBootApplication
public class SqsDemoApplication {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(SqsDemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
			log.info("started");
			SqsClient sqsClient = SqsClient.builder().region(Region.AP_SOUTHEAST_1).build();
			ListQueuesResponse queues = sqsClient.listQueues();
			queues.queueUrls().forEach(s -> {
				log.info("queues: " + s);
			});

		};
	}

}



