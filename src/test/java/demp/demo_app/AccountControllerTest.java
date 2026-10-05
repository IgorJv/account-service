package demp.demo_app;

import com.fasterxml.jackson.databind.ObjectMapper;
import demp.demo_app.controller.AccountController;
import demp.demo_app.service.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private NamedParameterJdbcTemplate jdbcTemplate;
    @MockBean
    private KafkaProducerService producerService;

    @Test
    void healthCheck_ShouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account Service is running successfully!"));

    }

    @Test
    void deposit_ShouldProcessDepositAndReturnResult() throws Exception {
        AccountDepositDto requestDto = new AccountDepositDto();
        requestDto.setAccountId(10L);
        requestDto.setAmount(250.0);

        when(jdbcTemplate.queryForObject(
                any(String.class),
                any(MapSqlParameterSource.class),
                eq(Double.class)
        )).thenReturn(350.0);

        mockMvc.perform(post("/api/v1/accounts/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(10L))
                .andExpect(jsonPath("$.balance").value(350.0))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }
}