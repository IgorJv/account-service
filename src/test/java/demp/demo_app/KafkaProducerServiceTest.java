package demp.demo_app;

import demp.demo_app.service.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KafkaProducerServiceTest {
    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;
    @InjectMocks
    private KafkaProducerService producerService;

    @Test
    void sendAccountEvent_ShouldSendToKafkaWithCorrectTopicAndKey() {
        AccountEventDto event = new AccountEventDto(1L, 150.0, "DEPOSIT_COMPLETE");
        SendResult<String, Object> sendResult = mock(SendResult.class);
        when(kafkaTemplate.send(anyString(), anyString(), any()))
                .thenReturn(CompletableFuture.completedFuture(sendResult));
        producerService.sendAccountEvent(event);
        verify(kafkaTemplate).send("account-events", "1", event);
    }
}
