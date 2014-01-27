import javax.swing.*;
import java.util.*;
import java.io.*;

public class MenuItem extends JCheckBoxMenuItem
{
    private static final int NORMAL = 0;
    private static final int DAKUTEN = 1;
    private static final int YOUON = 2;
    private static final int BOTH = 3;
    
    private Deck[] hiraToSound;
    private Deck[] kataToSound;
    private Deck[] hiraToKata;
    
    public MenuItem()
    {
        this(null);
    }
    
    public MenuItem(String description)
    {
        super(description);
        
        // Create blank decks.
        hiraToSound = new Deck[4];
        kataToSound = new Deck[4];
        hiraToKata = new Deck[4];
        
        for (int i = 0; i < 4; i++)
        {
            hiraToSound[i] = new Deck();
            kataToSound[i] = new Deck();
            hiraToKata[i] = new Deck();
        }
    }
    
    public void loadMenuItem(String filename)
    {
        Scanner scan = null;
        
        try
        {
            scan = new Scanner(getClass().getResourceAsStream(filename), "UTF-8");
            
            // Use first line of file as the menu item name.
            init(scan.nextLine(), null);
            
            while (true)
            {
                if (!scan.hasNext())
                    break;
                
                // Read a character from the file.
                String type = scan.next();
                String hira = scan.next();
                String kata = scan.next();
                String roma = scan.next();
                String sound = scan.next();
                
                // Parse dakuten/youon flags.
                int index;
                if (type.equals("n"))
                    index = NORMAL;
                else if (type.equals("d"))
                    index = DAKUTEN;
                else if (type.equals("y"))
                    index = YOUON;
                else if (type.equals("dy"))
                    index = BOTH;
                else
                {
                    System.err.println("Can't parse type \"" + type + "\" in file " + filename);
                    continue;
                }
                
                // Populate decks with new cards.
                hiraToSound[index].addCard(new Card("Read this hiragana", hira, null, roma, sound));
                hiraToSound[index].addCard(new Card("Write in hiragana", roma, sound, hira, sound));
                kataToSound[index].addCard(new Card("Read this katakana", kata, null, roma, sound));
                kataToSound[index].addCard(new Card("Write in katakana", roma, sound, kata, sound));
                hiraToKata[index].addCard(new Card("Write in katakana", hira, null, kata, sound));
                hiraToKata[index].addCard(new Card("Write in hiragana", kata, null, hira, sound));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (scan != null)
                scan.close();
        }
    }
    
    public boolean isEmpty()
    {
        // Return false if any of the sets are non-empty.
        // Note: hiraToSound, kataToSound, and hiraToKata should be the same length,
        // so I am just checking hiraToSound.
        for (int i = 0; i < 4; i++)
            if (!hiraToSound[i].isEmpty())
                return false;
        
        return true;
    }
    
    public Deck getDeck(boolean useHiragana, boolean useKatakana, boolean useDakuten, boolean useYouon)
    {
        Deck allHiraToSound = new Deck();
        Deck allKataToSound = new Deck();
        Deck allHiraToKata = new Deck();
        
        // Get all non-dakuten, non-youon characters.
        allHiraToSound.addDeck(hiraToSound[NORMAL]);
        allKataToSound.addDeck(kataToSound[NORMAL]);
        allHiraToKata.addDeck(hiraToKata[NORMAL]);
        
        // Get all characters with dakuten only.
        if (useDakuten)
        {
            allHiraToSound.addDeck(hiraToSound[DAKUTEN]);
            allKataToSound.addDeck(kataToSound[DAKUTEN]);
            allHiraToKata.addDeck(hiraToKata[DAKUTEN]);
        }
        
        // Get all non-dakuten youon characters.
        if (useYouon)
        {
            allHiraToSound.addDeck(hiraToSound[YOUON]);
            allKataToSound.addDeck(kataToSound[YOUON]);
            allHiraToKata.addDeck(hiraToKata[YOUON]);
        }
        
        // Get all youon characters with dakuten.
        if (useDakuten && useYouon)
        {
            allHiraToSound.addDeck(hiraToSound[BOTH]);
            allKataToSound.addDeck(kataToSound[BOTH]);
            allHiraToKata.addDeck(hiraToKata[BOTH]);
        }
        
        Deck deck = new Deck();
        
        // Use hiragana related cards.
        if (useHiragana)
            deck.addDeck(allHiraToSound);
        
        // Use katakana related card.
        if (useKatakana)
            deck.addDeck(allKataToSound);
        
        // Use cards with both hiragana and katakana.
        if (useHiragana && useKatakana)
            deck.addDeck(allHiraToKata);
        
        return deck;
    }
}
