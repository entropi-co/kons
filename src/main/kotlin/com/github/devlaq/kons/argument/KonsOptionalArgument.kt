package com.github.devlaq.kons.argument

import com.github.devlaq.kons.KonsCompleter

abstract class KonsOptionalArgument<Type>(
    filter: (Type?) -> Boolean,
    completerOverride: KonsCompleter?
) : KonsArgument<Type?>(filter, completerOverride) {
    override var parsed: Type? = null
}
