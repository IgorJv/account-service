package demp.demo_app;

import lombok.*;

@AllArgsConstructor
@ToString
@Getter
@EqualsAndHashCode
public class AccountEventDto {
    private final Long accountId;
    private final Double amount;
    private final String status;
}
