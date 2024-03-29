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
import net.dv8tion.jda.api.Permission

interface CommandPermissions {

    val userPermissions: List<Permission>

    val botPermissions: List<Permission>

    val ownerRestricted: Boolean

    companion object {
        val GUILD_ADMIN = CommandPermissionsImpl(listOf(Permission.ADMINISTRATOR), listOf(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE), false)
    }
}