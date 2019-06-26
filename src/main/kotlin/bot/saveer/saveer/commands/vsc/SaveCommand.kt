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

package bot.saveer.saveer.commands.vsc

import bot.saveer.saveer.command.AbstractCommand
import bot.saveer.saveer.command.CommandCategory
import bot.saveer.saveer.command.CommandContext
import bot.saveer.saveer.command.CommandPermissions
import bot.saveer.saveer.entities.vsc.VSCCommit

class SaveCommand(category: CommandCategory) : AbstractCommand(
        "Save",
        listOf("save", "s"),
        "<tag> [message]",
        "Saves the current guild.",
        category,
        permissions = CommandPermissions.GUILD_ADMIN
) {
    override fun execute(ctx: CommandContext) {
        if (ctx.args.isEmpty()) {
            ctx.sendError("Not enough arguments!", getUsageString(ctx))
            return
        }

        val tag = ctx.args[0]
        var message = "No message provided."
        if (ctx.args.size > 1) {
            message = ctx.args.stringInRange(1, ctx.args.size, " ")
        }

        val commit = VSCCommit(ctx.author, ctx.guild, tag, message)
    }
}