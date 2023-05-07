package com.github.devlaq.kons.argument

abstract class KonsArgumentBuilder<ArgumentType: KonsArgument<*>> {

    abstract fun build(): ArgumentType

}
