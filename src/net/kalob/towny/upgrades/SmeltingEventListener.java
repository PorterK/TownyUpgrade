package net.kalob.towny.upgrades;

import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

public class SmeltingEventListener implements Listener {
    private Main plugin;

    public SmeltingEventListener(Main plugin) {
        this.plugin = plugin;
    }

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

        int cookTimeMod = (int) plugin.config.get(plugin.SMELTING_SPEED_PATH);
        int burnTimeMod = (int) plugin.config.get(plugin.SMELTING_EFFICIENCY_PATH);

        int cookTime = furnace.getCookTime() * cookTimeMod;
        int burnTime = furnace.getBurnTime() * burnTimeMod;

        if (hasSpeedBoost) {
            furnace.setCookTime((short) cookTime);
        }

        if (hasEfficiencyBoost) {
            furnace.setBurnTime((short) burnTime);
        }
    }

}
