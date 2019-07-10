package net.kalob.towny.upgrades;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Main extends JavaPlugin {
    FileConfiguration config = getConfig();
    String SMELTING_SPEED_PATH;
    String SMELTING_EFFICIENCY_PATH;
    String FLYING;
    String MOB_SPAWN_RATE;
    String MOB_EXP;
    String MOB_DROPS;
    String BLAST_RESISTANCE;
    String tag;

    private PluginManager pm = getServer().getPluginManager();

    @Override
    public void onEnable() {
        tag = String.format("%s[%sTownyUpgrade%s]%s:", ChatColor.RED, ChatColor.GREEN, ChatColor.RED, ChatColor.WHITE);

        getLogger().log(Level.INFO, String.format("%s Towny Upgrades initialized, building config...", tag));

        buildConfig();

        getLogger().log(Level.INFO, String.format("%s Default config built.", tag));

        pm.registerEvents(new SmeltingEventListener(this), this);
        pm.registerEvents(new PlayerEventListener(this), this);

    }

    /**
     *
     * Handles building all of the default config options.
     *
     */
    private void buildConfig() {
        config.options().header("Towny Upgrades Config");

        SMELTING_SPEED_PATH = buildPath("smelting.speed");
        SMELTING_EFFICIENCY_PATH = buildPath("smelting.efficiency");
        FLYING = buildPath("fly.enabled");
        MOB_SPAWN_RATE = buildPath("mobs.spawn_rate");
        MOB_EXP = buildPath("mobs.exp_mod");
        MOB_DROPS = buildPath("mobs.drop_mod");
        BLAST_RESISTANCE = buildPath("player.blast_resistance");

        config.addDefault(SMELTING_SPEED_PATH, "2");
        config.addDefault(SMELTING_EFFICIENCY_PATH, "2");
        config.addDefault(FLYING, "true");
        config.addDefault(MOB_SPAWN_RATE, "1.5");
        config.addDefault(MOB_EXP, "2");
        config.addDefault(MOB_DROPS, "2");
        config.addDefault(BLAST_RESISTANCE, "true");

        config.options().copyDefaults(true);

        saveConfig();
    }

    /**
     *
     * @param path The unique path of the current config item.
     *
     * @return Passed path appended to the base path for the config.
     *
     */
    private String buildPath(@NotNull String path) {
        String basePath = "towny-upgrades";

        return String.format("%s%s", basePath, path);
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, String.format("%s Towny upgrades disabled.", tag));
    }
}
