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

package bot.saveer.saveer.commands;

import bot.saveer.saveer.command.AbstractCommand;
import bot.saveer.saveer.command.ctx.CommandContext;

public class TestCommand extends AbstractCommand {

  public TestCommand() {
    super("Test", new String[] {"test"});
    addSubcommand(new TestSubCommand());
  }

  @Override
  public void execute(CommandContext ctx) {
    ctx.getTextChannel().sendMessage("test reply").queue();
  }

  static class TestSubCommand extends AbstractCommand {

    public TestSubCommand() {
      super("Subcommand", new String[] {"subcommand"});
    }

    @Override
    public void execute(CommandContext ctx) {
      ctx.getTextChannel().sendMessage("this is a subcommand").queue();
    }
  }
}
