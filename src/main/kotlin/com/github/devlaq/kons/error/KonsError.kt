package com.github.devlaq.kons.error

sealed class KonsError : Throwable()

/**
 * Occurs when the player does not pass the filter
 */
class KonsFilterFailError(
    val reason: String? = null
): KonsError()

/**
 * Occurs when the subcommand is not found
 */
class KonsSubcommandNotFoundError(
    val name: String
): KonsError()

/**
 * Occurs when failed to call the command because of argument errors
 *
 * @param index Argument's index
 * @param input Raw input of the argument
 * @param requiredType The required type of the argument
 */
sealed class KonsArgumentError(
    val index: Int,
    val input: String,
    val requiredType: String
): KonsError()

class KonsArgumentCountError(
    index: Int,
    val needed: Int,
    requiredType: String
): KonsArgumentError(index, "none", requiredType)

/**
 * Occurs when the [input] was able to parse but couldn't pass the argument filter
 */
class KonsArgumentFilterError(
    index: Int,
    input: String,
    requiredType: String
) : KonsArgumentError(index, input, requiredType)

/**
 * Occurs when failed to parse the [input]
 *
 * @param reason The reason why argument is not parsable
 */
class KonsArgumentParseError(
    index: Int,
    input: String,
    requiredType: String,
    val reason: Throwable? = null
): KonsArgumentError(index, input, requiredType)

/**
 * Occurs when parsed target of [input] was not found
 */
class KonsArgumentTargetNotFoundError(
    index: Int,
    input: String,
    requiredType: String
): KonsArgumentError(index, input, requiredType)
