package testplugin.event;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import testplugin.item.CustomChest;
import testplugin.itemManager.CustomChestManager;
import testplugin.itemManager.CustomKeyManager;
import testplugin.tabCompleter.CustomItemTabCompleter;
import testplugin.util.BukkitSerialization;

import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import testplugin.command.CustomItemCommand;
import testplugin.handler.CustomChestHandler;
import testplugin.holder.CustomChestHolder;

public class CustomItemEvents implements Listener{
    private CustomChestHandler customChestHandler;
    private final JavaPlugin plugin;

    public CustomItemEvents(JavaPlugin plugin){
        this.plugin = plugin;
        this.customChestHandler = new CustomChestHandler(plugin);
        this.customChestHandler.loadCustomChest();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getCommand("givecustomitem").setExecutor(new CustomItemCommand(plugin));
        plugin.getCommand("givecustomitem").setTabCompleter(new CustomItemTabCompleter());
        plugin.getLogger().info("Test Dev Plugin Imported");
    }

    @EventHandler
    public void openCustomItemBlock(PlayerInteractEvent e){
        Player player = e.getPlayer();
        Location loc = e.getClickedBlock().getLocation();
        if((e.getAction() == Action.RIGHT_CLICK_BLOCK) & (player.isSneaking() == false)){
            if(CustomChestManager.mapCustomChest.containsKey(loc)){
                customChestHandler.openCustomChest(e);
            }
        }

    }

    @EventHandler
    public void closeCustomItemBlock(InventoryCloseEvent e){
        Inventory inv = e.getInventory();
        if(inv.getHolder() instanceof CustomChestHolder){
            CustomChest target = CustomChestManager.findCustomChest(inv);
            target.removeCurrentPlayerOpen(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void placeCustomItemBlock(BlockPlaceEvent e){
        ItemStack item = e.getItemInHand();
        if(!item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = CustomKeyManager.getCustomChestKey();

        if(meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)){
            try {
                Player player = e.getPlayer();
                Location loc = e.getBlock().getLocation();
                String inv64 = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
                Inventory inv = BukkitSerialization.fromBase64(inv64);

                new CustomChest(loc, inv);
                plugin.getLogger().info(player.getName() + "placed a custom chest at " + loc);
            } catch (Exception err) {
                plugin.getLogger().info("Error in placing CustomChest");
                err.printStackTrace();
            }

        }
    }

    @EventHandler
    public void breakCustomItemBlock(BlockBreakEvent e){
        Block block = e.getBlock();
        Location loc = block.getLocation();

        if(CustomChestManager.mapCustomChest.containsKey(loc)){
            e.setDropItems(false);
            Player player = e.getPlayer();

            plugin.getLogger().info(player.getName() + "break a custom chest at " + loc);
            CustomChest target = CustomChestManager.mapCustomChest.get(loc);
            target.closeAllPlayerOpen();
            CustomChestManager.mapCustomChest.remove(loc);
            CustomChestManager.removeCustomChest(target.getCustomInventory());
            block.getWorld().dropItemNaturally(loc, CustomChestManager.createCustomChestItem(plugin, target.getCustomInventory()));
        }
    }
    
    public void saveCustomItem(){
        plugin.getLogger().info("Saving Custom Chest Data");
        customChestHandler.saveCustomChest();
        plugin.getLogger().info("Custom Chest Data Saved");
    }    
}
