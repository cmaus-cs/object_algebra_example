//package example.algebra
//
//import com.github.h0tk3y.betterParse.combinators.leftAssociative
//import com.github.h0tk3y.betterParse.combinators.or
//import com.github.h0tk3y.betterParse.combinators.use
//import com.github.h0tk3y.betterParse.lexer.DefaultTokenizer
//import com.github.h0tk3y.betterParse.lexer.literalToken
//import com.github.h0tk3y.betterParse.lexer.regexToken
//import com.github.h0tk3y.betterParse.parser.Parser
//
//internal inline fun <CreditScore, PaymentMethods, Condition, UserCreditScore, PaymentRestriction, PaymentSelection, PaymentRule, RuleHead> PaymentSelectionAlgebra<CreditScore, PaymentMethods, Condition, UserCreditScore, PaymentRestriction, PaymentSelection, PaymentRule, RuleHead>.createParser(): Pair<DefaultTokenizer, Parser<T>> {
//
//    """
//        provided credit score is worse than good then allow advance payment
//        or
//        provided credit card then allow credit card payment and advance payment
//        otherwise
//        allow advance payment
//    """
//
//    val goodToken = literalToken("good")
//    val advancePaymentToken = literalToken("advance payment")
//    val creditCardPaymentToken = literalToken("credit card payment")
//    val userCreditScoreToken = literalToken("credit score")
//    val creditCardToken = literalToken("credit card")
//    val providedToken = literalToken("provided")
//    val isWorseThanToken = literalToken("is worse than")
//    val thenToken = literalToken("then")
//    val allowToken = literalToken("allow")
//    val orToken = literalToken("or")
//    val otherwiseToken = literalToken("otherwise")
//
//    val ws = regexToken("\\s+", ignore = true)
//    val tokenizer = DefaultTokenizer(
//        listOf(
//            goodToken,
//            advancePaymentToken,
//            creditCardPaymentToken,
//            userCreditScoreToken,
//            creditCardToken,
//            providedToken,
//            isWorseThanToken,
//            thenToken,
//            allowToken,
//            orToken,
//            otherwiseToken,
//            ws
//        )
//    )
//
//    val creditScoreCondition = leftAssociative(userCreditScoreToken, isWorseThanToken, goodToken)
//    val condition = leftAssociative(creditCardToken or )
//
//    lateinit var termFactory: () -> Parser<T>
//    val multExprFactory = { leftAssociative(parser(termFactory), mult) { a, _, b -> a * b } }
//    val sumExprFactory = { leftAssociative(parser(multExprFactory), plus) { a, _, b -> a + b } }
//    termFactory = {
//        (int use { lit(text.toInt()) }) or (-openp * parser(sumExprFactory) * -closep)
//    }
//
//    return tokenizer to sumExprFactory()
//}
