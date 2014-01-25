class Card
{
    private String m_question;
    private String m_questionLabel;
    private String m_questionSound;
    private String m_answerLabel;
    private String m_answerSound;
    
    public Card(String question, String questionLabel, String questionSound, String answerLabel, String answerSound)
    {
        m_question = question;
        m_questionLabel = questionLabel;
        m_questionSound = questionSound;
        m_answerLabel = answerLabel;
        m_answerSound = answerSound;
    }
    
    public String getQuestion() {return m_question;}
    public String getQuestionLabel() {return m_questionLabel;}
    public String getQuestionSound() {return m_questionSound;}
    public String getAnswerLabel() {return m_answerLabel;}
    public String getAnswerSound() {return m_answerSound;}
}
