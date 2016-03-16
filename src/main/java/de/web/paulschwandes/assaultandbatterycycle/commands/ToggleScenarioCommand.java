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
import de.web.paulschwandes.assaultandbatterycycle.AssaultAndBatteryCycleScenario;
import gg.uhc.flagcommands.commands.OptionCommand;
import gg.uhc.flagcommands.joptsimple.OptionSet;
import gg.uhc.flagcommands.joptsimple.OptionSpec;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public class ToggleScenarioCommand extends OptionCommand {

    protected static final String SAME_STATUS_FORMAT =
            ChatColor.RED + "Scenario is already %s!";
    protected static final String STATUS_CHANGED_FORMAT =
            ChatColor.AQUA + "Assault and Battery Cycle is now %s!";

    protected final AssaultAndBatteryCycleScenario scenario;
    protected final Server server;
    protected final boolean toState;
    protected final String toStateString;

    protected final OptionSpec<Void> silentSpec;

    public ToggleScenarioCommand(AssaultAndBatteryCycleScenario scenario, Server server, boolean toState) {
        this.scenario = scenario;
        this.server = server;

        this.toState = toState;
        this.toStateString = toState ? "enabled" : "disabled";

        silentSpec = parser.acceptsAll(ImmutableList.of("s", "silent"), "Perform toggle silently");
    }

    @Override
    protected boolean runCommand(CommandSender sender, OptionSet optionSet) {
        if (scenario.isEnabled() == toState) {
            String message = String.format(SAME_STATUS_FORMAT, toStateString);
            sender.sendMessage(message);
            return true;
        }

        // Change the scenario status
        scenario.setEnabled(toState);

        boolean silent = optionSet.has(silentSpec);
        String message = String.format(STATUS_CHANGED_FORMAT, toStateString);
        if (silent) {
            sender.sendMessage(message);
        } else {
            server.broadcastMessage(message);
        }
        return true;
    }
}
