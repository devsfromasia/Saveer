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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.dv8tion.jda.api.Permission;

public class CommandCategoryBuilder {

  private final String name;
  private final List<Permission> userPermissions = new ArrayList<>();
  private String description = "No description provided";
  private boolean ownerRestricted = false;

  public CommandCategoryBuilder(String name) {
    this.name = Objects.requireNonNull(name);
  }

  public CommandCategoryBuilder addUserPermission(Permission permission) {
    this.userPermissions.add(Objects.requireNonNull(permission));
    return this;
  }

  /**
   * Creates a new {@link CommandCategory}.
   *
   * @return The new {@link CommandCategory}.
   */
  public CommandCategory build() {
    return new BasicCommandCategory(
        name,
        description,
        userPermissions.toArray(new Permission[0]),
        ownerRestricted
    );
  }

  public String getName() {
    return name;
  }

  public List<Permission> getUserPermissions() {
    return userPermissions;
  }

  public String getDescription() {
    return description;
  }

  public CommandCategoryBuilder setDescription(String description) {
    this.description = Objects.requireNonNull(description);
    return this;
  }

  public boolean isOwnerRestricted() {
    return ownerRestricted;
  }

  public CommandCategoryBuilder setOwnerRestricted(boolean ownerRestricted) {
    this.ownerRestricted = ownerRestricted;
    return this;
  }
}
