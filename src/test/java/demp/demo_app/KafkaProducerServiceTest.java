package demp.demo_app;

import demp.demo_app.service.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class KafkaProducerServiceTest {
    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;
    @InjectMocks
    private KafkaProducerService producerService;

    @Test
    void sendAccountEvent_ShouldSendToKafkaWithCorrectTopicAndKey() {
        AccountEventDto event = new AccountEventDto(1L, 150.0, "DEPOSIT_COMPLETE");
        producerService.sendAccountEvent(event);
        verify(kafkaTemplate).send("account-events", "1", event);
    }
}
