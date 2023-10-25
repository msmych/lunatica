package uk.matvey.lunatica.app

import com.neovisionaries.i18n.CountryCode
import kotlinx.html.ButtonType
import kotlinx.html.HTML
import kotlinx.html.InputType
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.option
import kotlinx.html.p
import kotlinx.html.script
import kotlinx.html.select
import kotlinx.html.textArea
import kotlinx.html.title
import uk.matvey.lunatica.complaints.Complaint
import java.time.LocalDate

fun HTML.index() {
    head {
        title {
            +"Lunatica"
        }
        script(src = "https://unpkg.com/htmx.org@1.9.6") {
            integrity = "sha384-FhXw7b6AlE/jyjlZH5iHa/tTe9EpJ1Y55RjcgPbjeWMskSxZt1v9qkxLJWNJaGni"
            attributes["crossorigin"] = "anonymous"
        }
    }
    body {
        h1 {
            +"Ciao lunatica"
        }
        button {
            attributes["hx-get"] = "/api/complaints"
            attributes["hx-target"] = "#complaintsList"
            +"Load complaints"
        }
        div {
            id = "complaintsList"
        }
        form {
            attributes["hx-post"] = "/api/complaints"
            attributes["hx-target"] = "#complaintIdButton"
            p {
                select {
                    id = "problemCountry"
                    name = "problemCountry"
                    CountryCode.entries.forEach { country ->
                        option {
                            value = country.name
                            selected = country == CountryCode.GB
                            +country.getName()
                        }
                    }
                }
            }
            p {
                input(type = InputType.date) {
                    id = "problemDate"
                    name = "problemDate"
                    value = LocalDate.now().toString()
                }
            }
            p {
                Complaint.Type.entries.forEach { type ->
                    input(type = InputType.radio) {
                        id = type.name.lowercase()
                        name = "type"
                        value = type.name
                        checked = type == Complaint.Type.BANK
                    }
                    label {
                        attributes["for"] = type.name.lowercase()
                        +"${type.name.take(1)}${type.name.substring(1).lowercase()}"
                    }
                }
            }
            p {
                textArea {
                    id = "content"
                    name = "content"
                }
            }
            p {
                input(type = InputType.email) {
                    id = "email"
                    name = "email"
                }
            }
            button(type = ButtonType.submit) {
                +"Send"
            }
        }
        div {
            id = "complaintIdButton"
        }
    }
}
