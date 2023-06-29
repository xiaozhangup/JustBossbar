/*
 *  Copyright (C) <2022> <XiaoMoMi>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.momirealms.customnameplates.command.subcmd;

import net.momirealms.customnameplates.command.AbstractSubCommand;
import net.momirealms.customnameplates.utils.AdventureUtils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCommand extends AbstractSubCommand {

    public static final AbstractSubCommand INSTANCE = new HelpCommand();

    public HelpCommand() {
        super("help");
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> args) {
        AdventureUtils.sendMessage(sender, "<#3CB371>/justbossbar");
        AdventureUtils.sendMessage(sender, "  <gray>├─<white>help");
        AdventureUtils.sendMessage(sender, "  <gray>├─<white>reload");
        return true;
    }
}
