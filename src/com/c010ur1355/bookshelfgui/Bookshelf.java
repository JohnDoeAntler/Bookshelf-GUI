package com.c010ur1355.bookshelfgui;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;

public class Bookshelf{

    public class GUI{
        public String title;
        public String owner;
        public String password;
    }

    public class Books{
        public ArrayList<Book> book;
    }

    public class Book{
        public int pos;
        public String title;
        public String author;
        public String pages;
    }

    private main plugin = main.getPlugin(main.class);
    private Player player;
    private Location location;

    public String hash;
    public GUI gui;
    public Books books;

    //constructor
    public Bookshelf(Player player, Location location){
        hash = String.format("%s %s %s %s",
                player.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ());

        this.player = player;
        this.location = location;
    }

    //class deserialize
    public Bookshelf classDeserialize(){
        FileConfiguration config = plugin.getConfig();

        //initialize
        gui = new GUI();
        books = new Books();
        books.book = new ArrayList<>();

        //gui
        gui.title = config.getString("Bookshelf." + hash + ".gui.title");
        gui.owner = config.getString("Bookshelf." + hash + ".gui.owner");
        gui.password = config.getString("Bookshelf." + hash + ".gui.password");

        //books
        for(int i = 0; i < 9; i++)
            if(config.get("Bookshelf." + hash + ".books.book_" + i) != null){
                Book book = new Book();

                book.pos = i;
                book.title = config.getString("Bookshelf." + hash + ".books.book_" + i + ".title");
                book.author = config.getString("Bookshelf." + hash + ".books.book_" + i + ".author");
                book.pages = config.getString("Bookshelf." + hash + ".books.book_" + i + ".pages");

                books.book.add(book);
            }

        return this;
    }

    //configuration modification
    public Bookshelf createBookshelf(String title, String password){
        plugin.getConfig().set("Bookshelf." + hash + ".gui.title", title);
        plugin.getConfig().set("Bookshelf." + hash + ".gui.owner", player.getUniqueId().toString());
        plugin.getConfig().set("Bookshelf." + hash + ".gui.password", password);
        plugin.saveConfig();

        return this;
    }

    public Bookshelf modifyBookshelf(String attribute, String value){
        plugin.getConfig().set("Bookshelf." + hash + ".gui." + attribute, value);
        plugin.saveConfig();

        return this;
    }

    public void removeBookshelf(){
        plugin.getConfig().set("Bookshelf." + hash, null);
        plugin.saveConfig();
    }

    public Bookshelf addBook(BookMeta book){
        FileConfiguration config = plugin.getConfig();

        for(int i = 0; i < 9; i++)
            if(config.get("Bookshelf." + hash + ".books.book_" + i) == null){
                config.set("Bookshelf." + hash + ".books.book_" + i + ".title", book.getTitle());
                config.set("Bookshelf." + hash + ".books.book_" + i + ".author", book.getAuthor());
                config.set("Bookshelf." + hash + ".books.book_" + i + ".pages", String.join("<br/>", book.getPages()));
                plugin.saveConfig();

                break;
            }
        return this;
    }

    public Bookshelf removeBook(int num){
        plugin.getConfig().set("Bookshelf." + hash + ".books.book_" + num, null);
        plugin.saveConfig();

        return this;
    }

    //get Bookshelf existence in configuration
    public boolean isBookshelfExists(){
        return plugin.getConfig().get("Bookshelf." + hash + ".gui.title") != null;
    }
}
