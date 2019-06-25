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
import bot.saveer.saveer.commands.Test2Command;
import bot.saveer.saveer.commands.TestCommand;

/**
 * The CommandRegistry is used to register all commands.
 */
public class CommandRegistry {

  private final CommandManager commandManager;

  /**
   * Creates a new {@link CommandRegistry}.
   *
   * @param commandManager The {@link CommandManager}.
   */
  public CommandRegistry(final CommandManager commandManager) {
    this.commandManager = commandManager;
    registerCommands();
  }

  private void registerCommands() {
    // Create categories
    final var ownerCategory = new CommandCategoryBuilder("Owner")
        .setOwnerRestricted(true)
        .build();
    final var generalCategory = new CommandCategoryBuilder("General")
        .setOwnerRestricted(true)
        .build();

    // Register actual commands
    final var testCommand = new TestCommand();
    testCommand.setCommandCategory(ownerCategory);
    commandManager.register(testCommand);

    final var test2Command = new Test2Command();
    test2Command.setCommandCategory(generalCategory);
    commandManager.register(test2Command);
  }

  public CommandManager getCommandManager() {
    return commandManager;
  }
}
