package net.kalob.towny.upgrades;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.logging.Level;

public class MobEventListener implements Listener {
    private Main plugin;

    MobEventListener(Main plugin) { this.plugin = plugin; }

    @EventHandler
    public void onSpawnerSpawn(SpawnerSpawnEvent e) {
        World world = e.getLocation().getWorld();

        Boolean hasSpawnerBoost = TownyConnector.MayorHasPermission(plugin, e.getSpawner().getBlock(), plugin.MOB_SPAWN_RATE);

        double bonus = Utils.getBonusChance(plugin, plugin.MOB_SPAWN_RATE);
        int sure = Utils.getSureChance(plugin, plugin.MOB_SPAWN_RATE);

        boolean hasBonus = Math.random() < bonus;

        if (hasSpawnerBoost) {
            if (world == null) return;

            for (int i = 1; i <= sure; i ++) {
                world.spawnEntity(e.getLocation(), e.getEntityType());
            }

            if (hasBonus) {
                world.spawnEntity(e.getLocation(), e.getEntityType());
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Location dropLocation = e.getEntity().getLocation();
        World world = dropLocation.getWorld();
        Block block = dropLocation.getBlock();
        Boolean hasExpBoost = TownyConnector.MayorHasPermission(plugin, block, plugin.MOB_EXP);

        int expMod = Integer.parseInt(plugin.config.get(plugin.MOB_EXP).toString());
        int droppedExp = e.getDroppedExp() * expMod;

        if (hasExpBoost) {
            e.setDroppedExp(droppedExp);
        }


        Boolean hasDropBoost = TownyConnector.MayorHasPermission(plugin, block, plugin.MOB_DROPS);

        double bonus = Utils.getBonusChance(plugin, plugin.MOB_DROPS);
        int sure = Utils.getSureChance(plugin, plugin.MOB_DROPS);

        boolean hasBonus = Math.random() < bonus;

        /*
            This pattern works like this:
                -   If the mob drops are set to 1.5 there are 0 sure drops but there is a
                    50% chance of a "bonus drop".

                -   If the mob drops are set to 2 there is 1 sure drop (doubling the drops) and
                    there is a 0% chance of a "bonus drop"
         */
        if (hasDropBoost) {
            List<ItemStack> drops = e.getDrops();

            for (int i = 0; i < sure; i ++) {
                for (int j = 0; j < drops.size() - 1; j++) {
                    if (world != null) {
                        world.dropItemNaturally(dropLocation, drops.get(j));
                    }
                }
            }

            if (hasBonus) {
                for (int j = 0; j < drops.size() - 1; j++) {
                    if (world != null) {
                        world.dropItemNaturally(dropLocation, drops.get(j));
                    }
                }
            }
        }
    }
}
