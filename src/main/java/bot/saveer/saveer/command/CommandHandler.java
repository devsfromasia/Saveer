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
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

/**
 * Command handler handles parsing of incoming messages
 * and calling of the actual commands.
 */
public class CommandHandler {

  private static final String DEFAULT_PREFIX = "s!";
  private final Saveer saveer;
  private final CommandManager commandManager;

  public CommandHandler(final Saveer saveer, final CommandManager commandManager) {
    this.saveer = saveer;
    this.commandManager = commandManager;
  }

  /**
   * Handles created messages and checks them for valid commands.
   * <br>If the message is a valid command, the command get executed.
   *
   * @param event The {@link MessageReceivedEvent}.
   */
  @SubscribeEvent
  public void handleMessage(final MessageReceivedEvent event) {
    // Exclude bots and webhooks
    if (event.getAuthor().isBot() || event.getAuthor().isFake()) {
      return;
    }
    final var content = event.getMessage().getContentRaw();
    // Message does not start with prefix
    if (
        !content.startsWith(DEFAULT_PREFIX)
            && !content.startsWith(event.getJDA().getSelfUser().getAsMention())
    ) {
      return;
    }
    // TODO: 25.06.19 Fix mention as prefix
    final var parsed = ParsedMessage.parse(new String[] {
        DEFAULT_PREFIX, event.getJDA().getSelfUser().getAsMention()
    }, content);
    // Message just starts with prefix, no real command
    if (parsed.getCommand().equals("")) {
      return;
    }

    if (commandManager.getAliasCommands().containsKey(parsed.getCommand())) {
      callCommand(
          commandManager.getAliasCommands().get(parsed.getCommand()),
          parsed.getArgs(),
          event.getMessage()
      );
    }
  }

  private void callCommand(final Command command, final String[] args, final Message message) {
    if (args.length > 0) {
      final var subcommand = Arrays.stream(command.getSubcommands())
                                   .filter(sub -> List.of(sub.getAliases())
                                                      .contains(args[0]))
                                   .findAny();
      if (subcommand.isPresent()) {
        final var newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, args.length - 1);
        callCommand(subcommand.get(), newArgs, message);
        return;
      }
    }
    if (Arrays.stream(
        command.getBotPermissions())
              .anyMatch(permission ->
                  !message.getGuild()
                          .getSelfMember()
                          .hasPermission(message.getTextChannel(),
                              permission)
              )
    ) {
      // TODO: 25.06.19 Send no permission message (if possible)
      return;
    }
    if (
        command.isOwnerRestricted()
            && !List.of(saveer.getConfig().getOwners()).contains(message.getAuthor().getId())
    ) {
      // TODO: 25.06.19 Send no permission message
      return;
    }
    if (Arrays.stream(
        command.getUserPermissions())
              .anyMatch(permission ->
                  !Objects.requireNonNull(message.getGuild()
                                                 .getMember(message.getAuthor()))
                          .hasPermission(permission)
              )
    ) {
      // TODO: 25.06.19 Send no permission message
      return;
    }
    command.execute(new DefaultCommandContext(saveer, message, args));
  }

  public Saveer getSaveer() {
    return saveer;
  }

  public CommandManager getCommandManager() {
    return commandManager;
  }

  private static class ParsedMessage {

    private final String prefix;
    private final String command;
    private final String[] args;

    private ParsedMessage(final String prefix, final String command, final String... args) {
      this.prefix = prefix;
      this.command = command;
      this.args = args.clone();
    }

    private static ParsedMessage parse(final String[] prefixes, final String message) {
      final var splitted = message.split("\\s+");
      final AtomicReference<String> prefix = new AtomicReference<>();
      List.of(prefixes).forEach(s -> {
        if (message.startsWith(s)) {
          prefix.set(s);
        }
      });
      final var command = splitted[0].substring(prefix.get().length());
      final var args = new String[splitted.length - 1];
      System.arraycopy(splitted, 1, args, 0, splitted.length - 1);
      return new ParsedMessage(prefix.get(), command, args);
    }

    private String getPrefix() {
      return prefix;
    }

    private String getCommand() {
      return command;
    }

    private String[] getArgs() {
      return args;
    }
  }
}