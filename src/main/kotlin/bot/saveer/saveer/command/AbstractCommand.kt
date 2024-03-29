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

import bot.saveer.saveer.command.internal.CommandPermissionsImpl

abstract class AbstractCommand @JvmOverloads protected constructor(
    override val displayName: String,
    override val aliases: List<String>,
    override val usage: String = "",
    override val description: String = "No description provided.",
    override val category: CommandCategory,
    override val permissions: CommandPermissions = CommandPermissionsImpl()
) : Command {

    override val subcommands = mutableListOf<Command>()

    fun registerSubCommand(command: Command) {
        if (!subcommands.contains(command)) {
            subcommands.add(command)
        } else {
            // TODO: Print warning
        }
    }

    override fun beforeExecute() = Unit

    override fun afterExecute() = Unit
}