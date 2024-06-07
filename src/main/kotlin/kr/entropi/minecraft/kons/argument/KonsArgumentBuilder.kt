package kr.entropi.minecraft.kons.argument

import kr.entropi.minecraft.kons.KonsCompleter

abstract class KonsArgumentBuilder<T, ArgumentType: KonsArgument<T>> {
    var filter: (T) -> Boolean = { true }
    var completer: KonsCompleter? = null

    abstract fun build(): ArgumentType

    fun filter(filter: (T) -> Boolean) {
        this.filter = filter
    }

    fun complete(body: KonsCompleter) {
        this.completer = body
    }

}
