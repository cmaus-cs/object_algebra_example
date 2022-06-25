package example.model

import arrow.core.NonEmptyList

@JvmInline
value class PaymentRestriction(val value: NonEmptyList<PaymentMethod>)
