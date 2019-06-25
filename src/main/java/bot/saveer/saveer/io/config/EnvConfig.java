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

package bot.saveer.saveer.io.config;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.Optional;

public class EnvConfig implements Config {

  private final String token;
  private final String[] owners;

  /**
   * Creates a new {@link EnvConfig}.
   */
  public EnvConfig() {
    var dotenv = Dotenv.load();
    this.token = dotenv.get("SAVEER_TOKEN");
    this.owners = Optional.ofNullable(dotenv.get("SAVEER_OWNERS")).orElse("").split(";");
  }

  @Override
  public String getToken() {
    return this.token;
  }

  @Override
  public String[] getOwners() {
    return owners.clone();
  }
}
