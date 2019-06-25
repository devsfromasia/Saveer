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

import bot.saveer.saveer.command.ctx.DefaultCommandContext;
import bot.saveer.saveer.core.Saveer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command handler handles parsing of incoming messages
 * and calling of the actual commands.
 */
public class CommandHandler {

  private static final String DEFAULT_PREFIX = "s!";
  private final Logger log = LoggerFactory.getLogger(getClass());
  private final Saveer saveer;
  private final CommandManager commandManager;

  public CommandHandler(Saveer saveer, CommandManager commandManager) {
    this.saveer = saveer;
    this.commandManager = commandManager;
  }

  @SubscribeEvent
  public void handleMessage(MessageReceivedEvent event) {
    // Exclude bots and webhooks
    if (event.getAuthor().isBot() || event.getAuthor().isFake()) {
      return;
    }
    var content = event.getMessage().getContentRaw();
    // Message does not start with prefix
    if (!content.startsWith(DEFAULT_PREFIX) && !content.startsWith(event.getJDA().getSelfUser().getAsMention())) {
      return;
    }
    // TODO: 25.06.19 Fix mention as prefix
    var parsed = ParsedMessage.parse(new String[] {DEFAULT_PREFIX, event.getJDA().getSelfUser().getAsMention()}, content);
    // Message just starts with prefix, no real command
    if (parsed.command.equals("")) {
      return;
    }

    if (commandManager.getAliasCommands().containsKey(parsed.command)) {
      callCommand(commandManager.getAliasCommands().get(parsed.command), parsed.args, event.getMessage());
    }
  }

  private void callCommand(Command command, String[] args, Message message) {
    if (args.length > 0) {
      var possibleSubcommands = Arrays.asList(command.getSubcommands()).stream().filter(sub -> List.of(sub.getAliases()).contains(args[0])).collect(Collectors.toList());
      if (possibleSubcommands.size() == 1) {
        var newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, args.length - 1);
        callCommand(possibleSubcommands.get(0), newArgs, message);
        return;
      }
    }
    command.execute(new DefaultCommandContext(saveer, message, args));
  }

  private static class ParsedMessage {

    private final String prefix;
    private final String command;
    private final String[] args;

    ParsedMessage(String prefix, String command, String[] args) {
      this.prefix = prefix;
      this.command = command;
      this.args = args;
    }

    static ParsedMessage parse(String[] prefixes, String message) {
      var splitted = message.split("\\s+");
      AtomicReference<String> prefix = new AtomicReference<>();
      List.of(prefixes).forEach(p -> {
        if (message.startsWith(p)) {
          prefix.set(p);
        }
      });
      var command = splitted[0].substring(prefix.get().length());
      var args = new String[splitted.length - 1];
      System.arraycopy(splitted, 1, args, 0, splitted.length - 1);
      return new ParsedMessage(prefix.get(), command, args);
    }
  }
}