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

package bot.saveer.saveer.command;

import bot.saveer.saveer.command.category.CommandCategory;
import bot.saveer.saveer.command.ctx.CommandContext;
import net.dv8tion.jda.api.Permission;

/**
 * Command represents a command of the bot.
 */
public interface Command {

  /**
   * The name of this command.
   *
   * @return The display name.
   */
  String getDisplayName();

  /**
   * The aliases of this command that are used for
   * execution in the following format: {@code [prefix]alias <params...>}
   * <br> The first alias is the primary alias and displayed
   * in the command manual.
   *
   * @return The aliases of this command.
   */
  String[] getAliases();

  /**
   * The description of the exact command usage: {@code <required_param> [optional_param]}.
   * <br>Example for a delete command: {@code <amount> [@User]}
   *
   * @return The command usage.
   */
  String getUsage();

  /**
   * The description of this command (what it does,...).
   *
   * @return The command description.
   */
  String getDescription();

  /**
   * All commands that are representing further commands
   * of this command: {@code [prefix]command subcommand <param>}
   *
   * @return The subcommands.
   */
  Command[] getSubcommands();

  /**
   * The {@link Permission}s that the user requires to execute
   * this command.
   *
   * @return The required {@link Permission}s for the user.
   */
  Permission[] getUsePermissions();

  /**
   * The {@link Permission} the bot requires to run this command.
   *
   * @return The required {@link Permission} by the bot.
   */
  Permission[] getBotPermissions();

  /**
   * Whether this command can only be executed by the owner.
   *
   * @return If this command is owner restricted.
   */
  boolean isOwnerRestricted();

  /**
   * Called before command execution.
   */
  void beforeExecute();

  /**
   * Executes this command.
   *
   * @param ctx The command context.
   */
  void execute(CommandContext ctx);

  /**
   * The {@link CommandCategory} this command
   * is assigned to.
   * <br>If no category has been set, this returns null.
   *
   * @return The {@link CommandCategory}.
   */
  CommandCategory getCommandCategory();
}
