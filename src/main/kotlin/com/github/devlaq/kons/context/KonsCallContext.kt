package com.github.devlaq.kons.context

import com.github.devlaq.kons.Kons
import org.bukkit.command.CommandSender

class KonsCallContext(
    val sender: CommandSender,
    val kons: Kons,
    val command: String,
    val arguments: Array<out String>
)
