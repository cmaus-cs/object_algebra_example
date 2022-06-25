package example.service

import example.model.CreditScore
import example.model.User

fun interface CreditScoreService {
    fun creditScore(user: User): CreditScore
}
