package kr.entropi.minecraft.kons

import kr.entropi.minecraft.kons.context.KonsCallContext
import kr.entropi.minecraft.kons.error.KonsError

typealias KonsErrorHandler = (KonsCallContext, KonsError) -> Unit

typealias KonsCompleter = (KonsCallContext) -> List<String>
