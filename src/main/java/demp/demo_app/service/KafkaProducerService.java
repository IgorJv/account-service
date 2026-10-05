package demp.demo_app.service;

import demp.demo_app.AccountEventDto;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "account-events";

    public void sendAccountEvent(AccountEventDto event) {
        kafkaTemplate.send(TOPIC, String.valueOf(event.getAccountId()), event);
    }
}
