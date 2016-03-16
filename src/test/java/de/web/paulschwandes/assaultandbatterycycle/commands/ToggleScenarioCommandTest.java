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
import org.bukkit.command.CommandSender;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ToggleScenarioCommandTest {

    protected AssaultAndBatteryCycleScenario scenario;
    protected Server server;
    protected ToggleScenarioCommand command;
    protected CommandSender sender;
    protected Command bukkitCommand;

    @Before
    public void setUp() throws Exception {
        scenario = mock(AssaultAndBatteryCycleScenario.class);
        server = mock(Server.class);
        command = new ToggleScenarioCommand(scenario, server);
        sender = mock(CommandSender.class);
        bukkitCommand = mock(Command.class);
    }

    @Test
    public void testEnableScenario() throws Exception {
        when(scenario.isEnabled()).thenReturn(false);
        command.onCommand(sender, bukkitCommand, "", new String[]{"-e"});
        verify(scenario).setEnabled(true);
        verify(server).broadcastMessage(contains("enabled"));
    }

    @Test
    public void testDisableScenario() throws Exception {
        when(scenario.isEnabled()).thenReturn(true);
        command.onCommand(sender, bukkitCommand, "", new String[]{"-d"});
        verify(scenario).setEnabled(false);
        verify(server).broadcastMessage(contains("disabled"));
    }

    @Test
    public void testEnableSilently() throws Exception {
        when(scenario.isEnabled()).thenReturn(false);
        command.onCommand(sender, bukkitCommand, "", new String[]{"-es"});
        verify(scenario).setEnabled(true);
        verify(sender).sendMessage(contains("enabled"));
        verify(server, never()).broadcastMessage(anyString());
    }

    @Test
    public void testDisableSilently() throws Exception {
        when(scenario.isEnabled()).thenReturn(true);
        command.onCommand(sender, bukkitCommand, "", new String[]{"-ds"});
        verify(scenario).setEnabled(false);
        verify(sender).sendMessage(contains("disabled"));
        verify(server, never()).broadcastMessage(anyString());
    }
}