package de.web.paulschwandes.assaultandbatterycycle;

import com.google.common.collect.ImmutableSet;
import de.web.paulschwandes.assaultandbatterycycle.commands.ChangeDelayCommand;
import de.web.paulschwandes.assaultandbatterycycle.commands.ToggleScenarioCommand;
import de.web.paulschwandes.assaultandbatterycycle.listeners.FilterDamageListener;
import de.web.paulschwandes.assaultandbatterycycle.listeners.NoIPvPListener;
import gg.uhc.uhc.flagcommands.commands.SubcommandCommand;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Entry extends JavaPlugin {

    public static final long DEFAULT_CYCLE_SWITCH_DELAY = 2*60*20L;

    @Override
    public void onEnable() {
        Server server = getServer();
        BukkitScheduler scheduler = server.getScheduler();

        CycleManager cycleManager = new CycleManager(this, DEFAULT_CYCLE_SWITCH_DELAY);

        PvPDamageEventPredicate pvpDamageEventPredicate = new PvPDamageEventPredicate();
        IPvPMaterialPredicate iPvPMaterialPredicate = new IPvPMaterialPredicate();
        double disabledRange = 10;
        NoIPvPListener noIPvPListener = new NoIPvPListener(cycleManager, iPvPMaterialPredicate, disabledRange);
        FilterDamageListener filterDamageListener = new FilterDamageListener(cycleManager, pvpDamageEventPredicate);

        ImmutableSet<Listener> listeners = ImmutableSet.of(noIPvPListener, filterDamageListener);
        AssaultAndBatteryCycleScenario scenario = new AssaultAndBatteryCycleScenario(this, cycleManager, listeners);

        ChangeDelayCommand changeDelayCommand = new ChangeDelayCommand(server, cycleManager);
        ToggleScenarioCommand toggleScenarioCommand = new ToggleScenarioCommand(scenario, server);

        SubcommandCommand command = new SubcommandCommand();
        command.registerSubcommand("set", toggleScenarioCommand);
        command.registerSubcommand("delay", changeDelayCommand);
        getCommand("aabc").setExecutor(command);
    }
}
