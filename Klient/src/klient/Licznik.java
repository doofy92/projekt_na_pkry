package klient;

/**klasa odpowiedzialan za reprezentowanie licznika*/
public class Licznik 
{
    /**zmiena przechowujaca licznik*/
    int licznik;
    
    /**konstruktor*/
    public Licznik()
    {
        this.licznik = -1;
    }
    
     /**metoda zwracajaca licznik*/
    public int zwrocLicznik()
    {
        return ++this.licznik;
    }
    
     /**metoda aktualny licznik*/
    public int zwrocAktualnyLicznik()
    {
        return this.licznik;
    }
}
