import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

class Deck
{
    private ArrayList<Card> deck;
    private Iterator<Card> iter;
    
    public Deck()
    {
        deck = new ArrayList<Card>();
        iter = deck.iterator();
    }
    
    // Add a new card to the deck.
    public void addCard(Card card)
    {
        deck.add(card);
    }
    
    // Copy contents of other deck into this one.
    public void addDeck(Deck other)
    {
        if (other == null || other.isEmpty())
            return;
        
        deck.addAll(other.deck);
    }
    
    public boolean isEmpty()
    {
        return deck.isEmpty();
    }
    
    // Shuffle contents of array and reset iterator.
    public void shuffle()
    {
        Collections.shuffle(deck);
        iter = deck.iterator();
    }
    
    // Get the next card, reshuffling if necessary.
    public Card nextCard()
    {
        if (!iter.hasNext())
            shuffle();
        
        return iter.next();
    }
}
