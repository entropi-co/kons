package kr.entropi.minecraft.kons.context

import kr.entropi.minecraft.kons.argument.KonsArguments
import org.bukkit.command.CommandSender

class KonsActionContext<ArgumentType: KonsArguments?>(
    val command: String,
    val arguments: ArgumentType,
    val sender: CommandSender
)
