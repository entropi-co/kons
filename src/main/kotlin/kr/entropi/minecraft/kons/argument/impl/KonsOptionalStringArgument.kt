package kr.entropi.minecraft.kons.argument.impl

import kr.entropi.minecraft.kons.KonsCompleter
import kr.entropi.minecraft.kons.error.KonsArgumentFilterError
import kr.entropi.minecraft.kons.argument.KonsArgumentBuilder
import kr.entropi.minecraft.kons.argument.KonsArguments
import kr.entropi.minecraft.kons.argument.KonsOptionalArgument
import kr.entropi.minecraft.kons.context.KonsCallContext


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

class KonsOptionalStringArgumentBuilder: KonsArgumentBuilder<String?, KonsOptionalStringArgument>() {
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
