package example.algebra

import arrow.core.nel
import example.model.*
import example.service.CreditScoreService
import example.service.PaymentService

typealias TBD = Any?
// TODO: Step 1. Define typealiases to compile the tests
typealias EvalPaymentRule = TBD

typealias EvalRuleHead = TBD

typealias EvalCondition = TBD

typealias EvalUserCreditScore = TBD

typealias EvalPaymentSelection = TBD


//TODO: Step 2. Implementation
class PaymentSelectionAlgebraInterpreter(
    private val paymentService: PaymentService,
    private val creditScoreService: CreditScoreService
) :
    PaymentSelectionAlgebra<CreditScore, EvalCondition, EvalUserCreditScore, PaymentRestriction, EvalPaymentSelection, EvalPaymentRule, EvalRuleHead> {
    override val good = CreditScore.GOOD
    override val average = CreditScore.AVERAGE
    override val bad = CreditScore.BAD

    override val `advance payment`: PaymentRestriction = TODO()
    override val `credit card payment`: PaymentRestriction = TODO()

    override val `credit score`: EvalUserCreditScore = TODO()
    override val `credit card`: EvalCondition = TODO()

    override fun EvalUserCreditScore.`is worse than`(other: CreditScore): EvalCondition = TODO()

    override fun EvalRuleHead.then(restriction: EvalPaymentSelection): EvalPaymentRule = TODO()

    override fun allow(paymentMethods: PaymentRestriction): EvalPaymentSelection = TODO()

    override fun PaymentRestriction.and(other: PaymentRestriction): PaymentRestriction = TODO()

    override fun EvalPaymentRule.otherwise(restriction: EvalPaymentSelection): EvalPaymentSelection = TODO()

    override fun provided(condition: EvalCondition): EvalRuleHead = TODO()

    override fun EvalPaymentRule.or(next: EvalPaymentRule): EvalPaymentRule = TODO()
}


