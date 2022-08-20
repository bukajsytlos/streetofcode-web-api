package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.request.AddEmailToNewsletterRequest
import sk.streetofcode.webapi.api.request.SendFeedbackEmailRequest

interface EmailService {
    fun sendFeedbackEmail(userId: String? = null, request: SendFeedbackEmailRequest)
    fun sendDiscordInvitation(email: String)
    fun addToNewsletter(userId: String? = null, request: AddEmailToNewsletterRequest)
}