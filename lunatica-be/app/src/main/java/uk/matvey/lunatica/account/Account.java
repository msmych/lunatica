package uk.matvey.lunatica.account;

import java.util.Optional;
import java.util.UUID;

public class Account {

    public final UUID id;
    public final String email;
    public final Optional<Long> tgChatId;

    public Account(UUID id, String email, Optional<Long> tgChatId) {
        this.id = id;
        this.email = email;
        this.tgChatId = tgChatId;
    }
}
