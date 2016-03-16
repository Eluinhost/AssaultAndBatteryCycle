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

    protected static final String SPECIFY_STATUS_MESSAGE =
            ChatColor.RED + "Specify either -e/-enable or -d/-disable";
    protected static final String SAME_STATUS_FORMAT =
            ChatColor.RED + "Scenario is already %s!";
    protected static final String STATUS_CHANGED_FORMAT =
            ChatColor.AQUA + "Assault and Battery Cycle is now %s!";

    protected final AssaultAndBatteryCycleScenario scenario;
    protected final Server server;
    protected final OptionSpec<Void> enableSpec;
    protected final OptionSpec<Void> disableSpec;
    protected final OptionSpec<Void> silentSpec;

    public ToggleScenarioCommand(AssaultAndBatteryCycleScenario scenario, Server server) {
        this.scenario = scenario;
        this.server = server;
        enableSpec = parser.acceptsAll(ImmutableList.of("e", "enable", "on"), "Enable the scenario");
        disableSpec = parser.acceptsAll(ImmutableList.of("d", "disable", "off"), "Disable the scenario");
        silentSpec = parser.acceptsAll(ImmutableList.of("s", "silent"), "Perform toggle silently");
    }

    @Override
    protected boolean runCommand(CommandSender sender, OptionSet optionSet) {
        boolean hasEnableSpec = optionSet.has(enableSpec);
        boolean hasDisableSpec = optionSet.has(disableSpec);
        if (hasDisableSpec == hasEnableSpec) {
            sender.sendMessage(SPECIFY_STATUS_MESSAGE);
            return true;
        }

        // For clarity
        // noinspection UnnecessaryLocalVariable
        boolean newStatus = hasEnableSpec;
        String newStatusString = newStatus ? "enabled" : "disabled";
        if (scenario.isEnabled() == newStatus) {
            String message = String.format(SAME_STATUS_FORMAT, newStatusString);
            sender.sendMessage(message);
            return true;
        }

        boolean silent = optionSet.has(silentSpec);
        scenario.setEnabled(newStatus);
        String message = String.format(STATUS_CHANGED_FORMAT, newStatusString);
        if (silent) {
            sender.sendMessage(message);
        } else {
            server.broadcastMessage(message);
        }
        return true;
    }
}
