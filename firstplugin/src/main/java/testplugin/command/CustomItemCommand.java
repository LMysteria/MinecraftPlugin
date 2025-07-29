package testplugin.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.ChatColor;
import testplugin.itemManager.CustomChestManager;
public class CustomItemCommand implements CommandExecutor{
    final private JavaPlugin plugin;

    public CustomItemCommand(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
        if(args.length < 2 || args.length > 3){
            sender.sendMessage("Usage: /givecustomitem <player> <item> <level> [count]");
            return true;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if(player == null){
            sender.sendMessage(ChatColor.RED + "Player not found");
            return true;
        }

        String item = args[1];

        switch (item) {
            case "customchest":
                return giveCustomChest(sender, player, args);
        
            default:
                sender.sendMessage(ChatColor.RED + "Item not in plugin");
                return true;
        }
    }

    private boolean giveCustomChest(CommandSender sender, Player player, String[] args){
        int level;
        int count = 1;

        try {
            level = Integer.parseInt(args[2]);
            if(level > 6 || level < 1){
                sender.sendMessage(ChatColor.RED + "Level must be an integer from 1-6");
                return true;
            }             
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Level must be an integer from 1-6");
            return true;
        }

        if (args.length == 4) {
            try {   
                count = Integer.parseInt(args[3]);
                if(count < 1){
                    sender.sendMessage(ChatColor.RED + "Count must be a positive integer bigger than 0.");
                    return true;
                }    
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Count must be a positive integer bigger than 0.");
                return true;
            }
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack itemstack = CustomChestManager.createCustomChestItem(plugin, level);
        for(int i = 0; i < count; i++){
            inventory.addItem(itemstack.clone());
        }
        player.sendMessage("Gave " + count + " custom chest(s) level " + level + " to " + player.getName());
        return true;
    }
}
