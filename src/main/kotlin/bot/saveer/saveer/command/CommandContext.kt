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

package bot.saveer.saveer.command

import bot.saveer.saveer.core.Saveer
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import java.awt.Color

@Suppress("unused")
interface CommandContext {

    val saveer: Saveer

    val usedInvoke: String

    val prefix: String

    val args: CommandArguments

    val message: Message

    val guild: Guild
        get() = message.guild

    val textChannel: TextChannel
        get() = message.textChannel

    fun sendError(title: String, message: String) {
        val embed = EmbedBuilder()
                .setTitle(title)
                .setDescription(message)
                .setColor(Color(226, 54, 54))
                .build()
        sendSafe(MessageBuilder().setEmbed(embed).build())
    }

    fun sendSafe(message: Message) {
        if (hasSendPermissions())
            textChannel.sendMessage(message).queue()
    }

    private fun hasSendPermissions(): Boolean {
        return guild.selfMember.hasPermission(textChannel, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE)
    }
}