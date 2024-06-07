package kr.entropi.minecraft.kons.argument

import kr.entropi.minecraft.kons.KonsCompleter

abstract class KonsOptionalArgument<Type>(
    filter: (Type?) -> Boolean,
    completerOverride: KonsCompleter?
) : KonsArgument<Type?>(filter, completerOverride) {
    override var parsed: Type? = null
}
