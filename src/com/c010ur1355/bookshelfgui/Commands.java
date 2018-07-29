package com.c010ur1355.bookshelfgui;

import net.minecraft.server.v1_12_R1.CommandExecute;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commands extends CommandExecute implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender instanceof Player){
            Player player = (Player)sender;
            Bookshelf bookshelf = new Bookshelf(player, player.getTargetBlock(null, 100).getLocation());

            if(bookshelf.isBookshelfExists()){
                bookshelf.classDeserialize();
                if(bookshelf.gui.owner.equalsIgnoreCase(player.getUniqueId().toString()) || player.hasPermission("bookshelfgui.modify")){
                    if(args.length >= 2){
                        if(args[0].equalsIgnoreCase("title") || args[0].equalsIgnoreCase("password")){
                            List<String> list = new ArrayList<String>(Arrays.asList(args));
                            list.remove(0);
                            String line = String.join(" ", String.join(" ", list.toArray(new String[0])));

                            bookshelf.modifyBookshelf(args[0], line);
                            player.sendMessage(ChatColor.GREEN + "Successfully modified bookshelf \"" + args[0] + "\" attribute to \"" + line + "\".");
                            return true;
                        }
                    }else{
                        player.sendMessage(ChatColor.RED + "The prerequisite of modifying is requiring at least 2 parameters likes below one.\n\n/bsgui title My Bookshelf!\n/bsgui password 12345");
                        return true;
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "You are not permitted to use other one's bookshelf.");
                    return true;
                }
            }else{
                player.sendMessage(ChatColor.RED + "This bookshelf hasn't been registered yet.");
                return true;
            }
        }else{
            sender.sendMessage(ChatColor.RED + "ON9 isn't permitted to use this command. ON9 IS FORBIDDEN");
        }
        return false;
    }
}
