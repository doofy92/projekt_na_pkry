package klient;

/** klasa odpowiedzialana za reprezentowanie buffora */
public class Buffor 
{   
    /**zmiena przechowujaca buffor */
    private int buffor;
    
    /**konstruktor */
    public Buffor()
    {
        
    }
    
    /**konstruktor */
    public Buffor(int buffor)
    {
        this.buffor = buffor;
    }
    
    /**metoda zwracajaca buffor*/
    public int zwrocBuffor()
    {
        return this.buffor;
    }
    
    /**metoda ustawiajaca buffor*/
    public void ustawDana(int dana)
    {
        this.buffor = dana;
    }
    
    
    
}
