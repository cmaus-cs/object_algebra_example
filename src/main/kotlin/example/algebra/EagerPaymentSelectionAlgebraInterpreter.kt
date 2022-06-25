package example.algebra

import arrow.core.NonEmptyList
import arrow.core.nel
import example.model.*
import example.service.CreditScoreService
import example.service.PaymentService

class EagerPaymentSelectionAlgebraInterpreter(
    paymentService: PaymentService,
    creditScoreService: CreditScoreService,
    user: User
) : PaymentSelectionAlgebra<
        CreditScore,
        Boolean,
        CreditScore,
        NonEmptyList<PaymentMethod>,
        NonEmptyList<PaymentMethod>,
        NonEmptyList<PaymentMethod>?,
        Boolean> {
    override val good = CreditScore.GOOD
    override val average = CreditScore.AVERAGE
    override val bad = CreditScore.BAD
    override val `advance payment` = AdvancePayment.nel()
    override val `credit card payment` = CreditCardPayment.nel()
    override val `credit score` = creditScoreService.creditScore(user)
    override val `credit card` = paymentService.creditCardType(user) != null

    override fun CreditScore.`is worse than`(other: CreditScore): Boolean = this < other

    override fun allow(paymentMethods: NonEmptyList<PaymentMethod>): NonEmptyList<PaymentMethod> = paymentMethods

    override fun NonEmptyList<PaymentMethod>.and(other: NonEmptyList<PaymentMethod>): NonEmptyList<PaymentMethod> =
        this + other

    override fun NonEmptyList<PaymentMethod>?.otherwise(restriction: NonEmptyList<PaymentMethod>): NonEmptyList<PaymentMethod> =
        this ?: restriction

    override fun provided(condition: Boolean): Boolean = condition

    override fun NonEmptyList<PaymentMethod>?.or(next: NonEmptyList<PaymentMethod>?): NonEmptyList<PaymentMethod>? =
        this ?: next

    override fun Boolean.then(restriction: NonEmptyList<PaymentMethod>): NonEmptyList<PaymentMethod>? =
        if (this) restriction else null
}

