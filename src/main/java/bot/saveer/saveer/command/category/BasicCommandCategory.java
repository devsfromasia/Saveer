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

package bot.saveer.saveer.command.category;

import net.dv8tion.jda.api.Permission;

public class BasicCommandCategory implements CommandCategory {

  private final String name;
  private final String description;
  private final Permission[] userPermissions;
  private final boolean ownerRestricted;

  /**
   * Basic implementation of the {@link CommandCategory}.
   *
   * @param name The name of this category.
   * @param description The description of this category.
   * @param userPermissions The required permissions for the user.
   * @param ownerRestricted Whether this command only can be executed by an owner.
   */
  public BasicCommandCategory(
      String name,
      String description,
      Permission[] userPermissions,
      boolean ownerRestricted) {
    this.name = name;
    this.description = description;
    this.userPermissions = userPermissions;
    this.ownerRestricted = ownerRestricted;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public Permission[] getUserPermissions() {
    return userPermissions;
  }

  @Override
  public boolean isOwnerRestricted() {
    return ownerRestricted;
  }
}
