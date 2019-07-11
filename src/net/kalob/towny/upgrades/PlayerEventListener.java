package net.kalob.towny.upgrades;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerEventListener implements Listener {
    private Main plugin;

    public PlayerEventListener (Main plugin) { this.plugin = plugin; }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e) {
        Boolean hasPermission = TownyConnector.MayorHasPermission(plugin, e.getTo().getBlock(), plugin.FLYING);
        Boolean hasFlyPermission = e.getPlayer().hasPermission("simplefly.fly");

        // If the user has the ability the fly anyways we aren't going to pop them into or out of fly mode
        if (hasFlyPermission) return;

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

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        Player player;

        if (e.getEntity() instanceof Player) {
            player = (Player) e.getEntity();
        } else {
            return;
        }

        Block block = player.getLocation().getBlock();

        Boolean hasBlastResistance = TownyConnector.MayorHasPermission(plugin, block, plugin.BLAST_RESISTANCE);

        if (hasBlastResistance) {
            if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                double blastResistanceMod = (double) plugin.config.get(plugin.BLAST_RESISTANCE);

                double damage = e.getDamage() * blastResistanceMod;

                e.setDamage(damage);
            }
        }
    }
}
