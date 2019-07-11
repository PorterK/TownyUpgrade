package net.kalob.towny.upgrades;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;

public class MobEventListener implements Listener {
    private Main plugin;

    public MobEventListener(Main plugin) { this.plugin = plugin; }

    @EventHandler
    public void onSpawnerSpawn(SpawnerSpawnEvent e) {
        CreatureSpawner spawner = e.getSpawner();

        Boolean hasSpawnerBoost = TownyConnector.MayorHasPermission(plugin, e.getSpawner().getBlock(), plugin.MOB_SPAWN_RATE);

        int spawnRateMod = (int) plugin.config.get(plugin.MOB_SPAWN_RATE);

        int spawnRate = spawner.getSpawnCount() * spawnRateMod;

        if (hasSpawnerBoost) {
            spawner.setSpawnCount(spawnRate);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Block block = e.getEntity().getLocation().getBlock();
        Boolean hasExpBoost = TownyConnector.MayorHasPermission(plugin, block, plugin.MOB_EXP);

        int expMod = (int) plugin.config.get(plugin.MOB_EXP);
        int droppedExp = e.getDroppedExp() * expMod;

        if (hasExpBoost) {
            e.setDroppedExp(droppedExp);
        }
    }
}
