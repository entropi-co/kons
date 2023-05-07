package com.github.devlaq.kons.argument.impl

import com.github.devlaq.kons.KonsCompleter
import com.github.devlaq.kons.error.KonsArgumentFilterError
import com.github.devlaq.kons.argument.KonsArgument
import com.github.devlaq.kons.argument.KonsArgumentBuilder
import com.github.devlaq.kons.argument.KonsArguments
import com.github.devlaq.kons.context.KonsCallContext
import com.github.devlaq.kons.error.KonsArgumentTargetNotFoundError
import org.bukkit.Bukkit
import org.bukkit.entity.Player


class KonsPlayerArgument(
    filter: (Player) -> Boolean,
    completerOverride: KonsCompleter? = null
) : KonsArgument<Player>(filter, completerOverride) {

    override val typeString = "Player"

    override lateinit var parsed: Player

    override fun parse(index: Int, input: String, context: KonsCallContext) {
        val player = Bukkit.getPlayer(input) ?: throw KonsArgumentTargetNotFoundError(index, input, "Player")

        if(!filter(player)) throw KonsArgumentFilterError(index, input, "Player")

        parsed = player
    }

    override fun complete(index: Int, context: KonsCallContext): List<String> {
        return Bukkit.getOnlinePlayers().map { it.name }
    }

}

class KonsPlayerArgumentBuilder: KonsArgumentBuilder<KonsPlayerArgument>() {
    private var filter: (Player) -> Boolean = { true }
    private var completer: KonsCompleter? = null

    fun filter(filter: (Player) -> Boolean) {
        this.filter = filter
    }

    fun complete(body: KonsCompleter) {
        this.completer = body
    }

    override fun build(): KonsPlayerArgument {
        return KonsPlayerArgument(filter, completer)
    }
}

fun KonsArguments.player(body: KonsPlayerArgumentBuilder.() -> Unit): KonsPlayerArgument {
    val builder = KonsPlayerArgumentBuilder()

    builder.apply(body)

    val argument = builder.build()

    args.add(argument)

    return argument
}
