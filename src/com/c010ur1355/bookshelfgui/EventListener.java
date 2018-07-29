package com.c010ur1355.bookshelfgui;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.BookMeta;

import java.util.HashMap;
import java.util.Map;

public class EventListener implements Listener {
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if (!e.isCancelled()){
            if (e.getBlockPlaced().getType() == Material.BOOKSHELF){
                Player player = e.getPlayer();
                Location location = e.getBlockPlaced().getLocation();

                Bookshelf bookshelf = new Bookshelf(player, location);

                if (bookshelf.isBookshelfExists()) bookshelf.removeBookshelf();

                bookshelf.createBookshelf("Bookshelf GUI", null);

                GUI gui = new GUI();

                gui.openBookshelfGUI(player, location, 0);

                if(map.containsKey(player)) map.replace(player,location);
                else                        map.put(player, location);
            }
        }
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e){
        if(!e.isCancelled()){
            if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                if(e.getClickedBlock().getType() == Material.BOOKSHELF){
                    if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.NAME_TAG)
                    {
                        Player player = e.getPlayer();
                        Location location = e.getClickedBlock().getLocation();

                        Bookshelf bookshelf = new Bookshelf(player, location);
                        bookshelf.classDeserialize();
                        if(bookshelf.isBookshelfExists()){
                            if(bookshelf.gui.owner.equals(player.getUniqueId().toString())
                                    || bookshelf.gui.password == null
                                    || player.hasPermission("bookshelfgui.view")){
                                new GUI().openBookshelfGUI(player, location, 0);
                                if(map.containsKey(player)) map.replace(player,location);
                                else                        map.put(player, location);
                            }else if(e.getPlayer().getInventory().getItemInMainHand() != null){
                                if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null){
                                    if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null){
                                        if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(bookshelf.gui.password)){
                                            new GUI().openBookshelfGUI(player, location, 0);
                                            if(map.containsKey(player)) map.replace(player,location);
                                            else                        map.put(player, location);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if(!e.isCancelled()){
            if(e.getBlock().getType() == Material.BOOKSHELF){
                Player player = e.getPlayer();
                Location location = e.getBlock().getLocation();

                Bookshelf bookshelf = new Bookshelf(player, location);
                if(bookshelf.isBookshelfExists()) bookshelf.removeBookshelf();
            }
        }
    }

    public Map<Player, Location> map = new HashMap<>();

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(!e.isCancelled()){
            String invName = e.getInventory().getName();
            if(invName.contains("§l§i§b§g§u§i")){
                if(e.getCurrentItem().getType() == Material.WRITTEN_BOOK){
                    if(invName.contains("§i§n§d§e§x")){                     //view
                        if(e.getClickedInventory() == e.getInventory()){
                            e.getWhoClicked().getInventory().addItem(e.getCurrentItem());
                        }
                    }else if(invName.contains("§m§o§d§i§f§y")){
                        Bookshelf bookshelf = new Bookshelf((Player)e.getWhoClicked(), map.get(e.getWhoClicked()));
                        if(e.getClickedInventory() == e.getInventory()){
                            //remove books and setting
                            bookshelf.removeBook(e.getSlot() % 9 + 3 * (int)(e.getSlot() / 9) - 3);
                            e.getWhoClicked().getInventory().addItem(e.getCurrentItem());
                            new GUI().openBookshelfGUI((Player)e.getWhoClicked(), map.get(e.getWhoClicked()), 1);
                        }else{
                            //add
                            bookshelf.addBook((BookMeta)e.getCurrentItem().getItemMeta());
                            new GUI().openBookshelfGUI((Player)e.getWhoClicked(), map.get(e.getWhoClicked()), 1);
                        }
                    }
                }

                //GUI navigator
                if(e.getClickedInventory() == e.getInventory()){
                    if(e.getCurrentItem().getType() == Material.BOOK){
                        //goto view mode
                        new GUI().openBookshelfGUI((Player)e.getWhoClicked(), map.get(e.getWhoClicked()), 0);
                    }else if(e.getCurrentItem().getType() == Material.BOOK_AND_QUILL){
                        //goto modify mode
                        new GUI().openBookshelfGUI((Player)e.getWhoClicked(), map.get(e.getWhoClicked()), 1);
                    }else if(e.getCurrentItem().getType() == Material.COMPASS){
                        //show command
                        e.getWhoClicked().sendMessage("Adjust setting commands:\n\n1.    /bsgui title [custom string]\n2.    /bsgui password [custom string]");
                        e.getWhoClicked().closeInventory();
                    }else if(e.getCurrentItem().getType() == Material.BARRIER){
                        //exit
                        e.getWhoClicked().closeInventory();
                    }
                }

                //cancel event
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
            }
        }
    }
}
