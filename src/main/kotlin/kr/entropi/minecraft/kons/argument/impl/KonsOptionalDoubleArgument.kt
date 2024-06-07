package kr.entropi.minecraft.kons.argument.impl

import kr.entropi.minecraft.kons.KonsCompleter
import kr.entropi.minecraft.kons.argument.KonsArgumentBuilder
import kr.entropi.minecraft.kons.argument.KonsArguments
import kr.entropi.minecraft.kons.argument.KonsOptionalArgument
import kr.entropi.minecraft.kons.context.KonsCallContext
import kr.entropi.minecraft.kons.error.KonsArgumentFilterError

class KonsOptionalDoubleArgument(
    filter: (Double?) -> Boolean,
    completerOverride: KonsCompleter? = null
) : KonsOptionalArgument<Double>(filter, completerOverride) {

    override val typeString = "Number"

    override var parsed: Double? = null

    override fun parse(index: Int, input: String, context: KonsCallContext) {
        val number = input.toDouble()

        if(!filter(number)) throw KonsArgumentFilterError(index, input, typeString)

        parsed = number
    }

    override fun complete(index: Int, context: KonsCallContext): List<String> {
        return emptyList()
    }

}

class KonsOptionalNumberArgumentBuilder: KonsArgumentBuilder<Double?, KonsOptionalDoubleArgument>() {
    override fun build(): KonsOptionalDoubleArgument {
        return KonsOptionalDoubleArgument(filter, completer)
    }
}

fun KonsArguments.optionalNumber(body: KonsOptionalNumberArgumentBuilder.() -> Unit): KonsOptionalDoubleArgument {
    val builder = KonsOptionalNumberArgumentBuilder()

    builder.apply(body)

    val argument = builder.build()

    args.add(argument)

    return argument
}