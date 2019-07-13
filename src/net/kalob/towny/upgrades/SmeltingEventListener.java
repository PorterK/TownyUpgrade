package net.kalob.towny.upgrades;

import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
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
    public void onFurnaceSmelt(FurnaceSmeltEvent e) {
        Furnace furnace = (Furnace) e.getBlock().getState();

        // The config paths are very simple, they'll work for our
        // permissions too.
        Boolean hasSpeedBoost = TownyConnector.MayorHasPermission(plugin, e.getBlock(), plugin.SMELTING_SPEED_PATH);
        Boolean hasEfficiencyBoost = TownyConnector.MayorHasPermission(plugin, e.getBlock(), plugin.SMELTING_EFFICIENCY_PATH);

        int cookTimeMod = Integer.parseInt(plugin.config.get(plugin.SMELTING_SPEED_PATH).toString());
        int burnTimeMod = Integer.parseInt(plugin.config.get(plugin.SMELTING_EFFICIENCY_PATH).toString());

        // We want cook time to be shorter...
        int cookTime = furnace.getCookTime() / cookTimeMod;

        // But we want burn time to be longer
        int burnTime = furnace.getBurnTime() * burnTimeMod;

        if (hasSpeedBoost) {
            furnace.setCookTime((short) cookTime);
        }

        if (hasEfficiencyBoost) {
            furnace.setBurnTime((short) burnTime);
        }

        furnace.update();
    }
}
