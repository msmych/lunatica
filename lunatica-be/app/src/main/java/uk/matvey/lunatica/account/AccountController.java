package uk.matvey.lunatica.account;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountRepo accountRepo;

    public AccountController(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID addAccount(@RequestBody AddAccountRequest request) {
        Account account = new Account(randomUUID(), request.email, Optional.empty());
        accountRepo.add(account);
        return account.id;
    }

    public static class AddAccountRequest {
        public String email;
    }
}
