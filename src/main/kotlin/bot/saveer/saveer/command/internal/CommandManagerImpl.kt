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
import bot.saveer.saveer.command.CommandManager
import org.slf4j.LoggerFactory

internal class CommandManagerImpl : CommandManager {

    private val log = LoggerFactory.getLogger(CommandManagerImpl::class.java)
    override val uniqueCommands = mutableListOf <Command>()
    override val aliasCommands = mutableMapOf<String, Command>()

    override fun register(vararg commands: Command) {
        commands.forEach(this::register)
    }

    private fun register(command: Command) {
        command.aliases.forEach { alias ->
            if (aliasCommands.containsKey(alias)) {
                log.warn("Alias `{}` of command `{}` is already registered by command `{}`.", alias, command, aliasCommands[alias])
            } else {
                aliasCommands[alias] = command
                log.debug("Registered alias `{}` of command `{}`.", alias, command)
            }
        }
    }
}