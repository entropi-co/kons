package com.github.devlaq.kons

import com.github.devlaq.kons.context.KonsCallContext
import com.github.devlaq.kons.error.KonsError

typealias KonsErrorHandler = (KonsCallContext, KonsError) -> Unit

typealias KonsCompleter = (KonsCallContext) -> List<String>
