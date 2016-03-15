package de.web.paulschwandes.assaultandbatterycycle.commands;

import com.google.common.collect.ImmutableList;
import de.web.paulschwandes.assaultandbatterycycle.AssaultAndBatteryCycleScenario;
import gg.uhc.uhc.flagcommands.commands.OptionCommand;
import gg.uhc.uhc.flagcommands.joptsimple.OptionSet;
import gg.uhc.uhc.flagcommands.joptsimple.OptionSpec;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public class ToggleScenarioCommand extends OptionCommand {

    protected static final String SPECIFY_STATUS_MESSAGE =
            ChatColor.RED + "Specify either -e/-enable or -d/-disable";
    protected static final String SAME_STATUS_FORMAT =
            ChatColor.RED + "Scenario is already %s!";
    protected static final String STATUS_CHANGED_FORMAT =
            ChatColor.AQUA + "Assault and Battery Cycle is now %s! (by %s)";

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
        String message = String.format(STATUS_CHANGED_FORMAT, newStatusString, sender.getName());
        if (silent) {
            sender.sendMessage(message);
        } else {
            server.broadcastMessage(message);
        }
        return true;
    }
}
