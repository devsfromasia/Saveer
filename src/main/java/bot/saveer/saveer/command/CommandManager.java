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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The CommandManager handles the registering of commands.
 */
public class CommandManager {

  private final Logger log = LoggerFactory.getLogger(getClass());
  private final List<Command> uniqueCommands = new ArrayList<>();
  private final Map<String, Command> aliasCommands = new HashMap<>();

  /**
   * Registers the commands.
   *
   * @param commands The commands that should be registered.
   * @return This {@link CommandManager} instance.
   */
  public CommandManager register(Command... commands) {
    List.of(commands).forEach(this::register);
    return this;
  }

  /**
   * Registers a single {@link Command}.
   * <br>If one of the aliases is already registered,
   * a warning gets printed to the console.
   *
   * @param command The {@link Command} that should be registered.
   * @return This {@link CommandManager} instance.
   */
  public CommandManager register(Command command) {
    List.of(command.getAliases()).forEach(alias -> {
      if (aliasCommands.containsKey(alias)) {
        log.warn(
            "Alias `{}` is already registered by `{}`.",
            alias,
            aliasCommands.get(alias).getClass().getName()
        );
      } else {
        this.aliasCommands.put(alias, command);
        log.debug("Registered alias `{}` of command `{}`.", alias, command.getClass().getName());
      }
    });
    return this;
  }

  /**
   * A list with all {@link Command}s.
   *
   * @return The list with unique {@link Command}s.
   */
  public List<Command> getUniqueCommands() {
    return this.uniqueCommands;
  }

  /**
   * A map with all aliases of all {@link Command}s in relation with their {@link Command}.
   *
   * @return The {@link Map} with aliases and commands.
   */
  public Map<String, Command> getAliasCommands() {
    return this.aliasCommands;
  }
}
