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

interface Command {

    /**
     * The name of this command.
     */
    val displayName: String

    /**
     * The aliases of this command that are used to identify the command
     * in a [net.dv8tion.jda.api.entities.Message].
     */
    val aliases: List<String>

    /**
     * The usage of this command.
     */
    val usage: String

    /**
     * The description of this command.
     */
    val description: String

    /**
     * The subcommands of this command.
     */
    val subcommands: List<Command>

    val category: CommandCategory

    val permissions: CommandPermissions

    fun beforeExecute()

    fun execute(ctx: CommandContext)

    fun afterExecute()

    fun getUsageString(ctx: CommandContext): String {
        return ctx.prefix + ctx.usedInvoke + " " + usage
    }
}