package testplugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import testplugin.event.CustomItemEvents;
import testplugin.itemManager.CustomKeyManager;
public class App extends JavaPlugin {
    private CustomItemEvents custom_chest;

    @Override
    public void onEnable() {
        CustomKeyManager.initialize(this);
        custom_chest = new CustomItemEvents(this);
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            custom_chest.saveCustomItem();
        }, 6000L, 6000L);
        getLogger().info("Test Dev Plugin Imported");
    }

    @Override
    public void onDisable(){
        custom_chest.saveCustomItem();
    }
}