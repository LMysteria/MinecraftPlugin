package testplugin.handler;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.io.File;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import testplugin.item.CustomChest;
import testplugin.itemManager.CustomChestManager;
import testplugin.util.BukkitSerialization;

public class CustomChestHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private File data;
    private Plugin plugin;

    public CustomChestHandler(Plugin plugin){
        this.plugin = plugin;
        File datafolder = plugin.getDataFolder();
        this.data = new File(datafolder.getPath().concat("data.json"));
    }
    
    public void saveCustomChest(){
        
        try {
            if(!CustomChestManager.mapCustomChest.isEmpty()){
                HashMap<String, String> serializeMap = new HashMap<String, String>();
                CustomChestManager.mapCustomChest.forEach((loc, chest) -> {
                    serializeMap.put(BukkitSerialization.serializeLocation(loc), BukkitSerialization.toBase64(chest.getCustomInventory()));
                });
                String dataString = objectMapper.writeValueAsString(serializeMap);
                plugin.getLogger().info(dataString);
                FileWriter writer = new FileWriter(data);
                writer.write(dataString);
                writer.close();
            }
        } catch (IOException e) {
            plugin.getLogger().info("Error in Test Dev Plugin Save Inventory");
            e.printStackTrace();
        }

    }

    public void loadCustomChest(){
        try {
            data.createNewFile();
            String dataString = Files.readString(data.toPath());
            if(!dataString.isEmpty()){
            TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String,String>>() {};
            HashMap<String, String> serializeMap = objectMapper.readValue(dataString, typeRef);
            serializeMap.forEach((loc, inv64) -> {
                try {
                    new CustomChest(BukkitSerialization.deserializeLocation(loc), BukkitSerialization.fromBase64(inv64));
                } catch (IOException e) {
                    plugin.getLogger().info("Error in parsing base64 inventory Test Dev Plugin");
                    e.printStackTrace();
                }
            });
            plugin.getLogger().info("Load RepEChest successfully");
        } else {
            plugin.getLogger().info("No existed data for RepEChest");
        }
        } catch (IOException e) {
            plugin.getLogger().info("Error in importing Test Dev Plugin");
            e.printStackTrace();
        }
    }

    public void openCustomChest(PlayerInteractEvent e){
        Player player = e.getPlayer();
        org.bukkit.Location loc = e.getClickedBlock().getLocation();
        e.setCancelled(true);
        CustomChest target = CustomChestManager.mapCustomChest.get(loc);
        target.addCurrentPlayerOpen(player.getUniqueId());
        player.openInventory(target.getCustomInventory());
    }
}
