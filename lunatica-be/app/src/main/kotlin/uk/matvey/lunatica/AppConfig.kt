package uk.matvey.lunatica

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import com.pengrad.telegrambot.TelegramBot
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import uk.matvey.lunatica.yabeda.YabedaBot

@Configuration
open class AppConfig {
    @Bean
    open fun firestore(@Value("\${projectId}") projectId: String): Firestore {
        val creds = GoogleCredentials.newBuilder()
            .build()
        val opts = FirebaseOptions.builder().setCredentials(creds).setProjectId(projectId).build()
        FirebaseApp.initializeApp(opts)
        return FirestoreClient.getFirestore()
    }

    @Bean
    open fun telegramBotHolder(@Value("\${yabeda.bot-token}") yabedaBotToken: String): YabedaBot.TelegramBotHolder {
        return YabedaBot.TelegramBotHolder(yabedaBotToken.takeIf { it != "NONE" }?.let { TelegramBot(it) })
    }
}
