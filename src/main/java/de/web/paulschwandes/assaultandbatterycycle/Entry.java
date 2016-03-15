package de.web.paulschwandes.assaultandbatterycycle;

import de.web.paulschwandes.assaultandbatterycycle.commands.ChangeDelayCommand;
import de.web.paulschwandes.assaultandbatterycycle.commands.ToggleScenarioCommand;
import de.web.paulschwandes.assaultandbatterycycle.listeners.FilterDamageListener;
import gg.uhc.uhc.flagcommands.commands.SubcommandCommand;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Entry extends JavaPlugin {

    public static final long DEFAULT_CYCLE_SWITCH_DELAY = 2*60*20L;

    @Override
    public void onEnable() {
        Server server = getServer();
        BukkitScheduler scheduler = server.getScheduler();

        CycleManager cycleManager = new CycleManager(this, scheduler, server, DEFAULT_CYCLE_SWITCH_DELAY);
        PvPDamagePredicate pvpDamagePredicate = new PvPDamagePredicate();
        FilterDamageListener listener = new FilterDamageListener(cycleManager, pvpDamagePredicate);
        AssaultAndBatteryCycleScenario scenario = new AssaultAndBatteryCycleScenario(this, cycleManager, listener);

        ChangeDelayCommand changeDelayCommand = new ChangeDelayCommand(server, cycleManager);
        ToggleScenarioCommand toggleScenarioCommand = new ToggleScenarioCommand(scenario, server);
        SubcommandCommand command = new SubcommandCommand();
        command.registerSubcommand("set", toggleScenarioCommand);
        command.registerSubcommand("delay", changeDelayCommand);
        getCommand("aabc").setExecutor(command);
    }
}
