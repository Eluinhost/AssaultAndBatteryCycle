package de.web.paulschwandes.assaultandbatterycycle;

import com.google.common.base.Preconditions;
import de.web.paulschwandes.assaultandbatterycycle.listeners.FilterDamageListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public class AssaultAndBatteryCycleScenario {

    protected final Plugin plugin;
    protected final CycleManager manager;
    protected final FilterDamageListener listener;
    protected boolean enabled = false;

    public AssaultAndBatteryCycleScenario(Plugin plugin, CycleManager manager, FilterDamageListener listener) {
        this.plugin = plugin;
        this.manager = manager;
        this.listener = listener;
    }

    public void setEnabled(boolean enabled) {
        Preconditions.checkState(this.enabled != enabled);
        this.enabled = enabled;
        if (enabled) {
            enable();
        } else {
            disable();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected void enable() {
        manager.startSwitchTask();
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    protected void disable() {
        manager.cancelSwitchTask();
        HandlerList.unregisterAll(listener);
    }
}
