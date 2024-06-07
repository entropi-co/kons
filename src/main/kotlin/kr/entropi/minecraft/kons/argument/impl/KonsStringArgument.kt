package kr.entropi.minecraft.kons.argument.impl

import kr.entropi.minecraft.kons.KonsCompleter
import kr.entropi.minecraft.kons.error.KonsArgumentFilterError
import kr.entropi.minecraft.kons.argument.KonsArgument
import kr.entropi.minecraft.kons.argument.KonsArgumentBuilder
import kr.entropi.minecraft.kons.argument.KonsArguments
import kr.entropi.minecraft.kons.context.KonsCallContext

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

class KonsStringArgumentBuilder: KonsArgumentBuilder<String, KonsStringArgument>() {
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
