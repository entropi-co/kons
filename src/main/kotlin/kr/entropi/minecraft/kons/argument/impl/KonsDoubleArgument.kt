package kr.entropi.minecraft.kons.argument.impl

import kr.entropi.minecraft.kons.KonsCompleter
import kr.entropi.minecraft.kons.error.KonsArgumentFilterError
import kr.entropi.minecraft.kons.argument.KonsArgument
import kr.entropi.minecraft.kons.argument.KonsArgumentBuilder
import kr.entropi.minecraft.kons.argument.KonsArguments
import kr.entropi.minecraft.kons.context.KonsCallContext
import kotlin.properties.Delegates


class KonsDoubleArgument(
    filter: (Double) -> Boolean,
    completerOverride: KonsCompleter? = null
) : KonsArgument<Double>(filter, completerOverride) {

    override val typeString = "Double"

    override var parsed by Delegates.notNull<Double>()

    override fun parse(index: Int, input: String, context: KonsCallContext) {
        val number = input.toDouble()

        if(!filter(number)) throw KonsArgumentFilterError(index, input, typeString)

        parsed = number
    }

    override fun complete(index: Int, context: KonsCallContext): List<String> {
        return emptyList()
    }

}

class KonsDoubleArgumentBuilder: KonsArgumentBuilder<Double, KonsDoubleArgument>() {
    override fun build(): KonsDoubleArgument {
        return KonsDoubleArgument(filter, completer)
    }
}

fun KonsArguments.double(body: KonsDoubleArgumentBuilder.() -> Unit): KonsDoubleArgument {
    val builder = KonsDoubleArgumentBuilder()

    builder.apply(body)

    val argument = builder.build()

    args.add(argument)

    return argument
}
