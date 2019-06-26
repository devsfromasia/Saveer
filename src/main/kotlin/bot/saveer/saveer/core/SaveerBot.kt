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

package bot.saveer.saveer.core

import bot.saveer.saveer.command.CommandHandler
import bot.saveer.saveer.command.CommandManager
import bot.saveer.saveer.command.internal.CommandHandlerImpl
import bot.saveer.saveer.command.internal.CommandManagerImpl
import bot.saveer.saveer.commands.AjaCommand
import bot.saveer.saveer.io.config.Config
import net.dv8tion.jda.api.hooks.AnnotatedEventManager
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager

class SaveerBot(
        override val config: Config
) : Saveer {

    override val commandManger: CommandManager
    override lateinit var shardManager: ShardManager
    private val commandHandler: CommandHandler

    init {
        commandManger = CommandManagerImpl()
        commandManger.register(AjaCommand())
        commandHandler = CommandHandlerImpl(this, commandManger)
    }

    fun start() {
        val builder = DefaultShardManagerBuilder(config.token)
        builder.setEventManager(AnnotatedEventManager())

        builder.addEventListeners(commandHandler)

        shardManager = builder.build()
    }

    override fun shutdown() {
        shardManager.shutdown()
    }
}