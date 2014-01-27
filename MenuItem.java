import javax.swing.*;
import java.util.*;
import java.io.*;

public class MenuItem extends JCheckBoxMenuItem
{
    private Deck hiraToSound;
    private Deck kataToSound;
    private Deck hiraToKata;
    
    public MenuItem()
    {
        this(null);
    }
    
    public MenuItem(String description)
    {
        super(description);
        
        // Create blank decks.
        hiraToSound = new Deck();
        kataToSound = new Deck();
        hiraToKata = new Deck();
    }
    
    public void loadMenuItem(String filename)
    {
        Scanner scan = new Scanner(getClass().getResourceAsStream(filename), "UTF-8");
        
        // Use first line of file as the menu item name.
        init(scan.nextLine(), null);
        
        while (true)
        {
            if (!scan.hasNext())
                break;
            
            // Read a character from the file.
            String hira = scan.next();
            String kata = scan.next();
            String roma = scan.next();
            String sound = scan.next();
            
            // Populate decks with new cards.
            hiraToSound.addCard(new Card("Read this hiragana", hira, null, roma, sound));
            hiraToSound.addCard(new Card("Write in hiragana", roma, sound, hira, sound));
            kataToSound.addCard(new Card("Read this katakana", kata, null, roma, sound));
            kataToSound.addCard(new Card("Write in katakana", roma, sound, kata, sound));
            hiraToKata.addCard(new Card("Write in katakana", hira, null, kata, sound));
            hiraToKata.addCard(new Card("Write in hiragana", kata, null, hira, sound));
        }
        
        scan.close();
    }
    
    public boolean isEmpty()
    {
        // All decks should have the same number of cards, so just check one deck.
        return hiraToSound.isEmpty();
    }
    
    public Deck getDeck(boolean useHiragana, boolean useKatakana)
    {
        Deck deck = new Deck();
        
        // Use hiragana related cards.
        if (useHiragana)
            deck.addDeck(hiraToSound);
        
        // Use katakana related card.
        if (useKatakana)
            deck.addDeck(kataToSound);
        
        // Use cards with both hiragana and katakana.
        if (useHiragana && useKatakana)
            deck.addDeck(hiraToKata);
        
        return deck;
    }
}
