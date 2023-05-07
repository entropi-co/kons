package com.github.devlaq.kons.context

import com.github.devlaq.kons.argument.KonsArguments
import org.bukkit.command.CommandSender

class KonsActionContext<ArgumentType: KonsArguments?>(
    val command: String,
    val arguments: ArgumentType,
    val sender: CommandSender
)
