package de.web.paulschwandes.assaultandbatterycycle.commands;

import com.google.common.collect.ImmutableList;
import de.web.paulschwandes.assaultandbatterycycle.CycleManager;
import gg.uhc.uhc.flagcommands.commands.OptionCommand;
import gg.uhc.uhc.flagcommands.joptsimple.ArgumentAcceptingOptionSpec;
import gg.uhc.uhc.flagcommands.joptsimple.OptionSet;
import gg.uhc.uhc.flagcommands.joptsimple.OptionSpec;
import gg.uhc.uhc.flagcommands.tab.FixedValuesTabComplete;
import gg.uhc.uhc.modules.timer.TimeConverter;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public class ChangeDelayCommand extends OptionCommand {

    protected static final String CHANGED_DELAY_FORMAT =
            ChatColor.AQUA + "Cycle delay changed to %s seconds (by %s)";

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
        String message = String.format(CHANGED_DELAY_FORMAT, seconds, sender.getName());
        if (silent) {
            sender.sendMessage(message);
        } else {
            server.broadcastMessage(message);
        }
        return true;
    }
}
