package com.github.devlaq.kons.argument.impl

import com.github.devlaq.kons.KonsCompleter
import com.github.devlaq.kons.error.KonsArgumentFilterError
import com.github.devlaq.kons.argument.KonsArgument
import com.github.devlaq.kons.argument.KonsArgumentBuilder
import com.github.devlaq.kons.argument.KonsArguments
import com.github.devlaq.kons.context.KonsCallContext


class KonsNumberArgument(
    filter: (Number) -> Boolean,
    completerOverride: KonsCompleter? = null
) : KonsArgument<Number>(filter, completerOverride) {

    override val typeString = "Number"

    override lateinit var parsed: Number

    override fun parse(index: Int, input: String, context: KonsCallContext) {
        val number = input.toDouble()

        if(!filter(number)) throw KonsArgumentFilterError(index, input, "Number")

        parsed = number
    }

    override fun complete(index: Int, context: KonsCallContext): List<String> {
        return emptyList()
    }

}

class KonsNumberArgumentBuilder: KonsArgumentBuilder<KonsNumberArgument>() {
    private var filter: (Number) -> Boolean = { true }
    private var completer: KonsCompleter? = null

    fun filter(filter: (Number) -> Boolean) {
        this.filter = filter
        Int
    }

    fun complete(body: KonsCompleter) {
        this.completer = body
    }

    override fun build(): KonsNumberArgument {
        return KonsNumberArgument(filter, completer)
    }
}

fun KonsArguments.number(body: KonsNumberArgumentBuilder.() -> Unit): KonsNumberArgument {
    val builder = KonsNumberArgumentBuilder()

    builder.apply(body)

    val argument = builder.build()

    args.add(argument)

    return argument
}
