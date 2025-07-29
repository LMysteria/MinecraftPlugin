package testplugin.itemManager;

import java.lang.reflect.AccessFlag.Location;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import testplugin.item.Grinder;

public class GrinderManager {
    static public HashMap<Location, Grinder> mapGrinder = new HashMap<Location, Grinder>();
    static public HashMap<Inventory, Grinder> invToGrinder = new HashMap<Inventory, Grinder>();

            // (ItemStack) createCustomChestItem with Inventory
    static public ItemStack createGrinderItem(Plugin plugin){
        ItemStack item = new ItemStack(Material.BROWN_TERRACOTTA);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Grinder");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "A Grinder"));

        NamespacedKey key = CustomKeyManager.getGrinderKey();
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);

        item.setItemMeta(meta);
        return item;
    }

    static public Grinder findGrinder(Inventory inventory){
        return invToGrinder.get(inventory);
    }  

    static public void removeCustomChest(Inventory inventory){
        invToGrinder.remove(inventory);
    }  
}
