package net.kalob.towny.upgrades;

import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SmeltingEventListener implements Listener {
    private Main plugin;

    SmeltingEventListener(Main plugin) { this.plugin = plugin; }

    /**
     *
     * Handles smelting event to modify smelting speed (cook time)
     * and efficiency (burn time).
     *
     * */
    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent e) {
        Furnace furnace = (Furnace) e.getBlock().getState();

        // The config paths are very simple, they'll work for our
        // permissions too.
        Boolean hasSpeedBoost = TownyConnector.MayorHasPermission(plugin, e.getBlock(), plugin.SMELTING_SPEED_PATH);
        Boolean hasEfficiencyBoost = TownyConnector.MayorHasPermission(plugin, e.getBlock(), plugin.SMELTING_EFFICIENCY_PATH);

        FurnaceHandler handler = new FurnaceHandler(plugin, furnace);

        if (hasSpeedBoost) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        Furnace updated = (Furnace) e.getBlock().getState();
                        handler.refreshFurnace(updated);

                        handler.setCookTime();

                        // Furnace is done burning, stop timer until next thing starts burning
                        if (updated.getBurnTime() == 0) {
                            cancel();
                        }
                    } catch (ClassCastException e) {
                        // Block either doesn't exist or something else has been placed there
                        cancel();
                    }

                }
            }.runTaskTimer(this.plugin, 0, 20);
        }

        if (hasEfficiencyBoost) {
            handler.setBurnTime(e);
        }
    }
}
