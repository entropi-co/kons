package kr.entropi.minecraft.kons.builder

import kr.entropi.minecraft.kons.Kons
import kr.entropi.minecraft.kons.KonsErrorHandler
import kr.entropi.minecraft.kons.argument.KonsArguments
import kr.entropi.minecraft.kons.context.KonsActionContext
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

class KonsBuilder(val plugin: Plugin) {

    lateinit var name: String

    private var requires: (CommandSender) -> Boolean = { true }
    private val subcommands = mutableListOf<Kons>()

    private var arguments: (() -> KonsArguments)? = null
    private var action: (KonsActionContext<*>) -> Unit = {}

    private var errorHandler: KonsErrorHandler? = null

    fun requires(body: CommandSender.() -> Boolean) {
        requires = body
    }

    @Suppress("UNCHECKED_CAST")
    fun <ArgumentType: KonsArguments> action(arguments: () -> ArgumentType, body: KonsActionContext<ArgumentType>.() -> Unit) {
        this.arguments = arguments
        this.action = body as (KonsActionContext<*>) -> Unit
    }

    @Suppress("UNCHECKED_CAST")
    fun action(body: KonsActionContext<KonsArguments>.() -> Unit) {
        this.action = body as (KonsActionContext<*>) -> Unit
    }

    /**
     * Register an error handler
     */
    fun error(body: KonsErrorHandler) {
        errorHandler = body
    }

    fun subcommand(name: String, body: KonsBuilder.() -> Unit) {
        val builder = KonsBuilder(plugin)

        builder.name = name
        builder.apply(body)

        subcommands.add(builder.build())
    }

    fun build(): Kons {
        return Kons(
            plugin = plugin,
            name = name,
            requires = requires,
            subcommands = subcommands,
            arguments = arguments ?: { null },
            action = action,
            error = errorHandler
        )
    }
}
