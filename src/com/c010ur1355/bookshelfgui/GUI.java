package com.c010ur1355.bookshelfgui;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GUI{

    private main plugin = main.getPlugin(main.class);
    private String suffix1 = "§l§i§b§g§u§i§i§n§d§e§x";
    private String suffix2 = "§l§i§b§g§u§i§m§o§d§i§f§y";

    public void openBookshelfGUI(Player player, Location location, int type){
        //instantiate
        Bookshelf bookshelf = new Bookshelf(player, location);

        //create inventory
        Inventory inv;

        //load books from Bookshelf
        //check existence
        if(bookshelf.isBookshelfExists()){
            //deserialize
            bookshelf.classDeserialize();

            //instantiate
            inv = plugin.getServer().createInventory(player.getInventory().getHolder(), 27, bookshelf.gui.title + (type == 0 ? suffix1 : suffix2));

            //temp written book
            ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta meta = (BookMeta)item.getItemMeta();

            //opt
            var books = bookshelf.books.book;

            //set item with proper position
            for(int i = 0; i < books.size(); i++){
                //book meta
                meta.setTitle(books.get(i).title);
                meta.setAuthor(books.get(i).author);
                meta.setPages(books.get(i).pages.split("<br/>"));

                //apply meta
                item.setItemMeta(meta);

                //set display name
                item.getItemMeta().setDisplayName(books.get(i).title);

                //set item
                inv.setItem(books.get(i).pos % 3 + (int)(books.get(i).pos / 3) * 9 + 3, item);
            }
        }else{
            //instantiate
            inv = plugin.getServer().createInventory(player.getInventory().getHolder(), 27, "Bookshelf GUI" + (type == 0 ? suffix1 : suffix2));

            //create bookshelf in configuration when clicked bookshelf was not been record in configuration.
            bookshelf.createBookshelf("Bookshelf GUI", null);

            //deserialize
            bookshelf.classDeserialize();
        }

        //generate column
        //generate item stack

        //green glass
        ItemStack i1 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13);
        //red glass
        ItemStack i2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);

        //get item meta
        ItemMeta m1 = i1.getItemMeta();
        ItemMeta m2 = i2.getItemMeta();

        //set meta display name.
        m1.setDisplayName("COLUMN§l§i§b§g§u§i");
        m2.setDisplayName("COLUMN§l§i§b§g§u§i");

        //create private lore
        ArrayList<String> list = new ArrayList<String>();
        list.add("Bookshelf column.");
        m1.setLore(list);
        m2.setLore(list);

        //set item meta
        i1.setItemMeta(m1);
        i2.setItemMeta(m2);

        for(int i = 0; i < 27; i++)
            if(i % 9 == 0 || i % 9 == 2 || i % 9 == 6 || i % 9 == 8)
                inv.setItem(i, i1);
            else if(i % 9 == 1 || i % 9 == 7)
                inv.setItem(i, i2);

        //exit button
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("EXIT");
        item.setItemMeta(meta);
        inv.setItem(25, item);

        //owner and admin panel
        if(player.getUniqueId().toString().equals(bookshelf.gui.owner)
        || player.hasPermission("bookshelfgui.modify")){
            ItemStack i3 = new ItemStack(Material.BOOK);
            ItemStack i4 = new ItemStack(Material.BOOK_AND_QUILL);
            ItemStack i5 = new ItemStack(Material.COMPASS);

            ItemMeta m3 = i1.getItemMeta();
            ItemMeta m4 = i2.getItemMeta();
            ItemMeta m5 = i3.getItemMeta();

            m3.setDisplayName("View books.");
            m4.setDisplayName("Modify books.");
            m5.setDisplayName("Adjust bookshelf setting.");

            i3.setItemMeta(m3);
            i4.setItemMeta(m4);
            i5.setItemMeta(m5);

            if(type != 0)   inv.setItem(1, i3);
            else            inv.setItem(10, i4);

            inv.setItem(19, i5);
        }

        //open bookshelf gui
        player.openInventory(inv);
    }
}
