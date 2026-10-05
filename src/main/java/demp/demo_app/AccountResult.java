package demp.demo_app;

public class AccountResult {
    private Long accountId;
    private double newBalance;
    private String status;

    public AccountResult(Long accountId, double newBalance, String status) {
        this.accountId = accountId;
        this.newBalance = newBalance;
        this.status = status;
    }

    public Long getAccountId() { return accountId; }
    public double getNewBalance() { return newBalance; }
    public String getStatus() { return status; }
}
