fun main(args: Array<String>) {

    println(e1(EvalExp()).eval())
}

//this is somewhat ugly, try to do syntactic sugaring here
internal fun <T> e1(f: ExpAlg<T>): T {
    return f.add(
        f.lit(1),
        f.add(
            f.lit(2),
            f.lit(3)
        )
    )
}

internal interface ExpAlg<T> {
    fun lit(n: Int): T
    fun add(x: T, y: T): T
}

internal interface Eval {
    fun eval(): Int
}

internal class EvalExp : ExpAlg<Eval> {
    override fun lit(n: Int): Eval = object : Eval {
        override fun eval(): Int = n
    }

    override fun add(x: Eval, y: Eval): Eval = object : Eval {
        override fun eval(): Int = x.eval() + y.eval()
    }
}
