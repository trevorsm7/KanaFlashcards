import javax.swing.*;
import java.security.*;
import java.awt.*;
import java.awt.event.*;
import java.util.zip.*;
import java.util.*;
import java.net.*;
import java.io.*;

public class Menu extends JMenuBar
{
    private JCheckBoxMenuItem hiragana;
    private JCheckBoxMenuItem katakana;
    private JCheckBoxMenuItem dakuten;
    private JCheckBoxMenuItem youon;
    private JCheckBoxMenuItem allSets;
    private ArrayList<MenuItem> items;
    
    public void loadMenu(ActionListener listener)
    {
        JMenu menu = new JMenu("Options");
        add(menu);
        
        // Add "Hiragana" menu item.
        hiragana = new JCheckBoxMenuItem("Hiragana");
        hiragana.setActionCommand(Game.COMMAND_MENU);
        hiragana.addActionListener(listener);
        hiragana.setState(true); // set by default
        menu.add(hiragana);
        
        // Add "Katakana" menu item.
        katakana = new JCheckBoxMenuItem("Katakana");
        katakana.setActionCommand(Game.COMMAND_MENU);
        katakana.addActionListener(listener);
        menu.add(katakana);
        
        // Separate Hiragana/Katakana and Dakuten/Youon options.
        menu.addSeparator();
        
        // Add "Dakuten" menu item.
        dakuten = new JCheckBoxMenuItem("Dakuten");
        dakuten.setActionCommand(Game.COMMAND_MENU);
        dakuten.addActionListener(listener);
        dakuten.setState(true); // set by default
        menu.add(dakuten);
        
        // Add "Youon" menu item.
        youon = new JCheckBoxMenuItem("Youon");
        youon.setActionCommand(Game.COMMAND_MENU);
        youon.addActionListener(listener);
        youon.setState(true); // set by default
        menu.add(youon);
        
        // Add separator before character sets.
        menu.addSeparator();
        
        // Add "All Sets" menu item.
        allSets = new JCheckBoxMenuItem("All Sets");
        allSets.setActionCommand(Game.COMMAND_MENU);
        allSets.addActionListener(listener);
        allSets.setState(true); // set by default
        menu.add(allSets);
        
        items = new ArrayList<MenuItem>();
        try
        {
            // Get the CodeSource object for this class.
            CodeSource src = getClass().getProtectionDomain().getCodeSource();
            
            if (src != null)
            {
                // Get a ZipInputStream object for the enclosing JAR file.
                URL jar = src.getLocation();
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                
                // Iterate over each entry in the JAR file.
                ZipEntry entry;
                while((entry = zip.getNextEntry()) != null)
                {
                    String name = entry.getName();
                    
                    if (name.startsWith("files/") && name.endsWith(".txt"))
                    {
                        // Load character set as a menu item.
                        MenuItem item = new MenuItem();
                        item.loadMenuItem(name);
                        
                        if (!item.isEmpty())
                        {
                            // Add item to menu, if it contains valid data.
                            item.setActionCommand(Game.COMMAND_MENU);
                            item.addActionListener(listener);
                            menu.add(item);
                            items.add(item);
                        }
                    }
                }
            }
            else
            {
                System.out.println("Unable to open jar");
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public Deck getSelectedDecks()
    {
        Deck deck = new Deck();
        
        // Get state of option checkboxes.
        boolean useHiragana = hiragana.getState();
        boolean useKatakana = katakana.getState();
        boolean useDakuten = dakuten.getState();
        boolean useYouon = youon.getState();
        
        if (allSets.getState())
        {
            for (MenuItem item : items)
            {
                // Disable item when "All Sets" is selected.
                item.setEnabled(false);
                
                deck.addDeck(item.getDeck(useHiragana, useKatakana, useDakuten, useYouon));
            }
        }
        else
        {
            for (MenuItem item : items)
            {
                // Make sure item is enabled since "All Sets" is not selected.
                item.setEnabled(true);
                
                if (item.getState())
                    deck.addDeck(item.getDeck(useHiragana, useKatakana, useDakuten, useYouon));
            }
        }
        
        deck.shuffle();
        
        return deck;
    }
}
