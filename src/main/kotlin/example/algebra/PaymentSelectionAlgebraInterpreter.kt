package example.algebra

import arrow.core.nel
import example.model.*
import example.service.CreditScoreService
import example.service.PaymentService

typealias EvalPaymentRule = (User) -> PaymentRestriction?

typealias EvalRuleHead = (User) -> Boolean

typealias EvalCondition = (User) -> Boolean

typealias EvalUserCreditScore = (User) -> CreditScore

typealias EvalPaymentSelection = (User) -> PaymentRestriction


class PaymentSelectionAlgebraInterpreter(
    private val paymentService: PaymentService,
    private val creditScoreService: CreditScoreService
) :
    PaymentSelectionAlgebra<CreditScore, EvalCondition, EvalUserCreditScore, PaymentRestriction, EvalPaymentSelection, EvalPaymentRule, EvalRuleHead> {
    override val good = CreditScore.GOOD
    override val average = CreditScore.AVERAGE
    override val bad = CreditScore.BAD

    override val `advance payment` = PaymentRestriction(AdvancePayment.nel())
    override val `credit card payment` = PaymentRestriction(CreditCardPayment.nel())

    override val `credit score` = creditScoreService::creditScore
    override val `credit card` = { user: User -> paymentService.creditCardType(user) != null }

    override fun EvalUserCreditScore.`is worse than`(other: CreditScore) = { it: User -> this(it) < other }

    override fun EvalRuleHead.then(restriction: EvalPaymentSelection) = { it: User ->
        if (this(it)) restriction(it) else null
    }

    override fun allow(paymentMethods: PaymentRestriction) = { _: User -> paymentMethods }

    override fun PaymentRestriction.and(other: PaymentRestriction) = PaymentRestriction(this.value + other.value)

    override fun EvalPaymentRule.otherwise(restriction: EvalPaymentSelection) = { it: User ->
        this(it) ?: restriction(it)
    }

    override fun provided(condition: EvalCondition): EvalRuleHead = condition

    override fun EvalPaymentRule.or(next: EvalPaymentRule): EvalPaymentRule = { it: User ->
        this(it) ?: next(it)
    }
}


