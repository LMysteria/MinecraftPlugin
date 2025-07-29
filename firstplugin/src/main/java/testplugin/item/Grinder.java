package testplugin.item;

import java.lang.reflect.AccessFlag.Location;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import testplugin.itemManager.GrinderManager;

public class Grinder {
    private Inventory inv;
    private List<UUID> currentPlayerOpen = new ArrayList<UUID>();
    
    public Grinder(Location loc, Inventory inv){
        this.inv = inv;
        GrinderManager.mapGrinder.put(loc, this);
        GrinderManager.invToGrinder.put(inv, this);
    }

    public Inventory getInventory(){
        return inv;
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
