package kr.entropi.minecraft.kons.argument.impl

import kr.entropi.minecraft.kons.KonsCompleter
import kr.entropi.minecraft.kons.argument.KonsArgument
import kr.entropi.minecraft.kons.argument.KonsArgumentBuilder
import kr.entropi.minecraft.kons.argument.KonsArguments
import kr.entropi.minecraft.kons.context.KonsCallContext
import kr.entropi.minecraft.kons.error.KonsArgumentFilterError
import kotlin.properties.Delegates

class KonsBooleanArgument(
    filter: (Boolean) -> Boolean,
    completerOverride: KonsCompleter? = null
) : KonsArgument<Boolean>(filter, completerOverride) {

    override val typeString = "Double"

    override var parsed by Delegates.notNull<Boolean>()

    override fun parse(index: Int, input: String, context: KonsCallContext) {
        val number = input.toBoolean()

        if(!filter(number)) throw KonsArgumentFilterError(index, input, typeString)

        parsed = number
    }

    override fun complete(index: Int, context: KonsCallContext): List<String> {
        return emptyList()
    }

}

class KonsBooleanArgumentBuilder: KonsArgumentBuilder<Boolean, KonsBooleanArgument>() {
    override fun build(): KonsBooleanArgument {
        return KonsBooleanArgument(filter, completer)
    }
}

fun KonsArguments.boolean(body: KonsBooleanArgumentBuilder.() -> Unit): KonsBooleanArgument {
    val builder = KonsBooleanArgumentBuilder()

    builder.apply(body)

    val argument = builder.build()

    args.add(argument)

    return argument
}
