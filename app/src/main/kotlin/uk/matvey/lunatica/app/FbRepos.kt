package uk.matvey.lunatica.app

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext
import uk.matvey.lunatica.complaints.ComplaintFbRepo
import uk.matvey.lunatica.complaints.ComplaintRepo
import uk.matvey.lunatica.complaints.messages.MessageFbRepo
import uk.matvey.lunatica.complaints.messages.MessageRepo

@OptIn(DelicateCoroutinesApi::class)
class FbRepos(projectId: String) {

    val db: Firestore

    val complaintRepo: ComplaintRepo
    val messageRepo: MessageRepo

    init {
        val creds = GoogleCredentials.getApplicationDefault()
        val opts = FirebaseOptions.builder().setCredentials(creds).setProjectId(projectId).build()
        FirebaseApp.initializeApp(opts)

        db = FirestoreClient.getFirestore()
        val dispatcher = newFixedThreadPoolContext(4, "fb-data-source")
        complaintRepo = ComplaintFbRepo(db, dispatcher)
        messageRepo = MessageFbRepo(db, dispatcher)
    }
}
