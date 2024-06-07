package kr.entropi.minecraft.kons.context

import kr.entropi.minecraft.kons.Kons
import org.bukkit.command.CommandSender

class KonsCallContext(
    val sender: CommandSender,
    val kons: Kons,
    val command: String,
    val arguments: Array<out String>
)
