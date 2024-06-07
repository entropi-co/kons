package kr.entropi.minecraft.kons.argument.impl

import kr.entropi.minecraft.kons.KonsCompleter
import kr.entropi.minecraft.kons.error.KonsArgumentFilterError
import kr.entropi.minecraft.kons.argument.KonsArgument
import kr.entropi.minecraft.kons.argument.KonsArgumentBuilder
import kr.entropi.minecraft.kons.argument.KonsArguments
import kr.entropi.minecraft.kons.context.KonsCallContext
import kr.entropi.minecraft.kons.error.KonsArgumentTargetNotFoundError
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

class KonsPlayerArgumentBuilder: KonsArgumentBuilder<Player, KonsPlayerArgument>() {
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
