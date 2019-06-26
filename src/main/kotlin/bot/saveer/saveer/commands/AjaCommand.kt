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

package bot.saveer.saveer.commands

import bot.saveer.saveer.command.AbstractCommand
import bot.saveer.saveer.command.Command
import bot.saveer.saveer.command.CommandContext
import bot.saveer.saveer.command.SubCommand
import bot.saveer.saveer.command.internal.CommandCategoryImpl

class AjaCommand : AbstractCommand("Aja", listOf("aja"), category = CommandCategoryImpl("ja", "nein")) {

    init {
        registerSubCommand(AjaSubCommand(this))
    }

    override fun execute(ctx: CommandContext) {
        ctx.textChannel.sendMessage("hiiiiii").queue()
    }
}

class AjaSubCommand(parent: Command) : SubCommand(parent, "Subcmd", listOf("subcmd")) {
    override fun execute(ctx: CommandContext) {
        ctx.textChannel.sendMessage("sub").queue()
    }

}