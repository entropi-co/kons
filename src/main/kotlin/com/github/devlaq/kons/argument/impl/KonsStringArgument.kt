package com.github.devlaq.kons.argument.impl

import com.github.devlaq.kons.KonsCompleter
import com.github.devlaq.kons.error.KonsArgumentFilterError
import com.github.devlaq.kons.argument.KonsArgument
import com.github.devlaq.kons.argument.KonsArgumentBuilder
import com.github.devlaq.kons.argument.KonsArguments
import com.github.devlaq.kons.context.KonsCallContext


class KonsStringArgument(
    filter: (String) -> Boolean,
    completerOverride: KonsCompleter? = null
) : KonsArgument<String>(filter, completerOverride) {

    override val typeString = "String"

    override lateinit var parsed: String

    override fun parse(index: Int, input: String, context: KonsCallContext) {
        if(!filter(input)) throw KonsArgumentFilterError(index, input, "String")

        parsed = input
    }

    override fun complete(index: Int, context: KonsCallContext): List<String> {
        return emptyList()
    }

}

class KonsStringArgumentBuilder: KonsArgumentBuilder<KonsStringArgument>() {
    private var filter: (String) -> Boolean = { true }
    private var completer: KonsCompleter? = null

    fun filter(filter: (String) -> Boolean) {
        this.filter = filter
    }

    fun complete(body: KonsCompleter) {
        this.completer = body
    }

    override fun build(): KonsStringArgument {
        return KonsStringArgument(filter, completer)
    }
}

fun KonsArguments.string(body: KonsStringArgumentBuilder.() -> Unit): KonsStringArgument {
    val builder = KonsStringArgumentBuilder()

    builder.apply(body)

    val argument = builder.build()

    args.add(argument)

    return argument
}
