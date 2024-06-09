# Deprecated
### Kons is deprecated by 06/09/2024, use [arki-commands](https://github.com/entropi-kr/arki-commands) instead

# Kons (ALPHA)

A simple command framework for Bukkit and Kotlin

```kt
Kons.register {
    name = "hello"
    
    requires { isOp }
    
    action(::HelloArguments) {
        sender.sendMessage("Hello, OP ${arguments.player.name}!")
    }
}

class HelloArguments: KonsArguments() {
    val player by player {
        filter { p ->
            p.isOp
        }
    }
}
```