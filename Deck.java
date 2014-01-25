import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Deck
{
    private ArrayList<Card> deck;
    private Iterator<Card> iter;
    
    public Deck()
    {
        deck = null;
        iter = null;
    }
    
    // Load cards from text file.
    public void loadDeck(String filename)
    {
        deck = new ArrayList<Card>();
        Scanner scan = null;
        
        try
        {
            //scan = new Scanner(new FileReader(filename));
            scan = new Scanner(getClass().getResourceAsStream(filename), "UTF-8");
            
            while (true)
            {
                if (!scan.hasNextLine())
                    break;
                
                String question = scan.nextLine();
                //int qChar = scan.nextInt(16);
                String qLabel = scan.next();
                String qSound = scan.next();
                //int aChar = scan.nextInt(16);
                String aLabel = scan.next();
                String aSound = scan.next();
                
                // Kludge to get this to work.
                if (scan.hasNextLine())
                    scan.nextLine();
                
                // Convert unicode to string.
                //String qLabel = new String(Character.toChars(qChar));
                //String aLabel = new String(Character.toChars(aChar));
                
                if (qSound.equals("null"))
                    qSound = null;
                if (aSound.equals("null"))
                    aSound = null;
                
                // Add new card to deck.
                deck.add(new Card(question, qLabel, qSound, aLabel, aSound));
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
        
        shuffle();
    }
    
    public void addDeck(Deck other)
    {
        if (other == null || other.isEmpty())
            return;
        
        if (deck == null)
            deck = new ArrayList<Card>();
        
        deck.addAll(other.deck);
    }
    
    public boolean isEmpty()
    {
        return (deck == null) || (deck.isEmpty());
    }
    
    // Shuffle contents of array and reset iterator.
    public void shuffle()
    {
        if (deck == null)
            return;
        
        Collections.shuffle(deck);
        iter = deck.iterator();
    }
    
    // Get the next card, reshuffling if necessary.
    public Card nextCard()
    {
        if (iter == null)
            return null;
        
        if (!iter.hasNext())
            shuffle();
        
        return iter.next();
    }
}
