package kr.entropi.minecraft.kons

import kr.entropi.minecraft.kons.error.*
import org.bukkit.Bukkit
import org.bukkit.command.PluginCommand
import org.bukkit.command.SimpleCommandMap
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.SimplePluginManager

object KonsRegistry {

    private val fallbackErrorHandler: KonsErrorHandler = { context, error ->
        when(error) {
            is KonsArgumentCountError -> {
                context.sender.sendMessage("ArgumentError(needed: ${error.needed}, input: ${error.index})")
            }
            is KonsArgumentFilterError -> {
                context.sender.sendMessage("ArgumentError(Argument doesn't pass the filter)")
            }
            is KonsArgumentParseError -> {
                context.sender.sendMessage("ArgumentError(Failed to parse the argument: ${error.reason?.javaClass?.simpleName ?: "Unknown"})")
            }
            is KonsArgumentTargetNotFoundError -> {
                context.sender.sendMessage("ArgumentError(Target does not exist)")
            }
            is KonsFilterFailError -> {
                context.sender.sendMessage("Error(Sender doesn't pass the filter)")
            }
            is KonsSubcommandNotFoundError -> {
                context.sender.sendMessage("Error(Subcommand was not found)")
            }
        }
    }

    private val defaultErrorHandlers = mutableMapOf<Plugin, KonsErrorHandler>()

    fun setDefaultErrorHandler(plugin: Plugin, handler: KonsErrorHandler) {
        defaultErrorHandlers[plugin] = handler
    }

    fun getDefaultErrorHandler(plugin: Plugin): KonsErrorHandler {
        return defaultErrorHandlers[plugin] ?: fallbackErrorHandler
    }

    fun registerToCommandMap(plugin: Plugin, kons: Kons) {
        val pluginManager = Bukkit.getPluginManager() as SimplePluginManager

        val commandMapField = SimplePluginManager::class.java.getDeclaredField("commandMap")
        commandMapField.isAccessible = true

        val commandMap = commandMapField.get(pluginManager) as SimpleCommandMap

        val pluginCommandConstructor = PluginCommand::class.java.declaredConstructors[0]
        pluginCommandConstructor.isAccessible = true

        val pluginCommand = pluginCommandConstructor.newInstance(kons.name, plugin) as PluginCommand
        pluginCommand.setExecutor(kons.createExecutor())
        pluginCommand.tabCompleter = kons.createCompleter()

        commandMap.register(plugin.description.name, pluginCommand)
    }

}
