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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.dv8tion.jda.api.Permission;

public abstract class AbstractCommand implements Command {

  private final String displayName;
  private final String[] aliases;
  private final List<Command> subcommands = new ArrayList<>();
  private final List<Permission> userPermissions = new ArrayList<>();
  private final List<Permission> botPermissions = new ArrayList<>();
  private String usage = "";
  private String description = "No description provided.";
  private boolean ownerRestricted = false;
  private CommandCategory commandCategory = null;

  /**
   * Creates a new {@link AbstractCommand}.
   * @param displayName The display name of this command.
   * @param aliases The aliases of this command.
   */
  public AbstractCommand(String displayName, String[] aliases) {
    this.displayName = Objects.requireNonNull(displayName);
    this.aliases = Objects.requireNonNull(aliases).clone();
    addBotPermissions(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE);
  }

  public AbstractCommand setCommandCategory(CommandCategory category) {
    this.commandCategory = category;
    return this;
  }

  protected AbstractCommand addSubcommand(Command command) {
    this.subcommands.add(Objects.requireNonNull(command));
    return this;
  }

  protected AbstractCommand addUserPermissions(Permission... permissions) {
    this.userPermissions.addAll(List.of(permissions));
    return this;
  }

  protected AbstractCommand addBotPermissions(Permission... permissions) {
    this.botPermissions.addAll(List.of(permissions));
    return this;
  }

  @Override
  public String getDisplayName() {
    return displayName;
  }

  @Override
  public String[] getAliases() {
    return aliases.clone();
  }

  @Override
  public String getUsage() {
    return usage;
  }

  protected AbstractCommand setUsage(String usage) {
    this.usage = Objects.requireNonNull(usage);
    return this;
  }

  @Override
  public String getDescription() {
    return description;
  }

  protected AbstractCommand setDescription(String description) {
    this.description = Objects.requireNonNull(description);
    return this;
  }

  @Override
  public Command[] getSubcommands() {
    return subcommands.toArray(new Command[0]);
  }

  @Override
  public Permission[] getUserPermissions() {
    return userPermissions.toArray(new Permission[0]);
  }

  @Override
  public Permission[] getBotPermissions() {
    return botPermissions.toArray(new Permission[0]);
  }

  @Override
  public boolean isOwnerRestricted() {
    return ownerRestricted;
  }

  protected AbstractCommand setOwnerRestricted(boolean ownerRestricted) {
    this.ownerRestricted = ownerRestricted;
    return this;
  }

  @Override
  public CommandCategory getCommandCategory() {
    return commandCategory;
  }

  @Override
  public void beforeExecute() {
    // TODO Metrics
  }
}