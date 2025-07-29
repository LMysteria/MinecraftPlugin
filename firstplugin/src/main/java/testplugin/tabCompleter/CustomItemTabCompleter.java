package testplugin.tabCompleter;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;

public class CustomItemTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .toList();
        } else if (args.length == 2) {
            return List.of("customchest");
        } else if (args.length == 3) {
            return List.of("1", "2", "3", "4", "5");
        } else if (args.length == 4) {
            return List.of("1", "2", "3", "4", "5");
        }
        return List.of();
    }
}
