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

import de.web.paulschwandes.assaultandbatterycycle.AssaultAndBatteryCycleScenario;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ToggleScenarioCommandTest {

    protected AssaultAndBatteryCycleScenario scenario;
    protected Server server;
    protected CommandExecutor command;
    protected CommandSender sender;
    protected Command bukkitCommand;

    @Before
    public void setUp() throws Exception {
        scenario = mock(AssaultAndBatteryCycleScenario.class);
        server = mock(Server.class);
        sender = mock(CommandSender.class);
        bukkitCommand = mock(Command.class);
    }

    protected void initCommand(boolean toEnable) {
        command = new ToggleScenarioCommand(scenario, server, toEnable);
    }

    protected void triggerCommand(String... args) {
        command.onCommand(sender, bukkitCommand, "", args);
    }

    @Test
    public void testEnableScenario() throws Exception {
        initCommand(true);
        when(scenario.isEnabled()).thenReturn(false);

        triggerCommand();

        verify(scenario, times(1)).setEnabled(true);
        verify(server, times(1)).broadcastMessage(contains("enabled"));
        verify(sender, never()).sendMessage(anyString());
    }

    @Test
    public void testDisableScenario() throws Exception {
        initCommand(false);
        when(scenario.isEnabled()).thenReturn(true);

        triggerCommand();

        verify(scenario, times(1)).setEnabled(false);
        verify(server, times(1)).broadcastMessage(contains("disabled"));
        verify(sender, never()).sendMessage(anyString());
    }

    @Test
    public void testEnableSilently() throws Exception {
        initCommand(true);
        when(scenario.isEnabled()).thenReturn(false);

        triggerCommand("-s");

        verify(scenario, times(1)).setEnabled(true);
        verify(sender, times(1)).sendMessage(contains("enabled"));
        verify(server, never()).broadcastMessage(anyString());
    }

    @Test
    public void testDisableSilently() throws Exception {
        initCommand(false);
        when(scenario.isEnabled()).thenReturn(true);

        triggerCommand("-s");

        verify(scenario, times(1)).setEnabled(false);
        verify(sender, times(1)).sendMessage(contains("disabled"));
        verify(server, never()).broadcastMessage(anyString());
    }

    @Test
    public void testAlreadyEnabled() throws Exception {
        initCommand(true);
        when(scenario.isEnabled()).thenReturn(true);

        triggerCommand();

        verify(scenario, never()).setEnabled(anyBoolean());
        verify(sender, times(1)).sendMessage(contains("is already enabled"));
    }

    @Test
    public void testAlreadyDisabled() throws Exception {
        initCommand(false);
        when(scenario.isEnabled()).thenReturn(false);

        triggerCommand();

        verify(scenario, never()).setEnabled(anyBoolean());
        verify(sender, times(1)).sendMessage(contains("is already disabled"));
    }

    @Test
    public void testAlreadyEnabledSilent() throws Exception {
        initCommand(true);
        when(scenario.isEnabled()).thenReturn(true);

        triggerCommand("-s");

        verify(scenario, never()).setEnabled(anyBoolean());
        verify(sender, times(1)).sendMessage(contains("is already enabled"));
    }

    @Test
    public void testAlreadyDisabledSilent() throws Exception {
        initCommand(false);
        when(scenario.isEnabled()).thenReturn(false);

        triggerCommand("-s");

        verify(scenario, never()).setEnabled(anyBoolean());
        verify(sender, times(1)).sendMessage(contains("is already disabled"));
    }
}