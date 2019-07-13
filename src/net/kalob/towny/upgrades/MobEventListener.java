package net.kalob.towny.upgrades;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class MobEventListener implements Listener {
    private Main plugin;

    MobEventListener(Main plugin) { this.plugin = plugin; }

    @EventHandler
    public void onSpawnerSpawn(SpawnerSpawnEvent e) {
        World world = e.getLocation().getWorld();

        Boolean hasSpawnerBoost = TownyConnector.MayorHasPermission(plugin, e.getSpawner().getBlock(), plugin.MOB_SPAWN_RATE);

        int spawnRateMod = Integer.parseInt(plugin.config.get(plugin.MOB_SPAWN_RATE).toString());

        if (hasSpawnerBoost) {
            for (int i = 1; i <= spawnRateMod - 1; i ++) {
                try {
                    world.spawnEntity(e.getLocation(), e.getEntityType());
                } catch (NullPointerException err) {
                    plugin.getLogger().log(Level.SEVERE, err.getMessage());
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Block block = e.getEntity().getLocation().getBlock();
        Boolean hasExpBoost = TownyConnector.MayorHasPermission(plugin, block, plugin.MOB_EXP);

        int expMod = Integer.parseInt(plugin.config.get(plugin.MOB_EXP).toString());
        int droppedExp = e.getDroppedExp() * expMod;

        if (hasExpBoost) {
            e.setDroppedExp(droppedExp);
        }

        // For 150% drops there is a 50% chance of a double drop
        boolean hasDoubleDrop = Math.random() > 0.5;
        boolean dropRateEnabled = Boolean.parseBoolean(plugin.config.get(plugin.MOB_DROPS).toString());

        if (dropRateEnabled && hasDoubleDrop) {
            List<ItemStack> drops = e.getDrops();

            List<ItemStack> extraDrops = new ArrayList<>();

            for (int i = 0; i < drops.size() - 1; i++) {
                extraDrops.add(drops.get(i));
            }

            drops.addAll(extraDrops);
        }
    }
}
