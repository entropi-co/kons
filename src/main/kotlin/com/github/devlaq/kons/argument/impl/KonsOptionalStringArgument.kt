package com.github.devlaq.kons.argument.impl

import com.github.devlaq.kons.KonsCompleter
import com.github.devlaq.kons.error.KonsArgumentFilterError
import com.github.devlaq.kons.argument.KonsArgumentBuilder
import com.github.devlaq.kons.argument.KonsArguments
import com.github.devlaq.kons.argument.KonsOptionalArgument
import com.github.devlaq.kons.context.KonsCallContext


class KonsOptionalStringArgument(
    filter: (String?) -> Boolean,
    completerOverride: KonsCompleter? = null
) : KonsOptionalArgument<String>(filter, completerOverride) {

    override val typeString = "String"

    override var parsed: String? = null

    override fun parse(index: Int, input: String, context: KonsCallContext) {
        if(!filter(input)) throw KonsArgumentFilterError(index, input, "String")

        parsed = input
    }

    override fun complete(index: Int, context: KonsCallContext): List<String> {
        return emptyList()
    }

}

class KonsOptionalStringArgumentBuilder: KonsArgumentBuilder<KonsOptionalStringArgument>() {
    private var filter: (String?) -> Boolean = { true }
    private var completer: KonsCompleter? = null

    fun filter(filter: (String?) -> Boolean) {
        this.filter = filter
    }

    fun complete(body: KonsCompleter) {
        this.completer = body
    }

    override fun build(): KonsOptionalStringArgument {
        return KonsOptionalStringArgument(filter, completer)
    }
}

fun KonsArguments.optionalString(body: KonsOptionalStringArgumentBuilder.() -> Unit): KonsOptionalStringArgument {
    val builder = KonsOptionalStringArgumentBuilder()

    builder.apply(body)

    val argument = builder.build()

    args.add(argument)

    return argument
}
