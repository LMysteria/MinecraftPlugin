package testplugin.itemManager;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import testplugin.holder.CustomChestHolder;
import testplugin.item.CustomChest;
import testplugin.util.BukkitSerialization;

public class CustomChestManager {
    static public HashMap<Location, CustomChest> mapCustomChest = new HashMap<Location, CustomChest>();
    static public HashMap<Inventory, CustomChest> invToCustomChest = new HashMap<Inventory, CustomChest>();

        // (ItemStack) createCustomChestItem with Inventory
    static public ItemStack createCustomChestItem(Plugin plugin, Inventory inv){
        ItemStack item = new ItemStack(Material.BLACK_TERRACOTTA);
        ItemMeta meta = item.getItemMeta();
        Integer level = inv.getSize()/9;
        meta.setDisplayName(ChatColor.AQUA + "Custom Chest Lv."+level);
        meta.setLore(Arrays.asList(ChatColor.GRAY + "A Custom Made Chest Lv."+level));

        NamespacedKey key = CustomKeyManager.getCustomChestKey();
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, BukkitSerialization.toBase64(inv));

        item.setItemMeta(meta);
        return item;
    }

        // (ItemStack) createCustomChestItem with no Invetory
    static public ItemStack createCustomChestItem(Plugin plugin, Integer level){
        Inventory inv = Bukkit.createInventory(new CustomChestHolder(), 9*level, "Custom Chest");
        return createCustomChestItem(plugin, inv);
    }

    static public CustomChest findCustomChest(Inventory inventory){
        return invToCustomChest.get(inventory);
    }  

    static public void removeCustomChest(Inventory inventory){
        invToCustomChest.remove(inventory);
    }  
}