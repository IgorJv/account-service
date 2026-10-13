package demp.demo_app.service;

import demp.demo_app.AccountEventDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@AllArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "account-events";

    public void sendAccountEvent(AccountEventDto event) {
        CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(TOPIC, String.valueOf(event.accountId()), event);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Sent message=[{}] with offset=[{}]", event, result.getRecordMetadata().offset());
            } else {
                log.error("Unable to send message=[{}] due to : {}", event, ex.getMessage());
            }
        });
    }
}
