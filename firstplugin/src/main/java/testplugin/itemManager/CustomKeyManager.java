package testplugin.itemManager;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public class CustomKeyManager {
    private static NamespacedKey customChestKey;
    private static NamespacedKey grinderKey;

    public static void initialize(Plugin plugin){
        customChestKey = new NamespacedKey(plugin, "custom_chest");
        grinderKey = new NamespacedKey(plugin, "grinder");
    }

    public static NamespacedKey getCustomChestKey(){
        return customChestKey;
    }

    public static NamespacedKey getGrinderKey(){
        return grinderKey;
    }
}
