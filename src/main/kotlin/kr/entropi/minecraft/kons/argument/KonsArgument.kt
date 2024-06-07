package kr.entropi.minecraft.kons.argument

import kr.entropi.minecraft.kons.KonsCompleter
import kr.entropi.minecraft.kons.context.KonsCallContext
import kr.entropi.minecraft.kons.error.KonsArgumentFilterError
import kr.entropi.minecraft.kons.error.KonsArgumentParseError
import kr.entropi.minecraft.kons.error.KonsArgumentTargetNotFoundError
import kotlin.reflect.KProperty

abstract class KonsArgument<Type>(val filter: (Type) -> Boolean, val completerOverride: KonsCompleter?) {

    abstract val typeString: String

    abstract var parsed: Type

    open operator fun getValue(thisRef: KonsArguments, property: KProperty<*>) = parsed

    /**
     * Parse [input] and update [parsed]
     *
     * @throws KonsArgumentParseError When failed to parse [input]
     * @throws KonsArgumentTargetNotFoundError When parsed successfully but target doesn't exist
     * @throws KonsArgumentFilterError When parsed successfully but target doesn't pass the filter
     */
    abstract fun parse(
        index: Int,
        input: String,
        context: KonsCallContext
    )

    abstract fun complete(
        index: Int,
        context: KonsCallContext
    ): List<String>

}
