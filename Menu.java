import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class Menu extends JMenuBar
{
    private class MenuItem extends JCheckBoxMenuItem
    {
        public Deck deck;
        
        public MenuItem(String description)
        {
            super(description);
        }
    }
    
    private class Submenu extends JMenu
    {
        MenuItem all;
        ArrayList<MenuItem> items;
        
        public Submenu(String description)
        {
            super(description);
        }
        
        public void loadSubmenu(String menuFilename, ActionListener listener)
        {
            Scanner scan = null;
            items = new ArrayList<MenuItem>();
            
            // Add "All" menu item.
            all = new MenuItem("All");
            all.deck = new Deck();
            all.setActionCommand(Game.COMMAND_MENU); // bleh
            all.addActionListener(listener);
            add(all);
            
            try
            {
                //scan = new Scanner(new FileReader(menuFilename));
                scan = new Scanner(getClass().getResourceAsStream(menuFilename), "UTF-8");
                
                while (true)
                {
                    if (!scan.hasNextLine())
                        break;
                    
                    String description = scan.nextLine();
                    String filename = scan.nextLine();
                    
                    MenuItem item = new MenuItem(description);
                    item.deck = new Deck();
                    item.deck.loadDeck(filename);
                    item.setActionCommand(Game.COMMAND_MENU); // bleh
                    item.addActionListener(listener);
                    items.add(item);
                    add(item);
                    
                    // Add items to the "All" deck.
                    all.deck.addDeck(item.deck);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                // eh, oh well
            }
            finally
            {
                if (scan != null)
                    scan.close();
            }
        }
        
        /*public boolean hasSelected()
        {
            if (all.getState())
                return true;
            for (MenuItem item : items)
                if (item.getState())
                    return true;
            
            return false;
        }*/
        
        public Deck getSelectedDecks()
        {
            Deck deck = new Deck();
            
            if (all.getState())
            {
                deck.addDeck(all.deck);
                
                // Grey out other items.
                for (MenuItem item : items)
                    item.setEnabled(false);
            }
            else
            {
                for (MenuItem item : items)
                {
                    item.setEnabled(true);
                    
                    if (item.getState())
                        deck.addDeck(item.deck);
                }
            }
            
            //deck.shuffle();
            
            return deck;
        }
    }
    
    //private ArrayList<MenuItem> items;
    private ArrayList<Submenu> submenus;
    
    public void loadMenu(String menuFilename, ActionListener listener)
    {
        Scanner scan = null;
        submenus = new ArrayList<Submenu>();
        JMenu menu = new JMenu("Deck");
        add(menu);
        
        try
        {
            //scan = new Scanner(new FileReader(menuFilename));
            scan = new Scanner(getClass().getResourceAsStream(menuFilename), "UTF-8");
            
            while (true)
            {
                if (!scan.hasNextLine())
                    break;
                
                String description = scan.nextLine();
                String filename = scan.nextLine();
                
                Submenu submenu = new Submenu(description);
                submenu.loadSubmenu(filename, listener);
                submenus.add(submenu);
                menu.add(submenu);
                
                /*MenuItem item = new MenuItem(description);
                item.deck = new Deck();
                item.deck.loadDeck(filename);
                item.setActionCommand(Game.COMMAND_MENU); // bleh
                item.addActionListener(listener);
                menu.add(item);
                items.add(item);*/
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            // eh, oh well
        }
        finally
        {
            if (scan != null)
                scan.close();
        }
    }
    
    public Deck getSelectedDecks()
    {
        Deck deck = new Deck();
        
        for (Submenu menu : submenus)
            deck.addDeck(menu.getSelectedDecks());
        
        deck.shuffle();
        
        return deck;
    }
}
