package uk.matvey.lunatica.account;

import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AccountRepo {
    private final Firestore db;

    public AccountRepo(Firestore db) {
        this.db = db;
    }

    public void add(Account account) {
        db.collection("accounts").document(account.id.toString()).set(toDoc(account));
    }

    private static Map<String, Object> toDoc(Account account) {
        HashMap<String, Object> doc = new HashMap<>();
        doc.put("email", account.email);
        account.tgChatId.ifPresent(tgChatId -> doc.put("tgChatId", tgChatId));
        return doc;
    }
}
