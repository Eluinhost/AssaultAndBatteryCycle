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
