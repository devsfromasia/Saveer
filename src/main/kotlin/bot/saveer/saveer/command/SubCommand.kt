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

abstract class SubCommand @JvmOverloads protected constructor(
    val parent: Command,
    displayName: String,
    aliases: List<String>,
    usage: String = "",
    description: String = "No description provided.",
    permissions: CommandPermissions = CommandPermissionsImpl()
) : AbstractCommand(displayName, aliases, usage, description, parent.category, permissions)