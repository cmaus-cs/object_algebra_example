package example.model

sealed interface PaymentMethod
object AdvancePayment : PaymentMethod
object CreditCardPayment : PaymentMethod
