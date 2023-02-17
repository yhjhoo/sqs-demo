package me.prince.sqsdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@RestController
class MessageController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@GetMapping("/send")
	String sendMessage() {
		try {
			SqsClient sqsClient = SqsClient.builder().region(Region.AP_SOUTHEAST_1).build();
			Map<String, MessageAttributeValue> attributes = new HashMap<>();
			attributes.put("a1", MessageAttributeValue.builder().dataType("String").stringValue("ff").build());
			SendMessageRequest request = SendMessageRequest.builder()
					.queueUrl("https://sqs.ap-southeast-1.amazonaws.com/924307141432/hj_sqs.fifo")
					.messageGroupId("eks")
					.messageAttributes(attributes)
					.build();
			sqsClient.sendMessage(request);
		} catch (Exception e) {
			log.error("error", e);
		}

		return "success";
	}

	@GetMapping("/receive")
	String receiveMessage() {
		try {
			SqsClient sqsClient = SqsClient.builder().region(Region.AP_SOUTHEAST_1).build();

			ReceiveMessageRequest request = ReceiveMessageRequest.builder()
					.queueUrl("https://sqs.ap-southeast-1.amazonaws.com/924307141432/hj_sqs.fifo")
					.maxNumberOfMessages(10)
					.attributeNames(QueueAttributeName.ALL)
					.waitTimeSeconds(20)
					.build();
			List<Message> messages = sqsClient.receiveMessage(request).messages();
			for (Message message : messages) {
				log.info(message.messageId());
				log.info(message.body());
			}

		} catch (Exception e) {
			log.error("error", e);
		}

		return "success";
	}
}



