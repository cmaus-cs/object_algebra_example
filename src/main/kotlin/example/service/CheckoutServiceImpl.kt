package example.service

import arrow.core.NonEmptyList
import arrow.core.nel
import arrow.core.nonEmptyListOf
import example.model.*

class CheckoutServiceImpl(
    private val paymentService: PaymentService,
    private val creditScoreService: CreditScoreService
) : CheckoutService {

    private val creditScoreSelector = { user: User ->
        if (creditScoreService.creditScore(user) < CreditScore.GOOD) {
            AdvancePayment.nel()
        } else {
            null
        }
    }

    private val validCreditCardSelector = { user: User ->
        paymentService.creditCardType(user)?.let { nonEmptyListOf(CreditCardPayment, AdvancePayment) }
    }

    override fun selectPaymentMethods(user: User): NonEmptyList<PaymentMethod> =
        creditScoreSelector(user) ?: validCreditCardSelector(user) ?: AdvancePayment.nel()
}


