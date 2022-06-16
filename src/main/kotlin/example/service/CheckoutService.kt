package example.service

import arrow.core.NonEmptyList
import example.model.PaymentMethod
import example.model.User

fun interface CheckoutService {
    fun selectPaymentMethods(user: User): NonEmptyList<PaymentMethod>
}
