package example.algebra

import arrow.core.nel
import example.model.*
import example.service.CreditScoreService
import example.service.PaymentService

class PaymentSelectionAlgebraInterpreter(
    private val paymentService: PaymentService,
    private val creditScoreService: CreditScoreService
) : PaymentSelectionAlgebra<
        CreditScore,
        PaymentSelectionAlgebraInterpreter.Condition,
        PaymentSelectionAlgebraInterpreter.UserCreditScore,
        PaymentRestriction,
        PaymentSelectionAlgebraInterpreter.PaymentSelection,
        PaymentSelectionAlgebraInterpreter.PaymentRule,
        PaymentSelectionAlgebraInterpreter.RuleHead> {
    override val good = CreditScore.GOOD
    override val average = CreditScore.AVERAGE
    override val bad = CreditScore.BAD

    override val `advance payment` = PaymentRestriction(AdvancePayment.nel())
    override val `credit card payment` = PaymentRestriction(CreditCardPayment.nel())

    override val `credit score` = UserCreditScore(creditScoreService::creditScore)
    override val `credit card` = Condition { paymentService.creditCardType(it) != null }

    override fun UserCreditScore.`is worse than`(other: CreditScore) = Condition { this(it) < other }

    override fun RuleHead.then(restriction: PaymentSelection) = PaymentRule {
        if (this(it)) restriction(it) else null
    }

    override fun allow(paymentMethods: PaymentRestriction) = PaymentSelection { paymentMethods }

    override fun PaymentRestriction.and(other: PaymentRestriction) = PaymentRestriction(this.value + other.value)

    override fun PaymentRule.otherwise(restriction: PaymentSelection) = PaymentSelection {
        this(it) ?: restriction(it)
    }

    override fun provided(condition: Condition): RuleHead = RuleHead { condition(it) }

    override fun PaymentRule.or(next: PaymentRule): PaymentRule = PaymentRule {
        this(it) ?: next(it)
    }

    fun interface PaymentSelection {
        operator fun invoke(user: User): PaymentRestriction
    }

    fun interface UserCreditScore {
        operator fun invoke(user: User): CreditScore
    }

    fun interface Condition {
        operator fun invoke(user: User): Boolean
    }

    fun interface RuleHead {
        operator fun invoke(user: User): Boolean
    }

    fun interface PaymentRule {
        operator fun invoke(user: User): PaymentRestriction?
    }
}


