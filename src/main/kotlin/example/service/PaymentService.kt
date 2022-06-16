package example.service

import example.model.CreditCardType
import example.model.User

fun interface PaymentService {
    fun creditCardType(user: User): CreditCardType?
}
