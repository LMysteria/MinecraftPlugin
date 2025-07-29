package testplugin.item;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import testplugin.itemManager.CustomChestManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomChest{
    private Inventory customInventory;
    private List<UUID> currentPlayerOpen = new ArrayList<UUID>();


    public CustomChest(Location loc, Inventory inv){
        customInventory = inv;
        CustomChestManager.mapCustomChest.put(loc, this);
        CustomChestManager.invToCustomChest.put(customInventory, this);
    }

    public Inventory getCustomInventory(){
        return customInventory;
    }

    public void addCurrentPlayerOpen(UUID uuid){
        currentPlayerOpen.add(uuid);
    }

    public void removeCurrentPlayerOpen(UUID uuid){
        currentPlayerOpen.remove(uuid);
    }

    public void closeAllPlayerOpen(){
        Iterator<UUID> interator = currentPlayerOpen.iterator();
        while (interator.hasNext()){
            UUID uuid = interator.next();
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.closeInventory();
                interator.remove();
            }
        }
    }
}
