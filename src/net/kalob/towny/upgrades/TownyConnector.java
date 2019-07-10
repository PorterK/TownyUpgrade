package net.kalob.towny.upgrades;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyWorld;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TownyConnector {
    public static Boolean MayorHasPermission(Main plugin, Block block, String permission) {
        int x = block.getX();
        int z = block.getZ();
        TownyWorld world = new TownyWorld(block.getWorld().getName());

        TownBlock townBlock = new TownBlock(x, z, world);

        Town town;
        Player mayor;

        try {
            town = townBlock.getTown();
        } catch (NotRegisteredException e) {
            return false;
        }

        mayor = plugin.getServer().getPlayer(town.getMayor().getName());

        if (mayor.hasPermission(permission)) {
            return true;
        }

        return false;
    }
}
