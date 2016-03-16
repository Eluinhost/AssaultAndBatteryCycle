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

package de.web.paulschwandes.assaultandbatterycycle;

import com.google.common.collect.ImmutableSet;
import de.web.paulschwandes.assaultandbatterycycle.commands.ChangeDelayCommand;
import de.web.paulschwandes.assaultandbatterycycle.commands.ToggleScenarioCommand;
import de.web.paulschwandes.assaultandbatterycycle.listeners.FilterDamageListener;
import de.web.paulschwandes.assaultandbatterycycle.listeners.NoIPvPListener;
import gg.uhc.flagcommands.commands.SubcommandCommand;
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
