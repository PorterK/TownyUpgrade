package net.kalob.towny.upgrades;

import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;

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
    public void onFurnaceSmelt(FurnaceBurnEvent e) {
        Furnace furnace = (Furnace) e.getBlock().getState();

        // The config paths are very simple, they'll work for our
        // permissions too.
        Boolean hasSpeedBoost = TownyConnector.MayorHasPermission(plugin, e.getBlock(), plugin.SMELTING_SPEED_PATH);
        Boolean hasEfficiencyBoost = TownyConnector.MayorHasPermission(plugin, e.getBlock(), plugin.SMELTING_EFFICIENCY_PATH);

        FurnaceHandler handler = new FurnaceHandler(plugin, furnace);

        if (hasSpeedBoost) {
            handler.setCookTime();
        }

        if (hasEfficiencyBoost) {
            handler.setBurnTime(e);
        }
    }
}
