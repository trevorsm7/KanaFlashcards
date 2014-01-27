import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.util.*;
import java.io.*;

public class Game extends JPanel implements ActionListener
{
    // The default dimensions of the application window.
    public static final int DEFAULT_WIDTH = 480;
    public static final int DEFAULT_HEIGHT = 320;
    
    // String constants used for UI event names.
    public static final String COMMAND_REPLAY = "replay";
    public static final String COMMAND_NEXT = "next";
    public static final String COMMAND_MENU = "menu";
    
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Kana Practice");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create game object.
        Game game = new Game();
        frame.add(game);
        frame.setJMenuBar(game.getMenu());
        frame.pack();
        
        // Position frame in center of display.
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension location = tk.getScreenSize();
        location.width = (location.width - DEFAULT_WIDTH) / 2;
        location.height = (location.height - DEFAULT_HEIGHT) / 2;
        frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        frame.setLocation(location.width, location.height);
        
        frame.setVisible(true);
        game.buttonNext.requestFocus();
    }
    
    private Menu menu;
    private Deck deck;
    private Card card;
    private Clip sound;
    private boolean showQuestion;
    private boolean menuChanged;
    
    private JButton buttonReplay;
    private JButton buttonNext;
    private JLabel labelQuestion;
    private JLabel labelSymbol;
    
    public Game()
    {
        super(new GridBagLayout());
        
        deck = null;
        card = null;
        sound = null;
        showQuestion = false;
        menuChanged = true;
        
        // Create menu.
        menu = new Menu();
        menu.loadMenu(this);
        
        // Set up grid bag layout.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        
        // Create a label for the description of the current card.
        labelQuestion = new JLabel("", JLabel.CENTER);
        labelQuestion.setFont(new Font("Serif", Font.PLAIN, 24));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.weighty = 0.0;
        add(labelQuestion, c);
        
        // Create a label for the symbol to display on the card.
        labelSymbol = new JLabel("", JLabel.CENTER);
        labelSymbol.setFont(new Font("Serif", Font.PLAIN, 160));
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.weighty = 1.0;
        add(labelSymbol, c);
        
        // Create a button to replay the active audio clip.
        buttonReplay = new JButton("Replay");
        buttonReplay.setActionCommand(COMMAND_REPLAY);
        buttonReplay.addActionListener(this);
        buttonReplay.setEnabled(false);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.weighty = 0.0;
        add(buttonReplay, c);
        
        // Create a button to flip to the answer or to draw a new card.
        buttonNext = new JButton("Next");
        buttonNext.setActionCommand(COMMAND_NEXT);
        buttonNext.addActionListener(this);
        buttonNext.setEnabled(menu.hasSelection()); // enable if there are available cards
        c.gridx = 1;
        c.gridy = 2;
        c.weighty = 0.0;
        add(buttonNext, c);
    }
    
    public Menu getMenu() {return menu;}
    
    public void getNextCard()
    {
        // Update deck if the settings have changed.
        if (menuChanged)
        {
            menuChanged = false;
            deck = menu.getSelectedDecks();
        }
        
        // Deck is empty, clear the screen.
        if (deck.isEmpty())
        {
            card = null;
            labelQuestion.setText("");
            labelSymbol.setText("");
            buttonReplay.setEnabled(false);
            buttonNext.setEnabled(false);
            return;
        }
        
        // Get the next card and display the question.
        card = deck.nextCard();
        showQuestion = true;
        labelQuestion.setText(card.getQuestion());
        labelSymbol.setText(card.getQuestionLabel());
        loadSound(card.getQuestionSound());
        buttonNext.setEnabled(true);
    }
    
    public void loadSound(String filename)
    {
        if (filename != null)
        {
            try
            {
                // Load the audio clip and play it.
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource(filename));
                sound = AudioSystem.getClip();
                sound.open(audioIn);
                sound.start();
                
                buttonReplay.setEnabled(true);
            }
            catch (Exception ex)
            {
                sound = null;
                buttonReplay.setEnabled(false);
            }
        }
        else
        {
            sound = null;
            buttonReplay.setEnabled(false);
        }
    }
    
    public void actionPerformed(ActionEvent event)
    {
        String command = event.getActionCommand();
        
        if (command.equals(COMMAND_NEXT))
        {
            if (showQuestion && card != null)
            {
                // Display the answer.
                showQuestion = false;
                labelQuestion.setText("");
                labelSymbol.setText(card.getAnswerLabel());
                loadSound(card.getAnswerSound());
            }
            else
            {
                // No card or answer is being displayed, get new card.
                getNextCard();
            }
        }
        else if (command.equals(COMMAND_REPLAY))
        {
            if (sound != null)
            {
                // Restart the audio clip.
                sound.stop();
                sound.setFramePosition(0);
                sound.start();
            }
        }
        else if (command.equals(COMMAND_MENU))
        {
            menuChanged = true;
            
            // If the screen is blank, enable Next only if there are available cards.
            if (!showQuestion && card == null)
                buttonNext.setEnabled(menu.hasSelection());
        }
    }
}
