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