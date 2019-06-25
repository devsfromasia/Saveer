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

import bot.saveer.saveer.core.Bot;
import bot.saveer.saveer.io.config.EnvConfig;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main entry of the bot.
 */
public class Launcher {

  private static final Logger log = LoggerFactory.getLogger(Launcher.class);

  /**
   * The program entry method.
   * <br>
   * It handles basic initialization tasks like loading
   * configuration or handling CLI commands.
   *
   * @param args The passed program arguments.
   * @throws LoginException See {@link DefaultShardManagerBuilder#build()}
   */
  public static void main(String[] args) throws LoginException {
    log.debug("Loading config...");
    var config = new EnvConfig();
    log.debug("Config loaded.");

    log.debug("Initialising saveer...");
    var saveer = new Bot(config);
    log.info("Starting saveer...");
    saveer.start();
  }
}
