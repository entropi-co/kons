package kr.entropi.minecraft.kons.argument.impl

import kr.entropi.minecraft.kons.KonsCompleter
import kr.entropi.minecraft.kons.argument.KonsArgument
import kr.entropi.minecraft.kons.argument.KonsArgumentBuilder
import kr.entropi.minecraft.kons.argument.KonsArguments
import kr.entropi.minecraft.kons.context.KonsCallContext
import kr.entropi.minecraft.kons.error.KonsArgumentFilterError
import kotlin.properties.Delegates

class KonsIntegerArgument(
    filter: (Int) -> Boolean,
    completerOverride: KonsCompleter? = null
) : KonsArgument<Int>(filter, completerOverride) {

    override val typeString = "Int"

    override var parsed by Delegates.notNull<Int>()

    override fun parse(index: Int, input: String, context: KonsCallContext) {
        val number = input.toInt()

        if(!filter(number)) throw KonsArgumentFilterError(index, input, typeString)

        parsed = number
    }

    override fun complete(index: Int, context: KonsCallContext): List<String> {
        return emptyList()
    }

}

class KonsIntegerArgumentBuilder: KonsArgumentBuilder<Int, KonsIntegerArgument>() {
    override fun build(): KonsIntegerArgument {
        return KonsIntegerArgument(filter, completer)
    }
}

fun KonsArguments.integer(body: KonsIntegerArgumentBuilder.() -> Unit): KonsIntegerArgument {
    val builder = KonsIntegerArgumentBuilder()

    builder.apply(body)

    val argument = builder.build()

    args.add(argument)

    return argument
}
