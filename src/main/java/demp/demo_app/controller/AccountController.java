package demp.demo_app.controller;

import demp.demo_app.AccountDepositDto;
import demp.demo_app.AccountEventDto;
import demp.demo_app.AccountResult;
import demp.demo_app.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final KafkaProducerService kafkaProducerService;

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Account Service is running successfully!");
    }

    @PostMapping("/deposit")
    public ResponseEntity<AccountResult> deposit(@RequestBody AccountDepositDto accountDeposit) {
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("id", accountDeposit.getAccountId())
                .addValue("amount", accountDeposit.getAmount());

        Double newBalance = jdbcTemplate.queryForObject(
                "SELECT :amount AS balance",
                source,
                Double.class
        );

        AccountEventDto event = new AccountEventDto(accountDeposit.getAccountId(), accountDeposit.getAmount(), "DEPOSIT_COMPLETE");
        kafkaProducerService.sendAccountEvent(event);

        return ResponseEntity.ok(new AccountResult(accountDeposit.getAccountId(), newBalance != null ? newBalance : 0.0, "SUCCESS"));
    }
}
