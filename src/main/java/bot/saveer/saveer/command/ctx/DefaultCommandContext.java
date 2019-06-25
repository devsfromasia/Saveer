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

package bot.saveer.saveer.command.ctx;

import bot.saveer.saveer.core.Saveer;
import net.dv8tion.jda.api.entities.Message;

public class DefaultCommandContext implements CommandContext {

  private final Saveer saveer;
  private final String[] args;
  private final Message message;

  /**
   * Default implementation for {@link CommandContext}.
   * @param saveer The {@link Saveer} instance.
   * @param message The {@link Message}.
   * @param args The command args.
   */
  public DefaultCommandContext(Saveer saveer, Message message, String[] args) {
    this.saveer = saveer;
    this.message = message;
    this.args = args.clone();
  }

  @Override
  public Saveer getSaveer() {
    return saveer;
  }

  @Override
  public String[] getArgs() {
    return args.clone();
  }

  @Override
  public Message getMessage() {
    return message;
  }
}
