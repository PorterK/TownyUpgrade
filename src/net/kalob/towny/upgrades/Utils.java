package net.kalob.towny.upgrades;

class Utils {
    private static Object configItem(Main plugin, String path) {
        Object configItem = plugin.config.get(path);

        if (configItem == null) {
            throw new NullPointerException(String.format("Config path %s not found.", path));
        }

        return configItem;
    }
    /**
     * Gets a double from a config value
     *
     * Just a layer of abstraction to reduce code duplication.
     *
     * @param plugin The main plugin
     * @param configPath Path to the config option
     * @return double The double value of the config option
     */
    static double doubleFromConfig (Main plugin, String configPath) {
        Object configItem = configItem(plugin, configPath);

        String rawNumber = configItem.toString();

        return Double.parseDouble(rawNumber);
    }

    /**
     *
     * @param plugin The main plugin
     * @param configPath Path to the config option
     * @return booleam The boolean value of the config option
     */
    static boolean boolFromConfig (Main plugin, String configPath) {
       Object configItem = configItem(plugin, configPath);

       String rawBool = configItem.toString();

       return Boolean.parseBoolean(rawBool);
    }

    /**
     * Gets chance of bonus happening.
     *
     * i.e If we 2.5x mob spawns, there is a 100% chance of the spawn
     * doubling, and a 50% chance of the spawn tripling.
     *
     * @param plugin The main plugin
     * @param configPath Path to the config option
     * @return int Chance of a bonus happening
     */
    static double getBonusChance(Main plugin, String configPath) {
        double number = doubleFromConfig(plugin, configPath);

        return number % 1;
    }

    /**
     * Gets sure chance multiplier
     *
     * i.e If we 2.5x mob spawns, there is a 100% chance of a
     * bonus spawn happening, i.e "Sure chance".
     *
     * @param plugin The main plugin
     * @param configPath Path to the config option
     * @return int Sure chance.
     */
    static int getSureChance(Main plugin, String configPath) {
        double number = doubleFromConfig(plugin, configPath);

        return (int) number - 1;
    }
}
