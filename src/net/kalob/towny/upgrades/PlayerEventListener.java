package net.kalob.towny.upgrades;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerEventListener implements Listener {
    private Main plugin;

    public PlayerEventListener (Main plugin) { this.plugin = plugin; }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e) {
        Boolean hasPermission = TownyConnector.MayorHasPermission(plugin, e.getTo().getBlock(), plugin.FLYING);
        if (hasPermission) {
            e.getPlayer().setAllowFlight(true);

            String message = String.format("%s Fly mode enabled.", plugin.tag);

            e.getPlayer().sendMessage(message);
        } else {
            e.getPlayer().setAllowFlight(false);

            String message = String.format("%s Fly mode disabled.", plugin.tag);

            e.getPlayer().sendMessage(message);
        }

    }
}
