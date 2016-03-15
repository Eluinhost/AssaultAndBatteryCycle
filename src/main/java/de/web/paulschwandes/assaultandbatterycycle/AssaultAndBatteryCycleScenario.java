package de.web.paulschwandes.assaultandbatterycycle;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class AssaultAndBatteryCycleScenario {

    protected final Plugin plugin;
    protected final CycleManager manager;
    protected final Set<Listener> listeners;
    protected boolean enabled = false;

    public AssaultAndBatteryCycleScenario(Plugin plugin, CycleManager manager, Set<Listener> listeners) {
        this.plugin = plugin;
        this.manager = manager;
        this.listeners = ImmutableSet.copyOf(listeners);
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
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }

    protected void disable() {
        manager.cancelSwitchTask();
        for (Listener listener : listeners) {
            HandlerList.unregisterAll(listener);
        }
    }
}
