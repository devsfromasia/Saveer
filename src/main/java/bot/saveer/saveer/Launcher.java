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

package bot.saveer.saveer;

import bot.saveer.saveer.core.SaveerBot;
import bot.saveer.saveer.io.config.EnvConfig;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main entry of the bot.
 */
public final class Launcher {

  private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);

  private Launcher() {
  }

  /**
   * The program entry method.
   * <br>
   * It handles basic initialization tasks like loading
   * configuration or handling CLI commands.
   *
   * @param args The passed program arguments.
   * @throws LoginException See {@link DefaultShardManagerBuilder#build()}
   */
  public static void main(final String[] args) throws LoginException {
    LOG.debug("Loading config...");
    final var config = new EnvConfig();
    LOG.debug("Config loaded.");

    LOG.debug("Initialising saveer...");
    final var saveer = new SaveerBot(config);
    LOG.info("Starting saveer...");
    saveer.start();
  }
}
