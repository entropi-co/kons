package com.github.devlaq.kons.argument

import com.github.devlaq.kons.KonsCompleter
import com.github.devlaq.kons.context.KonsCallContext
import com.github.devlaq.kons.error.KonsArgumentFilterError
import com.github.devlaq.kons.error.KonsArgumentParseError
import com.github.devlaq.kons.error.KonsArgumentTargetNotFoundError
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
