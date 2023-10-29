package uk.matvey.lunatica.account

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import uk.matvey.lunatica.common.CryptoService
import java.util.UUID

@RestController
@RequestMapping("/accounts")
class AccountController(private val accountRepo: AccountRepo, private val cryptoService: CryptoService) {

    @GetMapping("/{id}")
    fun getAccount(@PathVariable id: UUID?): Account {
        return accountRepo.get(id!!)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addAccount(@RequestBody request: AddAccountRequest): UUID {
        val account = Account.account(request.email, cryptoService.sha256(request.pass))
        accountRepo.add(account)
        return account.id
    }

    data class AddAccountRequest(val email: String, val pass: String)
}
