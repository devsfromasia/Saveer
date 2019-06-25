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

/**
 * Categories are used to organize commands.
 */
public interface CommandCategory {

  /**
   * The name of this category.
   *
   * @return The name.
   */
  String getName();

  /**
   * The description of this category.
   *
   * @return The description.
   */
  String getDescription();

  /**
   * The {@link Permission}s that the user requires to execute
   * commands of this category.
   *
   * @return The required {@link Permission}s for the user.
   */
  Permission[] getUserPermissions();

  /**
   * Whether this commands of this category only can be executed by the owner.
   *
   * @return If commands of this category are owner restricted.
   */
  boolean isOwnerRestricted();
}
