package de.web.paulschwandes.assaultandbatterycycle;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class CycleManager {

    protected static final String BROADCAST_SWITCH_FORMAT = ChatColor.RED + "Cycle has switched to %s!";

    protected final Plugin plugin;
    protected Cycle cycle = Cycle.MELEE;
    protected long cycleSwitchDelay;
    protected Optional<BukkitTask> switchTask = Optional.absent();

    public CycleManager(Plugin plugin, long cycleSwitchDelay) {
        this.plugin = plugin;
        this.cycleSwitchDelay = cycleSwitchDelay;
    }

    public Cycle getCurrentCycle() {
        return cycle;
    }

    public long getCycleSwitchDelay() {
        return cycleSwitchDelay;
    }

    public void setCycleSwitchDelay(long cycleSwitchDelay) {
        Preconditions.checkState(!isSwitchTaskRunning());
        this.cycleSwitchDelay = cycleSwitchDelay;
    }

    public boolean isSwitchTaskRunning() {
        return switchTask.isPresent();
    }

    public void startSwitchTask() {
        Preconditions.checkState(!switchTask.isPresent());
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, new SwitchTask(), cycleSwitchDelay, cycleSwitchDelay);
        switchTask = Optional.of(task);
    }

    public void cancelSwitchTask() {
        Preconditions.checkState(switchTask.isPresent());
        BukkitTask task = switchTask.get();
        task.cancel();
        switchTask = Optional.absent();
    }

    public class SwitchTask implements Runnable {
        @Override
        public void run() {
            cycle = cycle.next();
            String message = String.format(BROADCAST_SWITCH_FORMAT, cycle.name());
            Bukkit.broadcastMessage(message);
        }
    }
}
