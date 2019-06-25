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

package bot.saveer.saveer.core;

import bot.saveer.saveer.command.CommandManager;
import bot.saveer.saveer.command.category.CommandCategoryBuilder;
import bot.saveer.saveer.commands.TestCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The CommandRegistry is used to register all commands.
 */
public class CommandRegistry {

  private final Logger log = LoggerFactory.getLogger(getClass());
  private final CommandManager commandManager;

  public CommandRegistry(CommandManager commandManager) {
    this.commandManager = commandManager;
    log.debug("Registering commands...");
    registerCommands();
    log.debug("All commands registered.");
  }

  private void registerCommands() {
    // Create categories
    var ownerCategory = new CommandCategoryBuilder("Owner")
        .setOwnerRestricted(true)
        .build();

    // Register actual commands
    commandManager.register(new TestCommand().setCategory(ownerCategory));
  }
}
