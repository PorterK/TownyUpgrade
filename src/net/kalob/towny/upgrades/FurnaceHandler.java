package net.kalob.towny.upgrades;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlastFurnace;
import org.bukkit.block.Furnace;
import org.bukkit.block.Smoker;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.inventory.*;

import java.util.Iterator;
import java.util.logging.Level;

/*
    Heavily influenced by:
    https://github.com/Jikoo/EnchantableBlocks/blob/master/src/main/java/com/github/jikoo/enchantableblocks/block/EnchantableFurnace.java#L156
 */

class FurnaceHandler {
    private Main plugin;
    private Furnace furnace;
    private int cookTimeMod;
    private int burnTimeMod;

    FurnaceHandler(Main plugin, Furnace furnace) {
        this.plugin = plugin;
        this.furnace = furnace;

        Object cookTimeConfig = plugin.config.get(plugin.SMELTING_SPEED_PATH);
        Object burnTimeConfig = plugin.config.get(plugin.SMELTING_EFFICIENCY_PATH);

        if (cookTimeConfig != null) {
            cookTimeMod = Integer.parseInt(cookTimeConfig.toString());
        } else {
            cookTimeMod = 1;
        }

        if (burnTimeConfig != null) {
            burnTimeMod = Integer.parseInt(burnTimeConfig.toString());
        } else {
            burnTimeMod = 1;
        }
    }

    void refreshFurnace(Furnace furnace) { this.furnace = furnace; }

    private boolean isFrozen() {
        FurnaceInventory inv = furnace.getInventory();
        CookingRecipe recipe = getFurnaceRecipe(inv);

        if (recipe == null) {
            return true;
        }

        // Already smelting
        if (inv.getSmelting() == null) {
            return true;
        }

        if (inv.getResult() != null && inv.getResult().getAmount() == inv.getResult().getType().getMaxStackSize()) {
            return true;
        }

        if ((inv.getResult() != null && inv.getResult().getType() != Material.AIR)
                ||!(recipe instanceof BlastingRecipe) && !(furnace instanceof BlastFurnace)
                || !(recipe instanceof SmokingRecipe) && !(furnace instanceof Smoker)) {

            return false;
        }

        return false;
    }

    void setCookTime() {
        if (isFrozen()) return;

        CookingRecipe recipe = getFurnaceRecipe(furnace.getInventory());

        if (recipe == null) return;

        int modifiedCookTime = recipe.getCookingTime() / cookTimeMod;

        if (furnace.getCookTimeTotal() == modifiedCookTime) return;

        furnace.setCookTimeTotal(modifiedCookTime);
        furnace.update();
    }

    void setBurnTime(FurnaceBurnEvent e) {
        if (isFrozen()) return;

        int modifiedBurnTime = e.getBurnTime() * burnTimeMod;

        e.setBurnTime(modifiedBurnTime);
    }

    /*
        Copied from:

        https://github.com/Jikoo/EnchantableBlocks/blob/master/src/main/java/com/github/jikoo/enchantableblocks/block/EnchantableFurnace.java#L225
     */
    private static @Nullable CookingRecipe getFurnaceRecipe(FurnaceInventory inventory) {
        if (inventory.getSmelting() == null) {
            return null;
        }

        Iterator<Recipe> iterator = Bukkit.recipeIterator();
        CookingRecipe bestRecipe = null;
        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();
            if (inventory.getHolder() instanceof BlastFurnace) {
                if (!(recipe instanceof BlastingRecipe)) {
                    continue;
                }
            } else if (inventory.getHolder() instanceof Smoker) {
                if (!(recipe instanceof SmokingRecipe)) {
                    continue;
                }
            } else if (!(recipe instanceof FurnaceRecipe)) {
                continue;
            }

            CookingRecipe cookingRecipe = (CookingRecipe) recipe;

            if (cookingRecipe.getInputChoice().test(inventory.getSmelting())) {
                bestRecipe = cookingRecipe;
                break;
            }
        }

        return bestRecipe;

    }
}
