package pl.cobrick.account.balance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AccountBalanceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountBalanceServiceApplication.class, args);
    }
}