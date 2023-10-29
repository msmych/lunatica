package uk.matvey.lunatica;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class AppConfig {

    @Bean
    public Firestore firestore(@Value("${projectId}") String projectId) throws IOException {
        GoogleCredentials creds = GoogleCredentials.getApplicationDefault();
        FirebaseOptions opts = FirebaseOptions.builder().setCredentials(creds).setProjectId(projectId).build();
        FirebaseApp.initializeApp(opts);
        return FirestoreClient.getFirestore();
    }
}
