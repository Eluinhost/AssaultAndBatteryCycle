/*
 The MIT License (MIT)
 Copyright (c) 2016 Paul Schwandes (paulschwandes@web.de)

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

package de.web.paulschwandes.assaultandbatterycycle.commands;

import com.google.common.collect.ImmutableList;
import de.web.paulschwandes.assaultandbatterycycle.CycleManager;
import gg.uhc.flagcommands.commands.OptionCommand;
import gg.uhc.flagcommands.joptsimple.ArgumentAcceptingOptionSpec;
import gg.uhc.flagcommands.joptsimple.OptionSet;
import gg.uhc.flagcommands.joptsimple.OptionSpec;
import gg.uhc.flagcommands.tab.FixedValuesTabComplete;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public class ChangeDelayCommand extends OptionCommand {

    protected static final String CHANGED_DELAY_FORMAT =
            ChatColor.AQUA + "Cycle delay changed to %s seconds";

    protected final Server server;
    protected final CycleManager manager;
    protected final ArgumentAcceptingOptionSpec<Long> timeSpec;
    protected final OptionSpec<Void> silentSpec;

    public ChangeDelayCommand(Server server, CycleManager manager) {
        this.server = server;
        this.manager = manager;
        timeSpec = parser.acceptsAll(ImmutableList.of("d", "newDelay"), "The new cycle delay")
                .withRequiredArg()
                .withValuesConvertedBy(new TimeConverter())
                .required();
        completers.put(timeSpec, new FixedValuesTabComplete("30s", "1m", "2m", "5m"));

        silentSpec = parser.acceptsAll(ImmutableList.of("s", "silent"), "Perform change silently");
    }

    @Override
    protected boolean runCommand(CommandSender sender, OptionSet optionSet) {
        long seconds = timeSpec.value(optionSet);
        long ticks = seconds * 20L;
        if (manager.isSwitchTaskRunning()) {
            manager.cancelSwitchTask();
            manager.setCycleSwitchDelay(ticks);
            manager.startSwitchTask();
        } else {
            manager.setCycleSwitchDelay(ticks);
        }

        boolean silent = optionSet.has(silentSpec);
        String message = String.format(CHANGED_DELAY_FORMAT, seconds);
        if (silent) {
            sender.sendMessage(message);
        } else {
            server.broadcastMessage(message);
        }
        return true;
    }
}
