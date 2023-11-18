package uk.matvey.lunatica.app

import uk.matvey.lunatica.account.AccountService
import uk.matvey.lunatica.complaint.ComplaintService
import uk.matvey.lunatica.message.MessageService

class Services(repos: Repos) {

    val accountService = AccountService(repos.accountRepo)
    val complaintService = ComplaintService(repos.complaintRepo)
    val messageService = MessageService(repos.messageRepo)
}
