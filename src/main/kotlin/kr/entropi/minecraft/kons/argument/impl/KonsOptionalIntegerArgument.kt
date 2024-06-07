package kr.entropi.minecraft.kons.argument.impl

import kr.entropi.minecraft.kons.KonsCompleter
import kr.entropi.minecraft.kons.argument.KonsArgumentBuilder
import kr.entropi.minecraft.kons.argument.KonsArguments
import kr.entropi.minecraft.kons.argument.KonsOptionalArgument
import kr.entropi.minecraft.kons.context.KonsCallContext
import kr.entropi.minecraft.kons.error.KonsArgumentFilterError

class KonsOptionalIntegerArgument(
    filter: (Int?) -> Boolean,
    completerOverride: KonsCompleter? = null
) : KonsOptionalArgument<Int>(filter, completerOverride) {

    override val typeString = "Integer"

    override var parsed: Int? = null

    override fun parse(index: Int, input: String, context: KonsCallContext) {
        val number = input.toInt()

        if(!filter(number)) throw KonsArgumentFilterError(index, input, typeString)

        parsed = number
    }

    override fun complete(index: Int, context: KonsCallContext): List<String> {
        return emptyList()
    }

}

class KonsOptionalIntegerArgumentBuilder: KonsArgumentBuilder<Int?, KonsOptionalIntegerArgument>() {
    override fun build(): KonsOptionalIntegerArgument {
        return KonsOptionalIntegerArgument(filter, completer)
    }
}

fun KonsArguments.optionalInteger(body: KonsOptionalIntegerArgumentBuilder.() -> Unit): KonsOptionalIntegerArgument {
    val builder = KonsOptionalIntegerArgumentBuilder()

    builder.apply(body)

    val argument = builder.build()

    args.add(argument)

    return argument
}