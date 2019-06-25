/*
 * Saveer
 * Copyright (C) 2019  Yannick Seeger, Leon Kappes, Michael Rittmeister
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package bot.saveer.saveer.command.internal

import bot.saveer.saveer.command.Command
import bot.saveer.saveer.command.CommandHandler
import bot.saveer.saveer.command.CommandManager
import bot.saveer.saveer.core.Saveer
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.SubscribeEvent

internal class CommandHandlerImpl(
        override val saveer: Saveer,
        override val commandManager: CommandManager
) : CommandHandler {

    val defaultPrefix = saveer.config.prefix

    @SubscribeEvent
    override fun handleMessage(event: MessageReceivedEvent) {
        if (event.author.isBot || event.author.isFake)
            return

        val content = event.message.contentRaw
        if (!content.startsWith(defaultPrefix) || !content.startsWith(event.jda.selfUser.asMention))
            return

        val splitted = content.split("\\s+")
        val prefix = if (content.startsWith(defaultPrefix)) defaultPrefix else event.jda.selfUser.asMention
        val command = splitted[0].substring(prefix.length)
        val args = splitted.subList(1, splitted.size - 1)

        if (!commandManager.aliasCommands.containsKey(command))
            return

    }

    private fun callCommand(command: Command, args: List<String>, message: Message) {
        if (args.isNotEmpty()) {
            val subcommand = command.subcommands.firstOrNull { cmd -> cmd.aliases.contains(args[0]) }
            if (subcommand != null) {
                val newArgs = args.subList(1, args.size - 1)
                callCommand(subcommand, newArgs, message)
                return
            }
        }

        if (command.permissions.botPermissions.any { permission -> !message.guild.selfMember.hasPermission(message.textChannel, permission) }) {
            // TODO: Send not enough permissions message
            return
        }

        if (command.permissions.userPermissions.any { permission -> !message.guild.getMember(message.author)!!.hasPermission(permission) }) {
            // TODO: Send not enough permissions message
            return
        }

        if (command.permissions.ownerRestricted && message.author.id !in saveer.config.owners) {
            // TODO: Send not enough permissions message
            return
        }
        command.execute(CommandContextImpl(saveer, args, message))
    }
}