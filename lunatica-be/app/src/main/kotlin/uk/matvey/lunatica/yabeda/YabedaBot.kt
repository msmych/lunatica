package uk.matvey.lunatica.yabeda

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import uk.matvey.lunatica.complaint.ComplaintRepo
import uk.matvey.lunatica.message.MessageRepo

@Component
class YabedaBot(
    private val complaintRepo: ComplaintRepo,
    private val messageRepo: MessageRepo
) {

    @PostConstruct
    fun start() {
    }
}
