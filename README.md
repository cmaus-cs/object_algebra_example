# Object Algebra example in Kotlin

## Step 1: regular kotlin code

Sensible implementation, works as intended

Domain expert not able to assess how the business rules work

Mixture of domain logic and implementation details:
- What is Result ?
- What is NonEmptyList ?
- What is all this XXXService stuff doing?

## Step2: internal dsl

More readable business code -> see method selectPaymentMethods

Domain expert is probably able to understand this.
Some quirks in the dsl because it is tied to kotlin syntax
 - escaping 
 - function invocations
 - indentation

The dsl interface works at a different level than the service interface
from step 1.

We intentionally restrict our vocabulary to terms from the domain language,
limiting ourselves in expressiveness, as we do not have the full power of the host 
language available.

Still, it is possible to mix "regular" kotlin code with dsl abstraction.

```
`advance payment`.value -> NonEmptyList<PaymentMethod>
```

The abstraction is "leaky", the concrete encodings of our domain concepts are available 
through the abstraction layer of the domain language.

This might not be a problem if the team is fine with it, but it might lead
to some hacky solution where one jumps from the dsl layer to the lower
implementation layer 🤷🏻‍♂️

Let's see how we can raise the level of abstraction one more step to draw a more
clear separation between the domain language layer and the implementation.

## Step 3: object algebra approach

The algebra (= interface) does impose no restriction on the actual
data type that is used to represent domain constructs.

This allows to interpret the dsl in multiple ways.
E.g. it allows to create a string/html representation from the code
itself
