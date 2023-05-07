package com.github.devlaq.kons

import com.github.devlaq.kons.argument.KonsArguments
import com.github.devlaq.kons.argument.KonsOptionalArgument
import com.github.devlaq.kons.builder.KonsBuilder
import com.github.devlaq.kons.context.KonsActionContext
import com.github.devlaq.kons.context.KonsCallContext
import com.github.devlaq.kons.error.*
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.Plugin

class Kons(
    val plugin: Plugin,

    val name: String,

    val requires: (CommandSender) -> Boolean,
    val subcommands: List<Kons>,

    val arguments: () -> KonsArguments?,
    val action: (KonsActionContext<*>) -> Unit,

    val error: ((KonsCallContext, KonsError) -> Unit)?
) {

    companion object {
        fun build(plugin: Plugin, body: KonsBuilder.() -> Unit): Kons {
            val builder = KonsBuilder(plugin)

            builder.apply(body)

            return builder.build()
        }

        fun register(plugin: Plugin, body: KonsBuilder.() -> Unit) {
            KonsRegistry.registerToCommandMap(plugin, build(plugin, body))
        }

        fun Plugin.buildKons(body: KonsBuilder.() -> Unit) = build(this, body)

        fun Plugin.registerKons(body: KonsBuilder.() -> Unit) {
            val kons = buildKons(body)

            KonsRegistry.registerToCommandMap(this, kons)
        }
    }

    fun getErrorHandler() = this.error ?: KonsRegistry.getDefaultErrorHandler(plugin)


    fun execute(context: KonsCallContext): Boolean {
        // Check filter
        if(!requires(context.sender)) {
            getErrorHandler().invoke(context, KonsFilterFailError())
        }

        val arguments = arguments() ?: KonsArguments()

        val actionContext = KonsActionContext(
            command = context.command,
            arguments = arguments,
            sender = context.sender
        )

        if(context.kons.subcommands.isNotEmpty()) {
            val subcommandName = context.arguments.firstOrNull()?.lowercase()

            if(subcommandName == null) {
                action(actionContext)
                return true
            } else {
                val subcommand = context.kons.subcommands.find { it.name.lowercase() == subcommandName }
                if(subcommand == null) {
                    getErrorHandler()(context, KonsSubcommandNotFoundError(subcommandName))
                    return false
                }

                return subcommand.execute(
                    KonsCallContext(
                        sender = context.sender,
                        arguments = context.arguments.drop(1).toTypedArray(),
                        command = subcommandName,
                        kons = subcommand
                    )
                )
            }
        } else {
            arguments.args.forEachIndexed { index, it ->
                val rawArg = context.arguments.getOrNull(index)?.lowercase()
                if(rawArg == null) {
                    if(it is KonsOptionalArgument) {
                        // Pass optional argument
                        return@forEachIndexed
                    } else {
                        getErrorHandler()
                            .invoke(context, KonsArgumentCountError(
                                index = index,
                                needed = arguments.args.filter { it !is KonsOptionalArgument }.size,
                                requiredType = it.typeString
                            ))
                    }
                    return false
                }

                try {
                    it.parse(index, rawArg, context)
                } catch (error: KonsError) {
                    getErrorHandler()(context, error)
                    return false
                } catch (throwable: Throwable) {
                    getErrorHandler()(context, KonsArgumentParseError(index, rawArg, it.typeString, throwable))
                    return false
                }
            }

            action(actionContext)

            return true
        }
    }

    fun complete(context: KonsCallContext): List<String> {

        val firstArg = context.arguments.firstOrNull()

        val subcommand = context.kons.subcommands.find { it.name.equals(firstArg, true) }

        if(subcommand != null) {
            return subcommand.complete(
                KonsCallContext(
                    sender = context.sender,
                    arguments = context.arguments.drop(1).toTypedArray(),
                    command = firstArg!!,
                    kons = subcommand
                )
            )
        }

        if(context.kons.subcommands.isNotEmpty() && context.arguments.size == 1) {
            return context.kons.subcommands.filter { it.requires(context.sender) }.map { it.name }
        }

        val arguments = context.kons.arguments()
            ?: // return players if action has no arguments
            return Bukkit.getOnlinePlayers().map { it.name }

        val argument = arguments.args.getOrNull(context.arguments.size-1) ?: return Bukkit.getOnlinePlayers().map { it.name }

        return argument.completerOverride?.invoke(context)
            ?: argument.complete(context.arguments.size, context)
    }

    internal fun createExecutor(): CommandExecutor {
        return CommandExecutor { sender, _, label, args ->
            val callContext = KonsCallContext(
                sender,
                this,
                label,
                args
            )

            return@CommandExecutor execute(callContext)
        }
    }

    internal fun createCompleter(): TabCompleter {
        return TabCompleter { sender, _, label, args ->
            val callContext = KonsCallContext(
                sender,
                this,
                label,
                args
            )

            return@TabCompleter complete(callContext)
        }
    }

}
