package net.kalob.towny.upgrades;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerEventListener implements Listener {
    private Main plugin;

    PlayerEventListener (Main plugin) { this.plugin = plugin; }

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
        Boolean blastResistanceEnabled = Boolean.parseBoolean(plugin.config.get(plugin.BLAST_RESISTANCE).toString());

        if (blastResistanceEnabled && hasBlastResistance) {
            if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                double blastResistanceMod = (double) plugin.config.get(plugin.BLAST_RESISTANCE);

                double damage = e.getDamage() * blastResistanceMod;

                e.setDamage(damage);
            }
        }
    }
}
