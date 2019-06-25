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

package bot.saveer.saveer.core;

import bot.saveer.saveer.command.CommandHandler;
import bot.saveer.saveer.command.CommandManager;
import bot.saveer.saveer.io.config.Config;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

/**
 * The Bot implements {@link Saveer} and is the main class of this application.
 */
public class Bot implements Saveer {

  private final Config config;
  private final CommandManager commandManager;
  private ShardManager shardManager;

  public Bot(Config config) {
    this.config = config;
    this.commandManager = new CommandManager();
    new CommandRegistry(commandManager);
  }

  /**
   * Starts the bot by building the {@link DefaultShardManagerBuilder#build()} and
   * connecting to the Discord gateway.
   *
   * @throws LoginException See {@link DefaultShardManagerBuilder#build()}
   */
  public void start() throws LoginException {
    var builder = new DefaultShardManagerBuilder();
    builder.setToken(config.getToken());
    builder.setEventManager(new AnnotatedEventManager());

    // Register event listeners
    builder.addEventListeners(new CommandHandler(this, commandManager));

    // Build shard manager
    setShardManager(builder.build());
  }

  @Override
  public Config getConfig() {
    return this.config;
  }

  @Override
  public ShardManager getShardManager() {
    return this.shardManager;
  }

  private void setShardManager(ShardManager shardManager) {
    if (getShardManager() != null) {
      throw new RuntimeException("ShardManager is already set.");
    }
    this.shardManager = shardManager;
  }

  @Override
  public CommandManager getCommandManager() {
    return this.commandManager;
  }

  @Override
  public void shutdown() {
    if (shardManager != null) {
      shardManager.shutdown();
    }
  }
}
